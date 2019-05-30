package com.mageshowdown.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mageshowdown.gamelogic.CollisionListener;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.MenuScreen;

public class MageShowdownClient extends Game {
    private static final MageShowdownClient INSTANCE = new MageShowdownClient();

    private MageShowdownClient() {
        GameWorld.world.setContactListener(new ClientCollisionListener());
    }

    @Override
    public void create() {
        GameWorld.updateResolutionScale();
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
