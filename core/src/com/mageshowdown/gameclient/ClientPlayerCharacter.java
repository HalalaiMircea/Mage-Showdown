package com.mageshowdown.gameclient;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.*;
import com.mageshowdown.packets.Network.*;

public class ClientPlayerCharacter extends PlayerCharacter implements AnimatedActorInterface {

    private GameClient myClient=GameClient.getInstance();

    private TextureRegion currShieldFrame;
    private TextureRegion currFrozenFrame;

    private boolean moveLeft=false;
    private boolean moveRight=false;
    private boolean jump=false;
    private boolean isMyPlayer=false;
    private boolean shoot=false;
    private String userName;

    public ClientPlayerCharacter(Stage stage, Vector2 position, String userName, boolean isMyPlayer) {
        super(stage,position,true);
        this.isMyPlayer=isMyPlayer;

        if(isMyPlayer) {
            addAnimation(4, 1, 1.2f, "idle", ClientAssetLoader.friendlyIdleSpritesheet);
            addAnimation(2, 1, .8f, "jumping", ClientAssetLoader.friendlyJumpingSpritesheet);
            addAnimation(8, 1, 1f, "running", ClientAssetLoader.friendlyRunningSpritesheet);
        }else{
            addAnimation(4, 1, 1.2f, "idle", ClientAssetLoader.enemyIdleSpritesheet);
            addAnimation(2, 1, .8f, "jumping", ClientAssetLoader.enemyJumpingSpritesheet);
            addAnimation(8, 1, 1f, "running", ClientAssetLoader.enemyRunningSpritesheet);
        }

        addAnimation(5,4,2f,"energy shield",ClientAssetLoader.energyShieldSpritesheet);
        addAnimation(5,6,2f,"frozen",ClientAssetLoader.frozenSpritesheet);
        this.userName=userName;


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
                    shoot=true;
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
                    myClient.sendTCP(ku);
                }
                else if(keycode==Input.Keys.D) {
                    moveRight=false;
                    myClient.sendTCP(ku);
                }
                else if(keycode==Input.Keys.W){
                    jump=false;
                    myClient.sendTCP(ku);
                }
                return true;
            }

        });

    }


    @Override
    public void act(float delta) {
        sendInputPackets();
        pickFrame();
        calcState();
        updateWeaponPos();
        updateFrozenState();

        updateGameActor(delta);
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(dmgImmune)
            batch.setColor(batch.getColor().r,batch.getColor().g,batch.getColor().b,55);
        if(currFrame!=null)
            batch.draw(currFrame,getX(),getY(),getWidth()*getScaleX(),getHeight()*getScaleY());
        if(energyShield>0 && currShieldFrame!=null)
            batch.draw(currShieldFrame,getX()-getWidth()/2,getY()-getHeight()/2,getWidth()*getScaleX()*1.5f,getHeight()*getScaleY()*1.5f);
        if(frozen && currFrozenFrame!=null)
            batch.draw(currFrozenFrame,getX()-getWidth()/2,getY()-getHeight()/2,getWidth()*getScaleX()*1.5f,getHeight()*getScaleY()*1.5f);
        if(dmgImmune)
            batch.setColor(batch.getColor().r,batch.getColor().g,batch.getColor().b,255);
    }


    @Override
    public void pickFrame() {
        boolean looping=true;
        if(animations.get("energy shield")!=null)
            currShieldFrame=animations.get("energy shield").getKeyFrame(passedTime,looping);
        if(frozen && animations.get("frozen")!=null)
            currFrozenFrame=animations.get("frozen").getKeyFrame(frozenTimer,looping);

        /*
         * if the character is grounded, we have to see if hes moving left or right or standing and change the animation accordingly
         * regardless of wether its flying or its grounded, the animation frame needs
         * to be flipped if youre going left, looking right being the "default" position
         */
        switch(verticalState){
            case GROUNDED:
                switch (horizontalState) {
                    case STANDING:
                        if(animations.get("idle")!=null)
                            currFrame = animations.get("idle").getKeyFrame(passedTime, looping);
                        break;
                    default:
                        if(animations.get("running")!=null)
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

    private void sendInputPackets(){
        MoveKeyDown keyPress=new MoveKeyDown();

        if(moveLeft){
            keyPress.keycode=Input.Keys.A;
            myClient.sendTCP(keyPress);
        }else if (moveRight){
            keyPress.keycode=Input.Keys.D;
            myClient.sendTCP(keyPress);
        }else if (jump){
            keyPress.keycode=Input.Keys.W;
            myClient.sendTCP(keyPress);
        }
        if(shoot){
            shootMyWeapon();
        }
    }

    private void calcState(){
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
    }

    private void shootMyWeapon(){
        ShootProjectile sp=new ShootProjectile();
        Vector2 shootingOrigin=new Vector2((frostCrystal.getX()),(frostCrystal.getY()+ frostCrystal.getHeight()/2));
        float rotation= GameWorld.getMouseVectorAngle(shootingOrigin);
        Vector2 direction=GameWorld.getNormalizedMouseVector(shootingOrigin);

        sp.id=myClient.getID();
        sp.rot=rotation;
        sp.dir=direction;
        myClient.sendTCP(sp);
        frostCrystal.shoot(direction,rotation,myClient.getID());

        shoot=false;
    }

    public void setMyPlayer(boolean myPlayer) {
        isMyPlayer = myPlayer;
    }

}
