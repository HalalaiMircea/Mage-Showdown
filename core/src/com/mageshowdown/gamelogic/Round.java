package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mageshowdown.gameclient.ClientAssetLoader;

public abstract class Round extends Actor {

    protected final float TIME_BETWEEN_ROUNDS=10f;
    protected final float ROUND_LENGTH=100f;

    protected boolean finished=false;
    protected boolean started=false;

    protected float timePassed;

    protected float timePassedRoundFinished;

    protected Round(){
        start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!finished && started)
            timePassed+= Gdx.graphics.getDeltaTime();

        if(timePassed>ROUND_LENGTH)
        {
            stop();
        }

        if(finished){
            timePassedRoundFinished-=Gdx.graphics.getDeltaTime();
            if(timePassedRoundFinished<=0){
                start();
            }
        }
    }

    public void start(){
        finished=false;
        started=true;
        timePassedRoundFinished=TIME_BETWEEN_ROUNDS;
    }

    public void stop(){
        finished=true;
        started=false;
        timePassed=0f;
    }



    public float getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(float timePassed) {
        this.timePassed = timePassed;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isStarted() {
        return started;
    }

    public float getTIME_BETWEEN_ROUNDS() {
        return TIME_BETWEEN_ROUNDS;
    }

    public float getROUND_LENGTH() {
        return ROUND_LENGTH;
    }
}
