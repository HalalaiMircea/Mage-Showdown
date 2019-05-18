package com.mageshowdown.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.MenuScreen;
import com.mageshowdown.packets.Network;
import com.mageshowdown.utils.PrefsKeys;

public class MageShowdownClient extends Game {

    private static final MageShowdownClient INSTANCE = new MageShowdownClient();

    private GameClient myClient = GameClient.getInstance();

    private MageShowdownClient() {

    }

    @Override
    public void create() {
        GameWorld.setResolutionScale(Gdx.graphics.getWidth() / 1280f);
        ClientAssetLoader.load();

        this.setScreen(MenuScreen.getInstance());
    }

    @Override
    public void render() {
        super.render();
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + " ");
    }

    @Override
    public void dispose() {
        ClientAssetLoader.dispose();
        //ClientAssetLoader.getInstance().manager.dispose();
    }

    public void clientStart(String ipAddress) {
        myClient.setUserName(ClientAssetLoader.prefs.getString(PrefsKeys.PLAYERNAME));
        myClient.start();

        myClient.connect(5000, ipAddress, Network.TCP_PORT, Network.UDP_PORT);
        myClient.addListener(new ClientListener());
    }

    public static MageShowdownClient getInstance() {
        return INSTANCE;
    }
}
