package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Game;
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
    private Vector2 queuedPos;
    private Vector2 queuedVel;
    private boolean canClear=false;

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
                }
                else if(keycode==Input.Keys.A){
                    moveLeft=true;
                }
                if(keycode==Input.Keys.W){
                    jump=true;
                }
                if(keycode==Input.Keys.Q){
                    myWeapon.shoot();
                }

                return true;
            }


            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                //if we stop moving we send a packet informing the server that we want to synchronize
                KeyUp ku=new KeyUp();
                ku.keycode=keycode;
                if(keycode==Input.Keys.A) {
                    moveLeft=false;
                    GameWorld.myClient.sendTCP(ku);
                }
                else if(keycode==Input.Keys.D) {
                    moveRight=false;
                    GameWorld.myClient.sendTCP(ku);
                }
                else if(keycode==Input.Keys.W){
                    jump=false;
                    GameWorld.myClient.sendTCP(ku);
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
        KeyDown keyPress=new KeyDown();

        if(moveLeft){
            keyPress.keycode=Input.Keys.A;
            GameWorld.myClient.sendTCP(keyPress);
        }else if (moveRight){
            keyPress.keycode=Input.Keys.D;
            GameWorld.myClient.sendTCP(keyPress);
        }else if (jump){
            keyPress.keycode=Input.Keys.W;
            GameWorld.myClient.sendTCP(keyPress);
        }

        pickFrame();

        if(body!=null) {
            if (body.getLinearVelocity().y > 0.0001f || body.getLinearVelocity().y < -0.0001f) {
                //if we detect that we start flying, then we need to reset the passed time so the animation wont loop
                if (verticalState != VerticalState.FLYING)
                    passedTime = 0f;

                verticalState = VerticalState.FLYING;
            } else verticalState = VerticalState.GROUNDED;


            if (body.getLinearVelocity().x > 0.0001f) {
                horizontalState = HorizontalState.GOING_RIGHT;
            } else if (body.getLinearVelocity().x < -0.0001f) {
                horizontalState = HorizontalState.GOING_LEFT;
            } else {
                horizontalState = HorizontalState.STANDING;
            }


            if (body.getLinearVelocity().y != 0) {
                velocity.y = body.getLinearVelocity().y;
            }
        }
        if(currFrame!=null) {
            switch (horizontalState) {
                case GOING_LEFT:
                    //as there is no setFlip() for TextureRegion we have to check if the texture's already flipped, libgdx pls
                    if (!currFrame.isFlipX())
                        currFrame.flip(true, false);
                    break;
                case GOING_RIGHT:
                    if (currFrame.isFlipX())
                        currFrame.flip(true, false);
                    break;
                case STANDING:
                    //only the client's own character flips in the direction of the mouse
                    if (isMyPlayer) {
                        if (GameWorld.getMousePos().x < getX()) {
                            if (!currFrame.isFlipX()) {
                                currFrame.flip(true, false);
                            }
                        } else {
                            if (currFrame.isFlipX())
                                currFrame.flip(true, false);
                        }
                    } else {
                        if (currFrame.isFlipX())
                            currFrame.flip(true, false);
                    }
                    break;
            }
        }

        if(myWeapon!=null)
            myWeapon.updatePosition(new Vector2(getX(),getY()));
        //super.act(delta);
        super.updateGameActor(delta);
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
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
                        currFrame = animations.get("running").getKeyFrame(passedTime,looping);
                        break;
                }
                break;
            case FLYING:
                looping=false;
                if(animations.get("jumping")!=null)
                    currFrame = animations.get("jumping").getKeyFrame(passedTime,looping);
                break;
        }
    }

    public void setMyPlayer(boolean myPlayer) {
        isMyPlayer = myPlayer;
    }

    public void setQueuedPos(Vector2 queuedPos) {
        this.queuedPos = queuedPos;
        canClear=true;
    }

    public void setQueuedVel(Vector2 queuedVel) {
        this.queuedVel = queuedVel;
    }

    public void clearQueue(){
        if(canClear){
            body.setTransform(queuedPos,body.getAngle());
            body.setLinearVelocity(queuedVel);
            canClear=false;
        }
    }
}
