package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Laser extends Ammo implements AnimatedActorInterface{

    private final float duration;

    //need to remember the rotation and set it every frame the laser exists
    private float rotation;

    public Laser(Stage stage, Vector2 position, float rotation, int id, int ownerId){
        super(stage,new Vector2(0f,0f),position,new Vector2(220,31),1f,rotation,id,ownerId,2f);
        this.rotation=rotation+180f;
        duration=.25f;
        addAnimation(1,7,.25f,"idle",ClientAssetLoader.fireLaserSpritesheet);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(passedTime>=duration) {
            setExpired(true);
        }

        setRotation(rotation);
        pickFrame();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(currFrame!=null)
            batch.draw(currFrame,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

    @Override
    public void pickFrame() {
        if(animations.get("idle")!=null)
            currFrame=animations.get("idle").getKeyFrame(passedTime,false);
    }
}
