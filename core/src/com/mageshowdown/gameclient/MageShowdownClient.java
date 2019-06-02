package com.mageshowdown.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.LoadingScreen;

public class MageShowdownClient extends Game {
    private static final MageShowdownClient INSTANCE = new MageShowdownClient();

    private MageShowdownClient() {
        GameWorld.world.setContactListener(new ClientCollisionManager());
    }

    @Override
    public void create() {
        GameWorld.updateResolutionScale();
        ClientAssetLoader.getInstance().load();

        this.setScreen(new LoadingScreen());
    }

    @Override
    public void render() {
        super.render();
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + " ");
    }

    @Override
    public void dispose() {
        super.dispose();
        ClientAssetLoader.getInstance().dispose();
    }

    public static MageShowdownClient getInstance() {
        return INSTANCE;
    }
}
