package com.mageshowdown.mygame.gamelogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.mygame.gameclient.*;


import java.util.ArrayList;
import java.util.ListIterator;

public class Weapon extends GameActor implements AnimatedActorInterface{
    private ArrayList<Projectile> weaponShots;
    private boolean loadAnimation;

    public Weapon(Stage stage, boolean loadAnimation){
        super(stage,new Vector2(0,0),new Vector2(32,33));
        weaponShots=new ArrayList<Projectile>();
        this.loadAnimation=loadAnimation;

        if(loadAnimation)
            addAnimation(3,1,.5f,"idle",ClientAssetLoader.waterSphereSpriteSheet);
    }


    public void updatePosition(Vector2 position){
        Vector2 offset=new Vector2(-5f,35f);
        setPosition(position.x+offset.x,position.y+offset.y);
     }

    @Override
    public void act(float delta) {
        super.act(delta);

        destroyEliminatedProjectiles();

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
                GameWorld.bodiesToBeRemoved.add(x.getBody());
                x.remove();
                iter.remove();
            }
        }
    }

    public void projectileHasCollided(int projId){
        for(Projectile x:weaponShots){
            if(x.getId()==projId){
                x.hasCollided();
                break;
            }
        }
    }


    public void shoot(Vector2 direction, float rotation, int ownerId){
        Vector2 shootingOrigin=new Vector2((getX()),(getY()+getHeight()/2));
        weaponShots.add(new Projectile(getStage(),shootingOrigin,rotation,direction,weaponShots.size(),ownerId));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(currFrame!=null)
            batch.draw(currFrame,getX(),getY(),getWidth()*getScaleX(),getHeight()*getScaleY());
    }

    @Override
    public void pickFrame() {
        currFrame=animations.get("idle").getKeyFrame(passedTime,true);
    }

    public ArrayList<Vector2> getProjectileLocations(){
        ArrayList<Vector2> locations=new ArrayList<Vector2>();
        for(Projectile x:weaponShots){
            locations.add(x.getBody().getPosition());
        }
        return locations;
    }

    public ArrayList<Vector2> getProjectileVelocities() {
        ArrayList<Vector2> locations = new ArrayList<Vector2>();
        for (Projectile x : weaponShots) {
            locations.add(x.getBody().getLinearVelocity());
        }
        return locations;
    }
}
