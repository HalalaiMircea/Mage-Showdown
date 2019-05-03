package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class PlayerCharacter extends DynamicGameActor{

    protected float MAXIMUM_ENERGY_SHIELD=25;
    protected float MAXIMUM_HEALTH=15;

    protected Weapon frostCrystal;

    protected float energyShield=MAXIMUM_ENERGY_SHIELD;
    protected float health=MAXIMUM_HEALTH;
    protected boolean dmgImmune=false;

    protected final float FREEZE_DURATION=2f;
    protected boolean frozen=false;
    protected float frozenTimer=0f;

    protected int score=0;

    protected PlayerCharacter(Stage stage, Vector2 position, boolean loadWeaponAnimation){
        super(stage,position, new Vector2(22,32),1.5f);

        createBody(BodyDef.BodyType.DynamicBody);
        body.setFixedRotation(true);
        frostCrystal =new Weapon(stage,loadWeaponAnimation,1.5f,16,2);
    }

    @Override
    public void destroyActor() {
        frostCrystal.destroyActor();
        super.destroyActor();
    }

    protected void updateWeaponPos(){
        if(frostCrystal !=null)
            frostCrystal.updatePosition(new Vector2(getX(),getY()));
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

    public Weapon getFrostCrystal() {
        return frostCrystal;
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
}
