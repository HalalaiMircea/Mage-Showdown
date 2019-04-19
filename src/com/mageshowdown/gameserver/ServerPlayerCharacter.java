package com.mageshowdown.gameserver;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.DynamicGameActor;
import com.mageshowdown.gamelogic.Weapon;


import java.util.ArrayList;

public class ServerPlayerCharacter extends DynamicGameActor {

    private Weapon myWeapon;
    private int moveDirection=-1;
    private int id;
    private int health=15;

    public ServerPlayerCharacter(Stage stage, Vector2 pos, int id) {
        super(stage,pos, new Vector2(22,32),1.5f);

        this.id=id;

        createBody(BodyDef.BodyType.DynamicBody);
        body.setFixedRotation(true);
        myWeapon=new Weapon(stage,false);

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
        if(body!=null){
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
        }
        if(myWeapon!=null)
            myWeapon.updatePosition(new Vector2(getX(),getY()));
        super.act(delta);
        moveDirection=-1;
    }

    public Weapon getMyWeapon() {
        return myWeapon;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    public ArrayList<Vector2> getProjectileLocations(){
        return myWeapon.getProjectileLocations();
    }

    public ArrayList<Vector2> getProjectileVel(){
        return myWeapon.getProjectileVelocities();
    }

    public int getId() {
        return id;
    }

    public void damageBy(int damageValue){
        health-=damageValue;
    }
}
