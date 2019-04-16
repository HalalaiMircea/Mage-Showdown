package com.mageshowdown.mygame.gamelogic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DynamicGameActor extends GameActor {
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

    public DynamicGameActor(Stage stage, Vector2 position, Vector2 size, float spriteScaling){
        super(stage,position,size, spriteScaling);
        velocity=new Vector2(0,0);
        horizontalState=HorizontalState.STANDING;
        verticalState=VerticalState.FLYING;
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
        if(body!=null)
            body.setLinearVelocity(new Vector2(velocity.x,velocity.y));
        super.act(delta);
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
}
