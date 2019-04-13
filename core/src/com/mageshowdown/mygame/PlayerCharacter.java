package com.mageshowdown.mygame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class PlayerCharacter extends DynamicGameActor implements AnimatedActorInterface{

    private Weapon myWeapon;

    public PlayerCharacter(Stage stage) {
        super(stage,new Vector2(30, 450), new Vector2(22,32));
        addAnimation(4,1,1.2f,"idle",AssetLoader.idlePlayerSpriteSheet);
        addAnimation(2,1,.8f,"jumping",AssetLoader.jumpingPlayerSpritesheet);
        addAnimation(8,1,1f,"running",AssetLoader.runningPlayerSpritesheet);

        createBody(BodyDef.BodyType.DynamicBody);
        myWeapon=new Weapon(stage);
        addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.D) {
                    velocity.x=2.5f;
                }
                else if(keycode==Input.Keys.A){
                    velocity.x=-2.5f;
                }
                if(keycode==Input.Keys.W && body.getLinearVelocity().y<0.01f && body.getLinearVelocity().y>-0.01f){
                    //we dont want to deal separately with gravity so when jumping we just apply an impulse
                    body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x,.22f),body.getPosition(),false);
                }
                if(keycode==Input.Keys.Q){
                    myWeapon.shoot();
                }

                return true;
            }


            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                //we stop moving in a direction only if we release the key which indicates said direction, not either one
                if((keycode==Input.Keys.D && velocity.x>0 )|| (keycode==Input.Keys.A && velocity.x<0)) {
                    velocity.x=0;
                }
                return true;
            }

        });
    }

    @Override
    public void act(float delta) {
        if(body.getLinearVelocity().y>0.0001f || body.getLinearVelocity().y<-0.0001f) {
            //if we detect that we start flying, then we need to reset the passed time so the animation wont loop
            if(verticalState!=VerticalState.FLYING)
                passedTime=0f;

            verticalState = VerticalState.FLYING;
        }
        else verticalState=VerticalState.GROUNDED;

        if(body.getLinearVelocity().x>0.0001f){
            horizontalState=HorizontalState.GOING_RIGHT;
        }else if (body.getLinearVelocity().x<-0.0001f){
            horizontalState=HorizontalState.GOING_LEFT;
        }else {
            horizontalState = HorizontalState.STANDING;
        }
        if(body.getLinearVelocity().y!=0){
            velocity.y=body.getLinearVelocity().y;
        }

        pickFrame();

        switch(horizontalState){
            case GOING_LEFT:
                //as there is no setFlip() for TextureRegion we have to check if the texture's already flipped, libgdx pls
                if(!currFrame.isFlipX())
                    currFrame.flip(true,false);
                break;
            case GOING_RIGHT:
                if(currFrame.isFlipX())
                    currFrame.flip(true,false);
                break;
        }

        myWeapon.updatePosition(new Vector2(getX(),getY()));
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currFrame,getX(),getY(),getWidth()*getScaleX(),getHeight()*getScaleY());
    }

    @Override
    public void pickFrame() {
        boolean looping=true;
        /*
         * if the character is grounded, we have to see if hes moving left or right or standing and change the animation accordingly
         * regardless of wether its flying or its grounded, the animation frame needs
         * to be flipped if youre going left, looking right being the "default" position
         */
        switch(verticalState){
            case GROUNDED:
                switch (horizontalState) {
                    case STANDING:
                        currFrame = animations.get("idle").getKeyFrame(passedTime, looping);
                        break;
                    default:
                        currFrame=animations.get("running").getKeyFrame(passedTime,looping);
                        break;
                }
                break;
            case FLYING:
                looping=false;
                currFrame=animations.get("jumping").getKeyFrame(passedTime,looping);
                break;
        }
    }
}
