package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public abstract class Ammo extends DynamicGameActor {

    protected boolean collided=false;
    protected boolean outOfBounds=false;
    protected boolean expired=false;
    protected int id;
    protected int ownerId;

    private float damageValue;

    protected Ammo(Stage stage, Vector2 velocity, Vector2 position, Vector2 size, Texture texture, float spriteScaling, float rotation, int id, int ownerId, float damageValue){
        super(stage,position, size, rotation, texture, spriteScaling);
        createBody(rotation,BodyDef.BodyType.DynamicBody);

        this.damageValue=damageValue;
        //the id is used to identify the projectile when it needs to be destroyed
        this.id=id;
        //the owner id is the id of the player that shot the bullet
        this.ownerId=ownerId;

        //we dont want the projectile to react to any collisions or be affected by gravity so we make it a sensor
        body.getFixtureList().get(0).setSensor(true);
        body.setGravityScale(0f);
        this.velocity=velocity;
        stage.addActor(this);
    }

    protected Ammo(Stage stage, Vector2 velocity, Vector2 position, Vector2 size, float spriteScaling, float rotation, int id, int ownerId, float damageValue){
        super(stage,position, size, rotation,spriteScaling);
        createBody(rotation,BodyDef.BodyType.DynamicBody);
        System.out.println(size);
        this.damageValue=damageValue;
        this.id=id;
        this.ownerId=ownerId;

        body.getFixtureList().get(0).setSensor(true);
        body.setGravityScale(0f);
        this.velocity=velocity;
        stage.addActor(this);
    }

    //if two projectiles have the same position then we know its the same projectile
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ammo){
            return ((Ammo) obj).getX() == getX() && ((Ammo) obj).getY() == getY();
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //System.out.println(GameWorld.convertWorldToPixels(body.getPosition())+" "+body.getFixtureList().get(0).getShape().getRadius());
        //every frame we check if the projectile is out of bounds
        if(getRotation()>80f)
        {
            if(sprite!=null)
                sprite.setFlip(false,true);
            if(currFrame!=null)
                if(!currFrame.isFlipY()){
                    currFrame.flip(false,true);
                }
        }else{
            if(sprite!=null)
                sprite.setFlip(false,false);
            if(currFrame!=null)
                if(currFrame.isFlipY())
                    currFrame.flip(false,false);
        }
        System.out.println(getRotation());

        if(body!=null){
            Vector2 convPosition=GameWorld.convertWorldToPixels(body.getPosition());
            if(convPosition.x > 1280 || convPosition.x<0 || convPosition.y>720 || convPosition.y<0)
            {
                outOfBounds=true;
            }
        }

        //System.out.println("Body at: "+GameWorld.convertWorldToPixels(body.getPosition()) +" rotation:"+(body.getAngle() * 180 / (float) Math.PI)+"and actor at:"+this.getX()+" "+this.getY()+" rotation: "+getRotation());
    }


    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public void setOutOfBounds(boolean outOfBounds){
        this.outOfBounds=outOfBounds;
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

    public float getDamageValue() {
        return damageValue;
    }

    @Override
    public String toString() {
        return new Vector2(getX(),getY()).toString();
    }
}
