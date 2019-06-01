package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Spell extends DynamicGameActor {
    private final float DAMAGE_VALUE;

    //when a spell collides we may want to have an impact animation so we use a second boolean besides collided to know if we can destroy it
    protected boolean collided = false;
    protected boolean destroyable=false;
    protected boolean outOfBounds = false;
    protected boolean expired = false;
    protected int id;
    protected int ownerId;

    protected Spell(Stage stage, Vector2 velocity, Vector2 position, Vector2 size, Vector2 sizeScaling, Vector2 bodySize, float rotation, int id, int ownerId, float damageValue) {
        super(stage, position, size, bodySize, rotation, sizeScaling);

        this.DAMAGE_VALUE = damageValue;
        this.id = id;
        this.ownerId = ownerId;

        this.velocity = velocity;
        stage.addActor(this);
    }

    //if two projectiles have the same position then we know its the same projectile
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Spell) {
            return ((Spell) obj).getX() == getX() && ((Spell) obj).getY() == getY();
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(body!=null && !body.getFixtureList().get(0).isSensor())
            makeBodySensor();

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

        //every frame we check if the projectile is out of bounds
        if (body != null) {
            Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
            if (convPosition.x > 1280 || convPosition.x < 0 || convPosition.y > 720 || convPosition.y < 0) {
                setOutOfBounds(true);
            }
        }
    }

    @Override
    protected void updatePositionFromBody() {
        Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
        setPosition(convPosition.x-getWidth()/2, convPosition.y-getHeight()/2);
        setRotation(body.getAngle() * 180 / (float) Math.PI);
    }

    //we dont want the projectile to change its angle or be affected by gravity
    protected void makeBodySensor() {
        body.setFixedRotation(true);
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

    public boolean canDestroy(){return destroyable;}

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
