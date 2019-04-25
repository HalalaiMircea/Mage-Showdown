package com.mageshowdown.gameclient;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.AnimatedActorInterface;
import com.mageshowdown.gamelogic.DynamicGameActor;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.Weapon;
import com.mageshowdown.packets.Network.*;

public class ClientPlayerCharacter extends DynamicGameActor implements AnimatedActorInterface {

    private GameClient myClient=GameClient.getInstance();

    private Weapon myWeapon;

    private boolean moveLeft=false;
    private boolean moveRight=false;
    private boolean jump=false;
    private boolean isMyPlayer=false;
    private int health=15;
    private String userName;

    private Vector2 queuedPos;
    private Vector2 queuedVel;
    private boolean canClearPos=false;
    private boolean canClearVel=false;

    public ClientPlayerCharacter(Stage stage, Vector2 position, String userName) {
        super(stage,position, new Vector2(22,32),1.5f   );
        addAnimation(4,1,1.2f,"idle",ClientAssetLoader.idlePlayerSpriteSheet);
        addAnimation(2,1,.8f,"jumping",ClientAssetLoader.jumpingPlayerSpritesheet);
        addAnimation(8,1,1f,"running",ClientAssetLoader.runningPlayerSpritesheet);
        this.userName=userName;

        myWeapon=new Weapon(stage,true);

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
                    ShootProjectile sp=new ShootProjectile();
                    Vector2 shootingOrigin=new Vector2((myWeapon.getX()),(myWeapon.getY()+myWeapon.getHeight()/2));
                    float rotation= GameWorld.getMouseVectorAngle(shootingOrigin);
                    Vector2 direction=GameWorld.getNormalizedMouseVector(shootingOrigin);

                    sp.id=myClient.getID();
                    sp.rot=rotation;
                    sp.dir=direction;
                    myClient.sendTCP(sp);
                    myWeapon.shoot(direction,rotation,myClient.getID());
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

    public void setPosition(Vector2 position){
        setPosition(position.x,position.y);
    }

    @Override
    public void act(float delta) {
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

    @Override
    public void destroyActor() {
        myWeapon.destroyActor();
        super.destroyActor();
    }

    public Weapon getMyWeapon() {
        return myWeapon;
    }

    public void setMyPlayer(boolean myPlayer) {
        isMyPlayer = myPlayer;
    }


    public void damageBy(int damageValue){
        health-=damageValue;
    }


}
