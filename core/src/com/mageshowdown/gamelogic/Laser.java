package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Laser extends Ammo implements AnimatedActorInterface{

    private static final float duration=.25f;


    public Laser(Stage stage, Vector2 position, float rotation, int id, int ownerId){
        super(stage,new Vector2(0f,0f),position,new Vector2(220,31),new Vector2(1.5f,1.2f),new Vector2(325,15),rotation,id,ownerId,2f);
        setOrigin(0,getHeight()/2);
        createBody(rotation,new Vector2(0,getOriginY()/2),BodyDef.BodyType.StaticBody);

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
    protected void updatePositionFromBody() {
        Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
        setPosition(convPosition.x, convPosition.y-getOriginY());
        setRotation(body.getAngle() * 180 / (float) Math.PI);
    }

    @Override
    public void pickFrame() {
        if(animations.containsKey("idle"))
            currFrame=animations.get("idle").getKeyFrame(passedTime,false);
    }
}
