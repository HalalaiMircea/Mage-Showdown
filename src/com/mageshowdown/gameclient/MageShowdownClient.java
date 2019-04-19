package com.mageshowdown.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mageshowdown.gamelogic.GameScreen;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.MenuScreen;
import com.mageshowdown.packets.Network;

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
