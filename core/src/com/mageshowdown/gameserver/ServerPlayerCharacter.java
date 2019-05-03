package com.mageshowdown.gameserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.PlayerCharacter;
import com.mageshowdown.gamelogic.Projectile;
import com.mageshowdown.packets.Network;

public class ServerPlayerCharacter extends PlayerCharacter {

    private int moveDirection=-1;
    private int id;

    //whenever you're hit, there will be a short cooldown before your shield starts regenerating
    //when that cooldown expires, youll be regenerating the energy shield at SHIELD_REGEN_RATE/second
    private float SHIELD_REGEN_RATE=5f;
    private float SHIELD_COOLDOWN=3f;
    private float shieldRegenTimer=0f;
    private boolean regenShield=true;

    private final float DMG_IMMUNE_DURATION =.80f;
    private float dmgImmuneTimer=0f;

    private final float FREEZE_SLOWING_FACTOR=.5f;

    public ServerPlayerCharacter(Stage stage, Vector2 pos, int id) {
        super(stage,pos,false);
        this.id=id;
    }

    @Override
    public void act(float delta) {
        updateDmgImmuneState();
        updateFrozenState();
        updateShieldState();

        switch (moveDirection){
            case Input.Keys.A:
                velocity.x=-2.5f;
                break;
            case Input.Keys.D:
                velocity.x=2.5f;
                break;
            case Input.Keys.W:
                if(body.getLinearVelocity().y<0.01f && body.getLinearVelocity().y>-0.01f)
                    body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x,.22f),body.getPosition(),false);
                break;
            default:
                /*
                * if the value isnt one of the keys that makes an action,
                * the body wont be moving horizontally
                 */
                velocity.x=0;
                break;
        }

        if(frozen)
            velocity.x*=FREEZE_SLOWING_FACTOR;

        if(body!=null){
            if(body.getLinearVelocity().y!=0){
                velocity.y=body.getLinearVelocity().y;
            }
        }

        if(frostCrystal !=null)
            frostCrystal.updatePosition(new Vector2(getX(),getY()));
        super.act(delta);
        moveDirection=-1;
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

    public void damageBy(int damageValue, Object object) {
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
            if(object instanceof Projectile){
                dmgImmune = true;
                frozen=true;
            }
        }
    }

    public void shootProjectile(Network.ShootProjectile packet){
        frostCrystal.shoot(packet.dir,packet.rot,packet.id);
        GameServer.getInstance().sendToAllExceptTCP(packet.id,packet);
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
