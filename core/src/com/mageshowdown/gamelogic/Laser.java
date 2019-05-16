package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Laser extends Ammo implements AnimatedActorInterface{

    private static final float duration=.25f;

    public Laser(Stage stage, Vector2 position, float rotation, int id, int ownerId){
        super(stage,new Vector2(0f,0f),position,new Vector2(220,31),new Vector2(3f,1f),rotation,id,ownerId,2f);
        activated=true;
        addAnimation(1,7,.25f,"idle",ClientAssetLoader.fireLaserSpritesheet);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(passedTime>=duration) {
            setExpired(true);
        }

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
