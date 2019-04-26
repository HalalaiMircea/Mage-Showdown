package com.mageshowdown.gameserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.DynamicGameActor;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.PlayerCharacter;
import com.mageshowdown.gamelogic.Weapon;


import java.util.ArrayList;

public class ServerPlayerCharacter extends PlayerCharacter {

    private int moveDirection=-1;
    private int id;

    private float dmgImmuneTimer=0f;
    private final float dmgImmuneMaxTime=.80f;


    public ServerPlayerCharacter(Stage stage, Vector2 pos, int id) {
        super(stage,pos,false);
        this.id=id;
    }

    @Override
    public void act(float delta) {
        if(dmgImmune)
        {
            dmgImmuneTimer+= Gdx.graphics.getDeltaTime();
            if(dmgImmuneTimer>dmgImmuneMaxTime){
                dmgImmune=false;
                dmgImmuneTimer=0f;
            }
        }


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
            if(body.getLinearVelocity().y!=0){
                velocity.y=body.getLinearVelocity().y;
            }
        }

        if(myWeapon!=null)
            myWeapon.updatePosition(new Vector2(getX(),getY()));
        super.act(delta);
        moveDirection=-1;
    }

    public void respawn(Vector2 respawnPos){
        setQueuedPos(respawnPos);
        health=15;
    }

    public void raiseScore(int value){
        score+=value;
    }

    public void damageBy(int damageValue) {
        if (!dmgImmune) {
            dmgImmune = true;
            health -= damageValue;
        }
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    public int getId() {
        return id;
    }

}
