package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Bomb extends Ammo implements AnimatedActorInterface{

    private final float duration;
    private Weapon.AmmoType ammoType;
    private float explosionTime=0f;

    boolean activated=false;

    public Bomb(Stage stage, Vector2 position, float rotation, int id, int ownerId, Weapon.AmmoType ammoType){
        super(stage,new Vector2(0,0),position,new Vector2(190,190),new Vector2(.8f,.8f),rotation,id,ownerId,9);

        //we'll have to generate the body only when the explosion reaches a certain point
        GameWorld.bodiesToBeRemoved.add(body);

        this.ammoType=ammoType;
        switch(ammoType) {
            case FREEZE:
                duration=2.5f;
                addAnimation(5, 5, duration/2f, "explosion", ClientAssetLoader.freezeBombSpritesheet);
                addAnimation(5,4,duration/2f,"arm",ClientAssetLoader.armFreezeBombSpritesheet);
                break;
            case FIRE:
                duration=2.5f;
                addAnimation(5,4,duration/2f,"explosion",ClientAssetLoader.fireBombSpritesheet);
                addAnimation(5,4,duration/2f,"arm",ClientAssetLoader.armFireBombSpritesheet);
                break;
            default:
                duration=0;
                break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        pickFrame();
        if(activated){
            explosionTime+= Gdx.graphics.getDeltaTime();
        }
        if(explosionTime>duration/2f)
        {
            setExpired(true);
        }
        if(passedTime>duration/2f && !activated)
        {
            createBody(BodyDef.BodyType.StaticBody);
            body.getFixtureList().get(0).setSensor(true);
            body.setGravityScale(0f);
            activated=true;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(currFrame!=null)
            batch.draw(currFrame,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

    @Override
    public void pickFrame() {
        if(!activated){
            if(animations.get("arm")!=null)
                currFrame=animations.get("arm").getKeyFrame(passedTime,false);
        }else{
            if(animations.get("explosion")!=null)
                currFrame=animations.get("explosion").getKeyFrame(explosionTime,false);
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public Weapon.AmmoType getAmmoType() {
        return ammoType;
    }
}
