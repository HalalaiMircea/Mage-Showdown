package com.mageshowdown.mygame.gameclient;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DynamicGameActor extends GameActor {

    protected enum VerticalState{
        GROUNDED,
        FLYING
    }
    protected enum HorizontalState{
        GOING_LEFT,
        GOING_RIGHT,
        STANDING
    }

    protected Vector2 velocity;
    protected VerticalState verticalState;
    protected HorizontalState horizontalState;

    public DynamicGameActor(Stage stage, Vector2 position, Vector2 size, float spriteScaling){
        super(stage,position,size, spriteScaling);
        velocity=new Vector2(0,0);
        horizontalState=HorizontalState.STANDING;
    }

    public DynamicGameActor(Stage stage,Vector2 position, Vector2 size){
        this(stage,position,size,1f);
    }

    public DynamicGameActor(Stage stage,Vector2 position, Vector2 size, Texture texture, float spriteScaling){
        super(stage,position,size,texture, spriteScaling);
        velocity=new Vector2(0,0);
        horizontalState=HorizontalState.STANDING;
    }

    public DynamicGameActor(Stage stage,Vector2 position, Vector2 size, Texture texture){
        this(stage,position,size,texture,1f);
    }

    @Override
    public void act(float delta) {
        body.setLinearVelocity(new Vector2(velocity.x,velocity.y));
        super.act(delta);
    }

}
