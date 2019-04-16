package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.mygame.gamelogic.*;


import java.util.ArrayList;

public class ServerPlayerCharacter extends DynamicGameActor{

    private Weapon myWeapon;
    private int moveDirection=-1;

    public ServerPlayerCharacter(Stage stage, Vector2 pos) {
        super(stage,pos, new Vector2(22,32),1.5f);

        createBody(BodyDef.BodyType.DynamicBody);
        body.setFixedRotation(true);
        //myWeapon=new Weapon(stage);

    }

    @Override
    public void act(float delta) {
        switch (moveDirection){
            case Input.Keys.A:
                velocity.x=-2.5f;
                break;
            case Input.Keys.D:
                velocity.x=2.5f;
                break;
            case Input.Keys.W:
                if(body.getLinearVelocity().y<0.01f && body.getLinearVelocity().y>-0.01f)
                    body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x,.22f),body.getPosition(),false);
                break;
            default:
                /*
                * if the value isnt one of the keys that makes an action,
                * the body wont be moving horizontally
                 */
                velocity.x=0;
                break;
        }

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
//        myWeapon.updatePosition(new Vector2(getX(),getY()));
        super.act(delta);
        moveDirection=-1;
    }


    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

}
