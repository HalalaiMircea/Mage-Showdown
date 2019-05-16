package com.mageshowdown.gamelogic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.*;
import com.mageshowdown.gameclient.ClientRound;
import com.mageshowdown.gameserver.ServerRound;

import java.util.HashMap;

public abstract class GameActor extends Actor {

    protected Sprite sprite;
    protected Body body;
    protected HashMap<String, Animation<TextureRegion>> animations;
    protected float passedTime=0f;
    protected TextureRegion currFrame;

    /*
     * declaring the variable where im holding the position of the actor
     * after the world steps, we update its body with these values
     */
    protected Vector2 queuedPos;
    protected boolean canClearPos=false;


    protected GameActor(Stage stage, Vector2 position, Vector2 size, float rotation, Vector2 sizeScaling){
        setScale(sizeScaling.x,sizeScaling.y);
        setPosition(position.x,position.y);
        setSize(size.x,size.y);
        setOrigin(0,0);
        setRotation(rotation);
        stage.addActor(this);
        //debug();
        animations=new HashMap<String, Animation<TextureRegion>>();
    }
    protected GameActor(Stage stage,Vector2 position, Vector2 size, float rotation){
        this(stage,position,size,rotation,new Vector2(1,1));
    }

    protected GameActor(Stage stage,Vector2 position, Vector2 size, float rotation, Texture texture, Vector2 sizeScaling){
        this(stage,position, size,rotation,new Vector2(sizeScaling.x+(size.x/texture.getWidth()-1),sizeScaling.y+(size.y/texture.getHeight()-1)));

        sprite=new Sprite(texture);
        sprite.setPosition(getX(),getY());
        sprite.setScale(sizeScaling.x,sizeScaling.y);
        sprite.setSize(getWidth(),getHeight());
        sprite.setOrigin(getOriginX(),getOriginY());
        sprite.setRotation(getRotation());
    }


    @Override
    public void act(float delta) {
        passedTime+=delta;

        if(body!=null) {
            Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
            setPosition(convPosition.x, convPosition.y);
            setRotation(body.getAngle() * 180 / (float) Math.PI);
        }
        if(sprite!=null){
            sprite.setPosition(getX(), getY());
        }

        super.act(delta);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(sprite!=null)
            batch.draw(sprite,getX(),getY(),sprite.getOriginX(),sprite.getOriginY(),getWidth(),getHeight(),1f,1f,sprite.getRotation());
    }

    protected void addAnimation(int frameColumns, int frameRows, float animationDuration,String animationName, Texture spriteSheet){

       /*
       * split the spritesheet by number of columns and rows into a TextureRegion matrix,
       * put those frames into an ArrayList and then create the animation from said ArrayList
       */
        TextureRegion[][] tmp=TextureRegion.split(spriteSheet,
                spriteSheet.getWidth()/frameColumns,
                spriteSheet.getHeight()/frameRows);
        TextureRegion[] animationFrames=new TextureRegion[frameColumns*frameRows];

        int index=0;
        for(int i=0;i<frameRows;i++)
            for(int j=0;j<frameColumns;j++)
                animationFrames[index++]=tmp[i][j];

        animations.put(animationName,
                new Animation<TextureRegion>(animationDuration/(float)(frameColumns*frameRows), animationFrames));
    }


    protected void createBody(float density, float friction, float restitution, float rotation, BodyDef.BodyType bodyType){
        Vector2 bodySize=new Vector2(getWidth()*getScaleX(),
                getHeight()*getScaleY());

        body=CreateBodies.createRectangleBody(new Vector2(getX(),getY()), bodySize,bodyType,density,friction,restitution,rotation);
        setTouchable(Touchable.enabled);

        //we set the body's user data to the current object in order to retrieve it later for collision handling
        body.setUserData(this);
    }

    public void clearQueue(){
        if(canClearPos){
            body.setTransform(queuedPos,body.getAngle());
            canClearPos=false;
        }
    }

    public void setQueuedPos(Vector2 queuedPos) {
        this.queuedPos = queuedPos;
        canClearPos=true;
    }

    protected void createBody(float rotation,BodyDef.BodyType bodyType){
        createBody(.6f,0f,0f,rotation,bodyType);
    }

    protected void createBody(BodyDef.BodyType bodyType){
        createBody(.6f,0f,0f,0f,bodyType);
    }

    public Body getBody() {
        return body;
    }

    public void destroyActor(){
        if(body!=null)
            GameWorld.bodiesToBeRemoved.add(body);
        remove();
    }
}
