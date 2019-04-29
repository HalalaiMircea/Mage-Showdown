package com.mageshowdown.gameserver;

import com.badlogic.gdx.Gdx;
import com.mageshowdown.gamelogic.GameScreen;
import com.mageshowdown.gamelogic.Round;

import java.util.Random;

public class ServerRound extends Round {

    private static ServerRound instance=new ServerRound(5f);


    private float mapChangeInterval;
    private int mapsChanged=0;

    public ServerRound(float mapChangeInterval){
        super();
        this.mapChangeInterval=mapChangeInterval;
    }

    @Override
    public void act(float delta){
        super.act(delta);


        if(timePassed-mapChangeInterval*mapsChanged>mapChangeInterval){
            GameServer.getInstance().sendMapChange(GameServer.getInstance().getARandomMap());
            mapsChanged++;
        }
    }

    @Override
    public void stop() {
        super.stop();
        mapsChanged=0;
        GameServer.getInstance().registerRound();
    }

    @Override
    public void start() {
        super.start();
        GameServer.getInstance().startRound();
    }

    public static ServerRound getInstance() {
        return instance;
    }
}
