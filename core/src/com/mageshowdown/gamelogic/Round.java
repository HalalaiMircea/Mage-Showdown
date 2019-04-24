package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Round extends Actor {

    private boolean finished=false;
    private boolean started=false;

    private float timePassed;
    private float roundLength;

    private BitmapFont font;

    public Round(float roundLength, boolean assignFont){
        timePassed=0f;
        this.roundLength=roundLength;
        if(true) {
            font=ClientAssetLoader.font1;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!finished && started)
            timePassed+= Gdx.graphics.getDeltaTime();

        if(timePassed>roundLength)
        {
            finished=true;
            started=false;
        }
    }

    public void start(){
        finished=false;
        started=true;
    }

    public void stop(){
        finished=true;
        started=false;
        timePassed=0f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch,Float.toString(timePassed),Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
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
}
