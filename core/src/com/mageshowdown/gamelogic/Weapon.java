package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;


import java.util.ArrayList;
import java.util.ListIterator;

public class Weapon extends GameActor implements AnimatedActorInterface{
    private ArrayList<Projectile> weaponShots;
    private boolean loadAnimation;

    /*
    * weapons will work like an energy shield; their "ammo" capacity will constantly regenerate
    * shooting the weapon interrupts that process, after which there is a small cooldown until the regeneration starts again
     */
    private final float COOLDOWN_TIME;
    private final float MAXIMUM_CAPACITY;
    private final float RECHARGE_RATE=5f;   //recharge rate per second

    private float currentCapacity;
    private float projectileCost;
    private float rechargeTimer=0f;
    private boolean recharge=false;

    public Weapon(Stage stage, boolean loadAnimation, float cd, int capacity, int projectileCost){
        super(stage,new Vector2(0,0),new Vector2(64,66));
        COOLDOWN_TIME=cd;
        MAXIMUM_CAPACITY=capacity;
        currentCapacity=MAXIMUM_CAPACITY;
        this.projectileCost=projectileCost;
        weaponShots=new ArrayList<Projectile>();
        this.loadAnimation=loadAnimation;


        if(loadAnimation)
            addAnimation(6,1,.5f,"idle", ClientAssetLoader.crystalSpritesheet);
    }


    public void updatePosition(Vector2 position){
        Vector2 offset=new Vector2(-15f,35f);
        setPosition(position.x+offset.x,position.y+offset.y);
     }

    @Override
    public void act(float delta) {
        super.act(delta);
        destroyEliminatedProjectiles();
        rechargeWeapon();
        System.out.println(currentCapacity);

        if(loadAnimation)
            pickFrame();
    }

    private void destroyEliminatedProjectiles(){
        /*
         * if it has collided or out of the screen remove projectile from the arraylist and the stage so theres no reference to it left
         */
        ListIterator<Projectile> iter=weaponShots.listIterator();
        while(iter.hasNext()){
            Projectile x=iter.next();
            if(x.isOutOfBounds() || x.hasCollided())
            {
                x.destroyActor();
                iter.remove();
            }
        }
    }

    public void projectileHasCollided(int projId){
        for(Projectile x:weaponShots){
            if(x.getId()==projId){
                x.setCollided(true);
                break;
            }
        }
    }


    public void shoot(Vector2 direction, float rotation, int ownerId){
        if(currentCapacity>projectileCost){
            currentCapacity-=projectileCost;
            Vector2 shootingOrigin=new Vector2((getX()+getWidth()/8),(getY()+getHeight()/2));
            weaponShots.add(new Projectile(getStage(),shootingOrigin,rotation,direction,weaponShots.size(),ownerId));
            recharge=false;
        }
    }

    private void rechargeWeapon(){
        if(recharge){
            if(currentCapacity<MAXIMUM_CAPACITY){
                currentCapacity+= RECHARGE_RATE*Gdx.graphics.getDeltaTime();
            }
        }else{
            rechargeTimer+=Gdx.graphics.getDeltaTime();
            if(rechargeTimer>=COOLDOWN_TIME){
                recharge=true;
                rechargeTimer=0f;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(currFrame!=null)
            batch.draw(currFrame,getX(),getY(),getWidth()*getScaleX(),getHeight()*getScaleY());
    }

    @Override
    public void pickFrame() {
        if(animations.get("idle")!=null)
            currFrame=animations.get("idle").getKeyFrame(passedTime,true);
    }

    @Override
    public void clearQueue() {
        for(Projectile x:weaponShots){
            x.clearQueue();
        }
    }

}
