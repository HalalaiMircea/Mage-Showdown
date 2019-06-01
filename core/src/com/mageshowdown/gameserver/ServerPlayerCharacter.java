package com.mageshowdown.gameserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.Bomb;
import com.mageshowdown.gamelogic.FreezeProjectile;
import com.mageshowdown.gamelogic.Orb;
import com.mageshowdown.gamelogic.PlayerCharacter;
import com.mageshowdown.packets.Network;

public class ServerPlayerCharacter extends PlayerCharacter {

    private int moveDirection=-1;
    private int id;

    //whenever you're hit, there will be a short cooldown before your shield starts regenerating
    //when that cooldown expires, youll be regenerating the energy shield at SHIELD_REGEN_RATE/second
    private final float SHIELD_REGEN_RATE=1f;
    private final float SHIELD_COOLDOWN=3f;
    private float shieldRegenTimer=0f;
    private boolean regenShield=true;

    private final float DMG_IMMUNE_DURATION =.80f;
    private float dmgImmuneTimer=0f;

    private final float FREEZE_SLOWING_FACTOR=.5f;

    public ServerPlayerCharacter(Stage stage, Vector2 pos, int orbEquipped, int id) {
        super(stage,pos,orbEquipped,false);
        this.id=id;
    }

    @Override
    public void act(float delta) {
        updateDmgImmuneState();
        updateFrozenState();
        updateShieldState();
        updateOrbPosition();

        switch (moveDirection){
            case Input.Keys.A:
                velocity.x=-3.5f;
                break;
            case Input.Keys.D:
                velocity.x=3.5f;
                break;
            case Input.Keys.W:
                if(body.getLinearVelocity().y<0.01f && body.getLinearVelocity().y>-0.01f)
                    body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x,.5f),body.getPosition(),false);
                    break;
            default:
                /*
                * if the value isnt one of the keys that makes an action,
                * the body wont be moving horizontally
                 */
                velocity.x=0;
                break;
        }
        if(body!=null){
            if(body.getLinearVelocity().y!=0){
                velocity.y=body.getLinearVelocity().y;
            }
        }

        super.act(delta);
        moveDirection=-1;
    }

    @Override
    protected void updateFrozenState() {
        super.updateFrozenState();
        if(frozen)
            velocity.x*=FREEZE_SLOWING_FACTOR;
    }

    private void updateDmgImmuneState(){
        if(dmgImmune)
        {
            dmgImmuneTimer+= Gdx.graphics.getDeltaTime();
            if(dmgImmuneTimer> DMG_IMMUNE_DURATION){
                dmgImmune=false;
                dmgImmuneTimer=0f;
            }
        }

    }

    private void updateShieldState(){
        if(!regenShield){
            shieldRegenTimer+=Gdx.graphics.getDeltaTime();
            if(shieldRegenTimer>=SHIELD_COOLDOWN){
                regenShield=true;
                shieldRegenTimer=0f;
            }
        }else{
            if(energyShield<MAXIMUM_ENERGY_SHIELD)
                energyShield+=SHIELD_REGEN_RATE*Gdx.graphics.getDeltaTime();
        }
    }

    public void respawn(Vector2 respawnPos){
        setQueuedPos(respawnPos);
        health=MAXIMUM_HEALTH;
        energyShield=MAXIMUM_ENERGY_SHIELD;
    }


    public void raiseScore(int value){
        score+=value;
    }

    public void damageBy(float damageValue, Object object) {
        if (!dmgImmune) {
            //everytime youre hit the cooldown for shield regen resets
            regenShield=false;
            shieldRegenTimer=0f;

            if(energyShield>0){
                energyShield-=damageValue;
                if(energyShield<0){
                    health+=energyShield;
                    energyShield=0;
                }
            }else health-=damageValue;

            //check what the character gets damaged by
            if(object instanceof FreezeProjectile || object instanceof Bomb && ((Bomb)(object)).getSpellType()== Orb.SpellType.FROST){
                dmgImmune = true;
                frozen=true;
            }
        }
    }

    public void castSpellProjectile(Network.CastSpellProjectile packet){
        Vector2 direction = new Vector2((float)Math.cos(packet.rot*Math.PI/180),(float)Math.sin(packet.rot*Math.PI/180));

        currentOrb.castSpellProjectile(direction,packet.rot,packet.id);
        GameServer.getInstance().sendToAllExceptTCP(packet.id,packet);
    }

    public void castBomb(Network.CastBomb packet){
        currentOrb.castBomb(packet.pos,packet.id);
        GameServer.getInstance().sendToAllExceptTCP(packet.id,packet);
    }

    public void addKill(){
        kills++;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int getId() {
        return id;
    }

}
