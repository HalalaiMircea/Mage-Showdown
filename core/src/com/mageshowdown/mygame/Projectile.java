package com.mageshowdown.mygame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Projectile extends DynamicGameActor {

    boolean collided=false;
    boolean outOfBounds=false;

    public Projectile(Stage stage, Vector2 position, float rotation){
        super(stage,position, new Vector2(46,31),AssetLoader.laserShotTexture,.75f);
        createBody(BodyDef.BodyType.DynamicBody);
        Vector2 direction=GameWorld.getNormalizedMouseVector(position);

        //we dont want the projectile to react to any collisions or be affected by gravity so we make it a sensor
        body.getFixtureList().get(0).setSensor(true);
        body.setGravityScale(0f);
        sprite.setRotation(rotation);
        sprite.setOrigin(getWidth()/2,getHeight()/2);
        velocity.x=5f*direction.x;
        velocity.y=5f*direction.y;
        stage.addActor(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Projectile){
            if(((Projectile) obj).getX() == getX() && ((Projectile) obj).getY() == getY())
                return true;
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Vector2 convPosition=GameWorld.convertWorldToPixels(body.getPosition());
        if(convPosition.x >1280 || convPosition.x<0 || convPosition.y>720 || convPosition.y<0)
            outOfBounds=true;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public void setOutOfBounds(boolean outOfBounds){
        this.outOfBounds=outOfBounds;
    }

    public boolean hasCollided() {
        return collided;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

}
