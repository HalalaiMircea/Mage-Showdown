package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.gameclient.ClientAssetLoader;
import com.mageshowdown.mygame.gamelogic.*;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.packets.Network;

import java.io.IOException;
import java.util.ArrayList;

public class MageShowdownServer extends Game {
    private ServerGameStage gameStage;
    private boolean updatePositions=false;
    private float timePassed=0f;

    @Override
    public void create () {
        GameWorld.resolutionScale=1f;
        gameStage=new ServerGameStage();
        serverStart();
        ServerAssetLoader.load();
        ClientAssetLoader.load();
        gameStage.start();
    }

    @Override
    public void render () {
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond()+" ");
        gameStage.act();

        if(GameWorld.myServer.getUpdatePositions()){
            new UpdatePlayerPositions(gameStage);
            timePassed=0f;
            GameWorld.myServer.setUpdatePositions(false);
        }else{
            if(timePassed>=0.01f){
                new UpdatePlayerPositions(gameStage);
                timePassed=0f;
            }else{
                timePassed+=Gdx.graphics.getDeltaTime();
            }
        }

        GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);

        GameWorld.clearBodyRemovalQueue();
    }

    @Override
    public void dispose () {
        ServerAssetLoader.dispose();
    }

    public void serverStart(){
        GameWorld.myServer.start();
        GameWorld.myServer.bind(Network.TCP_PORT, Network.UDP_PORT);

        GameWorld.myServer.addListener(new ServerListener(gameStage));
    }

}
