package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;


import java.util.ArrayList;
import java.util.ListIterator;



public class Weapon extends GameActor implements AnimatedActorInterface{
    public static enum AmmoType{
        FREEZE_BULLETS,
        LASER
    }


    private AmmoType ammoType;
    private ArrayList<Ammo> ammunition;
    private boolean loadAnimation;

    /*
    * weapons will work like an energy shield; their "ammo" capacity will constantly regenerate
    * shooting the weapon interrupts that process, after which there is a small cooldown until the regeneration starts again
     */
    private final float COOLDOWN_TIME;
    private final float MAXIMUM_CAPACITY;
    private final float RECHARGE_RATE=10f;   //recharge rate per second

    private float currentCapacity;
    private float projectileCost;
    private float rechargeTimer=0f;
    private boolean recharge=false;

    public Weapon(Stage stage, boolean loadAnimation, AmmoType ammoType, float cd, int capacity, int projectileCost){
        super(stage,new Vector2(0,0),new Vector2(64,66));
        COOLDOWN_TIME=cd;
        MAXIMUM_CAPACITY=capacity;
        currentCapacity=MAXIMUM_CAPACITY;
        this.ammoType=ammoType;
        this.projectileCost=projectileCost;
        ammunition =new ArrayList<Ammo>();
        this.loadAnimation=loadAnimation;


        if(loadAnimation)
            switch(ammoType) {
                case FREEZE_BULLETS:
                    addAnimation(6, 1, 1.5f, "idle", ClientAssetLoader.crystalSpritesheet);
                    break;
                case LASER:
                    addAnimation(6,2,1.5f,"idle",ClientAssetLoader.sphereSpriteSheet);
                    break;
        }
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

        if(loadAnimation)
            pickFrame();
    }

    private void destroyEliminatedProjectiles(){
        /*
         * if it has collided or out of the screen remove projectile from the arraylist and the stage so theres no reference to it left
         */
        ListIterator<Ammo> iter= ammunition.listIterator();
        while(iter.hasNext()){
            Ammo x=iter.next();
            if(x.isOutOfBounds() || x.hasCollided() || x.isExpired())
            {
                x.destroyActor();
                iter.remove();
            }
        }
    }

    public void projectileHasCollided(int projId){
        for(Ammo x: ammunition){
            if(x.getId()==projId){
                x.setCollided(true);
                break;
            }
        }
    }


    public void shoot(Vector2 direction, float rotation, int ownerId){
        if(currentCapacity>projectileCost){
            currentCapacity-=projectileCost;
            Vector2 shootingOrigin=new Vector2((getX()+getWidth()/2),(getY()+getHeight()/2));
            switch(ammoType) {
                case FREEZE_BULLETS:
                    ammunition.add(new FreezeProjectile(getStage(), shootingOrigin, rotation, direction, ammunition.size(), ownerId));
                    break;
                case LASER:
                    ammunition.add(new Laser(getStage(), shootingOrigin, rotation, ammunition.size(), ownerId));
                    break;
            }
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
        for(Ammo x: ammunition){
            x.clearQueue();
        }
    }

}
