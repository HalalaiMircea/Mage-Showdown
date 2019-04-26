package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class PlayerCharacter extends DynamicGameActor{

    protected Weapon myWeapon;

    protected int health=15;
    protected boolean dmgImmune=false;
    protected int score=0;

    protected PlayerCharacter(Stage stage, Vector2 position, boolean loadWeaponAnimation){
        super(stage,position, new Vector2(22,32),1.5f);

        createBody(BodyDef.BodyType.DynamicBody);
        body.setFixedRotation(true);
        myWeapon=new Weapon(stage,loadWeaponAnimation);
    }

    @Override
    public void destroyActor() {
        myWeapon.destroyActor();
        super.destroyActor();
    }

    protected void updateWeaponPos(){
        if(myWeapon!=null)
            myWeapon.updatePosition(new Vector2(getX(),getY()));
    }

    public Weapon getMyWeapon() {
        return myWeapon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDmgImmune() {
        return dmgImmune;
    }

    public void setDmgImmune(boolean dmgImmune) {
        this.dmgImmune = dmgImmune;
    }
}
