package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.ListIterator;

public class Weapon extends GameActor implements AnimatedActorInterface{
    private ArrayList<Projectile> weaponShots;

    public Weapon(Stage stage){
        super(stage,new Vector2(0,0),new Vector2(32,33));
        weaponShots=new ArrayList<Projectile>();

        addAnimation(3,1,.5f,"idle",AssetLoader.waterSphereSpriteSheet);

    }

    public void updatePosition(Vector2 position){
        Vector2 offset=new Vector2(-5f,35f);
        setPosition(position.x+offset.x,position.y+offset.y);
     }

    @Override
    public void act(float delta) {
        super.act(delta);

        /*
        * if it has collided or out of the screen remove projectile from the arraylist and the stage so theres no reference to it left
        */
        ListIterator<Projectile> iter=weaponShots.listIterator();
        while(iter.hasNext()){
            Projectile x=iter.next();
            if(x.isOutOfBounds() || x.hasCollided())
            {
                x.remove();
                iter.remove();
            }
        }

        pickFrame();
    }

    public void shoot(){
        Vector2 shootingOrigin=new Vector2((getX()),(getY()+getHeight()/2));
        float rotation=GameWorld.getMouseVectorAngle(shootingOrigin);
        weaponShots.add(new Projectile(getStage(),shootingOrigin,rotation));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currFrame,getX(),getY(),getWidth()*getScaleX(),getHeight()*getScaleY());
    }

    @Override
    public void pickFrame() {
        currFrame=animations.get("idle").getKeyFrame(passedTime,true);
    }
}
