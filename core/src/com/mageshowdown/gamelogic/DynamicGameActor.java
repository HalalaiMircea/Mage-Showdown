package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class DynamicGameActor extends GameActor {
    public enum VerticalState{
        GROUNDED("idle"),
        FLYING("jumping");

        private String name;

        VerticalState(String name){
            this.name=name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
    public enum HorizontalState{
        GOING_LEFT("running"),
        GOING_RIGHT("running"),
        STANDING("idle");

        private String name;

        HorizontalState(String name){
            this.name=name;
        }

        @Override
        public String toString() {
            return this.name;
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

    protected DynamicGameActor(Stage stage, Vector2 position, Vector2 size, Vector2 bodySize, float rotation, Vector2 sizeScaling){
        super(stage,position,size, bodySize, rotation, sizeScaling);
        velocity=new Vector2(0,0);
        horizontalState=HorizontalState.STANDING;
        verticalState=VerticalState.FLYING;
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
