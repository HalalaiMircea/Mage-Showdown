package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;


public abstract class PlayerCharacter extends DynamicGameActor{
    protected Stage gameStage;

    protected float MAXIMUM_ENERGY_SHIELD=25;
    protected float MAXIMUM_HEALTH=15;

    protected Weapon frostWeapon;
    protected Weapon fireWeapon;
    protected Weapon currWeapon;

    protected float energyShield=MAXIMUM_ENERGY_SHIELD;
    protected float health=MAXIMUM_HEALTH;
    protected boolean dmgImmune=false;

    protected final float FREEZE_DURATION=2f;
    protected boolean frozen=false;
    protected float frozenTimer=0f;

    protected int score=0;
    protected int kills=0;

    protected PlayerCharacter(Stage stage, Vector2 position, boolean loadWeaponAnimation){
        super(stage,position, new Vector2(22,32),1.5f);

        createBody(BodyDef.BodyType.DynamicBody);
        gameStage=stage;
        body.setFixedRotation(true);

        frostWeapon=new Weapon(stage,loadWeaponAnimation,Weapon.AmmoType.FREEZE_BULLETS,1.5f,25,2);
        fireWeapon=new Weapon(stage,loadWeaponAnimation,Weapon.AmmoType.LASER,1.5f,25,1);

        frostWeapon.remove();
        fireWeapon.remove();

        currWeapon = fireWeapon;
        //currWeapon=frostWeapon;
        gameStage.addActor(currWeapon);
    }

    @Override
    public void destroyActor() {
        destroyWeapons();
        super.destroyActor();
    }

    protected void updateWeaponPos(){
        if(currWeapon !=null)
            currWeapon.updatePosition(new Vector2(getX(),getY()));
    }

    protected void updateFrozenState(){
        if(frozen){
            frozenTimer+=Gdx.graphics.getDeltaTime();
            if(frozenTimer>FREEZE_DURATION){
                frozen=false;
                frozenTimer=0f;
            }
        }
    }

    protected void destroyWeapons(){
        frostWeapon.destroyActor();
        fireWeapon.destroyActor();
    }

    public void switchMyWeapons(){
        currWeapon.remove();

        if(currWeapon.equals(frostWeapon))
            currWeapon=fireWeapon;
        else currWeapon=frostWeapon;

        gameStage.addActor(currWeapon);

    }

    public Weapon getCurrWeapon() {
        return currWeapon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getEnergyShield() {
        return energyShield;
    }

    public void setEnergyShield(float energyShield) {
        this.energyShield = energyShield;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isDmgImmune() {
        return dmgImmune;
    }

    public void setDmgImmune(boolean dmgImmune) {
        this.dmgImmune = dmgImmune;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}
