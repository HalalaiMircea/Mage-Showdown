package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Projectile extends DynamicGameActor {

    protected boolean collided=false;
    protected boolean outOfBounds=false;
    protected int id;
    protected int ownerId;

    public Projectile(Stage stage, Vector2 position, float rotation, Vector2 direction, int id, int ownerId){
        super(stage,position, new Vector2(46,31), ClientAssetLoader.laserShotTexture,.75f);
        createBody(BodyDef.BodyType.DynamicBody);

        //the id is used to identify the projectile when it needs to be destroyed
        this.id=id;
        //the owner id is the id of the player that shot the bullet
        this.ownerId=ownerId;

        //we dont want the projectile to react to any collisions or be affected by gravity so we make it a sensor
        body.getFixtureList().get(0).setSensor(true);
        body.setGravityScale(0f);
        sprite.setRotation(rotation);
        sprite.setOrigin(getWidth()/2,getHeight()/2);
        velocity.x=5f*direction.x;
        velocity.y=5f*direction.y;
        stage.addActor(this);
    }


    //if two projectiles have the same position then we know its the same projectile
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

        //every frame we check if the projectile is out of bounds
        Vector2 convPosition=GameWorld.convertWorldToPixels(body.getPosition());
        if(convPosition.x > Gdx.graphics.getWidth() || convPosition.x<0 || convPosition.y>Gdx.graphics.getHeight() || convPosition.y<0)
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

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return new Vector2(getX(),getY()).toString();
    }
}
