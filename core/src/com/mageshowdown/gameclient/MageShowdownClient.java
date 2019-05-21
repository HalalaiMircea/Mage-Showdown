package com.mageshowdown.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.MenuScreen;

public class MageShowdownClient extends Game {
    private static final MageShowdownClient INSTANCE = new MageShowdownClient();

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
    }

    public static MageShowdownClient getInstance() {
        return INSTANCE;
    }
}
