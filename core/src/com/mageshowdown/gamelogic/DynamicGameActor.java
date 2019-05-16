package com.mageshowdown.gamelogic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class DynamicGameActor extends GameActor {
    public static enum VerticalState{
        GROUNDED(1),
        FLYING(2);

        private int numValue;

        VerticalState(int numValue){
            this.numValue=numValue;
        }

        public int getNumValue(){
            return numValue;
        }

        public static VerticalState valueOf( int numValue){
            switch(numValue){
                case 1:
                    return GROUNDED;
                case 2:
                    return FLYING;
                default:
                    return GROUNDED;
            }
        }
    }
    public static enum HorizontalState{
        GOING_LEFT(1),
        GOING_RIGHT(2),
        STANDING(3);

        private int numValue;

        HorizontalState(int numValue){
            this.numValue=numValue;
        }
        public int getNumValue(){
            return numValue;
        }

        public static HorizontalState valueOf(int numValue){
            switch(numValue){
                case 1:
                    return GOING_LEFT;
                case 2:
                    return GOING_RIGHT;
                default:
                    return STANDING;

            }
        }
    }

    protected Vector2 velocity;
    protected VerticalState verticalState;
    protected HorizontalState horizontalState;

    /*
    * because the dynamic game actor also has velocity we'll want to change, we also queue that up
    */
    protected Vector2 queuedVel;
    protected boolean canClearVel=false;

    protected DynamicGameActor(Stage stage, Vector2 position, Vector2 size, float rotation, Vector2 sizeScaling){
        super(stage,position,size, rotation, sizeScaling);
        velocity=new Vector2(0,0);
        horizontalState=HorizontalState.STANDING;
        verticalState=VerticalState.FLYING;
    }

    protected DynamicGameActor(Stage stage,Vector2 position, Vector2 size, float rotation, Texture texture, Vector2 sizeScaling){
        super(stage,position,size,rotation, texture, sizeScaling);
        velocity=new Vector2(0,0);
        horizontalState=HorizontalState.STANDING;
    }

    @Override
    public void act(float delta) {
        updateGameActor(delta);

        if(body!=null && !GameWorld.world.isLocked()) {
            body.setLinearVelocity(new Vector2(velocity.x, velocity.y));
        }
    }

    @Override
    public void clearQueue() {
        super.clearQueue();
        if(canClearVel){
            body.setLinearVelocity(queuedVel);
            canClearVel=false;
        }
    }

    public void setQueuedVel(Vector2 queuedVel) {
        this.queuedVel = queuedVel;
        canClearVel=true;
    }

    public void setHorizontalState(HorizontalState horizontalState) {
        this.horizontalState = horizontalState;
    }

    public void setVerticalState(VerticalState verticalState) {
        this.verticalState = verticalState;
    }

    public HorizontalState getHorizontalState() {
        return horizontalState;
    }

    public VerticalState getVerticalState() {
        return verticalState;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void updateGameActor(float delta){
        super.act(delta);
    }
}
