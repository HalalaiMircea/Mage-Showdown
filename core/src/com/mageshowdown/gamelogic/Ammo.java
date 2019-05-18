package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Ammo extends DynamicGameActor {

    protected boolean collided = false;
    protected boolean outOfBounds = false;
    protected boolean expired = false;
    protected int id;
    protected int ownerId;

    private final float DAMAGE_VALUE;

    protected Ammo(Stage stage, Vector2 velocity, Vector2 position, Vector2 size, Texture texture, Vector2 sizeScaling, float rotation, int id, int ownerId, float damageValue) {
        super(stage, position, size, rotation, texture, sizeScaling);

        this.DAMAGE_VALUE = damageValue;
        //the id is used to identify the projectile when it needs to be destroyed
        this.id = id;
        //the owner id is the id of the player that shot the bullet
        this.ownerId = ownerId;
        this.velocity = velocity;
        stage.addActor(this);
    }

    protected Ammo(Stage stage, Vector2 velocity, Vector2 position, Vector2 size, Vector2 sizeScaling, float rotation, int id, int ownerId, float damageValue) {
        super(stage, position, size, rotation, sizeScaling);

        this.DAMAGE_VALUE = damageValue;
        this.id = id;
        this.ownerId = ownerId;

        this.velocity = velocity;
        stage.addActor(this);
    }

    //if two projectiles have the same position then we know its the same projectile
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ammo) {
            return ((Ammo) obj).getX() == getX() && ((Ammo) obj).getY() == getY();
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(body!=null && !body.getFixtureList().get(0).isSensor())
            makeBodySensor();

        //every frame we check if the projectile is out of bounds
        if (getRotation() > 80f) {
            if (sprite != null)
                sprite.setFlip(false, true);
            if (currFrame != null)
                if (!currFrame.isFlipY()) {
                    currFrame.flip(false, true);
                }
        } else {
            if (sprite != null)
                sprite.setFlip(false, false);
            if (currFrame != null)
                if (currFrame.isFlipY())
                    currFrame.flip(false, false);
        }

        if (body != null) {
            Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
            if (convPosition.x > 1280 || convPosition.x < 0 || convPosition.y > 720 || convPosition.y < 0) {
                setOutOfBounds(true);
            }
        }
    }


    //we dont want the projectile to react to any collisions or be affected by gravity so we make it a sensor
    protected void makeBodySensor() {
        body.getFixtureList().get(0).setSensor(true);
        body.setGravityScale(0f);
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public void setOutOfBounds(boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }


    public boolean hasCollided() {
        return collided;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public boolean isExpired() {
        return expired;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public float getDAMAGE_VALUE() {
        return DAMAGE_VALUE;
    }

    @Override
    public String toString() {
        return new Vector2(getX(), getY()).toString();
    }
}
