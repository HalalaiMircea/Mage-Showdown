package com.mageshowdown.mygame;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class Weapon extends GameActor implements AnimatedActorInterface{


    ArrayList<Projectile> weaponShots;

    public Weapon(Stage stage){
        super(stage,new Vector2(0,0),new Vector2(32,33));
        weaponShots=new ArrayList<Projectile>();

        addAnimation(3,1,.5f,"idle",AssetLoader.waterSphereSpriteSheet);

    }

    public void updatePosition(Vector2 position){
        Vector2 offset=new Vector2(-5f*GameWorld.resolutionScale,35f*GameWorld.resolutionScale);
        setPosition(position.x+offset.x,position.y+offset.y);
     }

    @Override
    public void act(float delta) {
        super.act(delta);

        for(Projectile x:weaponShots){
            if(x.hasCollided() || x.isOutOfBounds()){
                //remove from stage so theres no reference to the projectile left
                x.remove();
                weaponShots.remove(x);
                break;
            }
        }
        pickFrame();
    }

    void shoot(){
        Vector2 shootingOrigin=new Vector2((getX())/GameWorld.resolutionScale,(getY()+getHeight()/2)/GameWorld.resolutionScale);
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
