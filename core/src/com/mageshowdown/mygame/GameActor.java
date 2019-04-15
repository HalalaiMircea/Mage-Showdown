package com.mageshowdown.mygame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import java.util.HashMap;

public class GameActor extends Actor {

    protected Sprite sprite;
    protected Body body;
    protected HashMap<String, Animation<TextureRegion>> animations;
    protected float passedTime=0f;
    protected TextureRegion currFrame;

    public GameActor(Stage stage, Vector2 position, Vector2 size, float spriteScaling){
        setScale(spriteScaling);
        setPosition(position.x,position.y);
        setSize(size.x,size.y);
        setOrigin(getWidth()*getScaleX()/2f,getHeight()*getScaleY()/2f);

        stage.addActor(this);

        animations=new HashMap<String, Animation<TextureRegion>>();
    }
    public GameActor(Stage stage,Vector2 position, Vector2 size){
        this(stage,position,size,1f);
    }

    public GameActor(Stage stage,Vector2 position, Vector2 size, Texture texture, float spriteScaling){
        this(stage,position, size,spriteScaling+(size.x/texture.getWidth()-1));

        sprite=new Sprite(texture);
        sprite.setPosition(getX(),getY());
        sprite.setScale(spriteScaling);
        sprite.setSize(getWidth(),getHeight());
        sprite.setOrigin(getOriginX(),getOriginY());
    }

    public GameActor(Stage stage,Vector2 position, Vector2 size, Texture texture){
        this(stage,position, size,texture,1f);
    }

    @Override
    public void act(float delta) {
        passedTime+=delta;

        if(body!=null) {
            Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
            setPosition(convPosition.x - getWidth() / 2, convPosition.y - getHeight() / 2);
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


    protected void createBody(float density, float friction, float restitution, BodyDef.BodyType bodyType){
        Vector2 bodySize=new Vector2(getWidth(),
                getHeight());

        body=CreateBodies.createRectangleBody(new Vector2(getX(),getY()), bodySize,bodyType,density,friction,restitution);
        setTouchable(Touchable.enabled);

        //we set the body's user data to the current object in order to retrieve it later for collision handling
        body.setUserData(this);
    }

    protected void createBody(BodyDef.BodyType bodyType){
        createBody(.6f,0f,0f,bodyType);
    }


}
