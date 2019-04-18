package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mageshowdown.mygame.packets.Network;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.gamelogic.*;


import java.io.IOException;
import java.util.ArrayList;

public class MageShowdownClient extends Game {
    private GameScreen gameScreen;
    private MenuScreen menuScreen;


    @Override
    public void create() {
        GameWorld.setResolutionScale(Gdx.graphics.getWidth() / 1280f);
        ClientAssetLoader.load();

        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this, gameScreen);
        this.setScreen(menuScreen);


    }

    @Override
    public void render() {
        super.render();

        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + " ");
    }

    @Override
    public void dispose() {
        ClientAssetLoader.dispose();
        gameScreen.dispose();
        menuScreen.dispose();
    }


    public void clientStart(String ipAddress) {
        GameWorld.myClient.start();

        GameWorld.myClient.connect(5000, ipAddress, Network.TCP_PORT, Network.UDP_PORT);

        GameWorld.myClient.addListener(new ClientListener(gameScreen));
    }


}
