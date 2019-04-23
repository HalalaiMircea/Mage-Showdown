package com.mageshowdown.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;

public class MageShowdownServer extends Game {

    private GameServer myServer=GameServer.getInstance();

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

        if(myServer.getUpdatePositions()){
            new UpdatePlayerPositions(gameStage);
            timePassed=0f;
            myServer.setUpdatePositions(false);
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
        myServer.start();
        myServer.bind(Network.TCP_PORT, Network.UDP_PORT);

        myServer.addListener(new ServerListener(gameStage));
    }

}
