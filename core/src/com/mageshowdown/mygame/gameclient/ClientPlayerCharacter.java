package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.gamelogic.*;

public class ClientPlayerCharacter extends DynamicGameActor implements AnimatedActorInterface{

    private Weapon myWeapon;
    private boolean moveLeft=false;
    private boolean moveRight=false;
    private boolean jump=false;
    private boolean isMyPlayer=false;

    public ClientPlayerCharacter(Stage stage, Vector2 position) {
        super(stage,position, new Vector2(22,32),1.5f   );
        addAnimation(4,1,1.2f,"idle",ClientAssetLoader.idlePlayerSpriteSheet);
        addAnimation(2,1,.8f,"jumping",ClientAssetLoader.jumpingPlayerSpritesheet);
        addAnimation(8,1,1f,"running",ClientAssetLoader.runningPlayerSpritesheet);

        myWeapon=new Weapon(stage);
        createBody(BodyDef.BodyType.DynamicBody);
        body.setFixedRotation(true);
        addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.D) {
                    moveRight=true;
                    System.out.println("AM APASAT");
                    //velocity.x=2.5f;
                }
                else if(keycode==Input.Keys.A){
                    moveLeft=true;
                    //velocity.x=-2.5f;
                }
                if(keycode==Input.Keys.W){
                    jump=true;
                    //we dont want to deal separately with gravity so when jumping we just apply an impulse
                    //body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x,.22f),body.getPosition(),false);
                }
                if(keycode==Input.Keys.Q){
                    myWeapon.shoot();
                }

                return true;
            }


            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                //we stop moving in a direction only if we release the key which indicates said direction, not either one
                if(keycode==Input.Keys.A) {
                    moveLeft=false;
                }
                else if(keycode==Input.Keys.D) {
                    moveRight=false;
                }
                else if(keycode==Input.Keys.W){
                    jump=false;
                }
                return true;
            }

        });

    }

    public void setPosition(Vector2 position){
        setPosition(position.x,position.y);
    }
    @Override
    public void act(float delta) {
        KeyPress keyPress=new KeyPress();

        if(moveLeft){
            keyPress.keycode=Input.Keys.A;
            GameWorld.myClient.sendUDP(keyPress);
        }else if (moveRight){
            keyPress.keycode=Input.Keys.D;
            GameWorld.myClient.sendUDP(keyPress);
        }else if (jump){
            keyPress.keycode=Input.Keys.W;
            GameWorld.myClient.sendUDP(keyPress);
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
            case STANDING:
                //only the client's own character flips in the direction of the mouse
                if(isMyPlayer)
                    if (GameWorld.getMousePos().x < getX()) {
                        if (!currFrame.isFlipX()) {
                            currFrame.flip(true, false);
                        }
                    } else {
                        if (currFrame.isFlipX())
                            currFrame.flip(true, false);
                    }
                break;
        }

        myWeapon.updatePosition(new Vector2(getX(),getY()));
        super.act(delta);
    }

    public void myPlayerAct(){
        if(horizontalState==HorizontalState.STANDING) {
            if (GameWorld.getMousePos().x < getX()) {
                if (!currFrame.isFlipX()) {
                    currFrame.flip(true, false);
                }
            } else {
                if (currFrame.isFlipX())
                    currFrame.flip(true, false);
            }
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        //System.out.println("im getting drawn at"+getX()+" "+getY());
        if(currFrame!=null)
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

    public void setMyPlayer(boolean myPlayer) {
        isMyPlayer = myPlayer;
    }
}
