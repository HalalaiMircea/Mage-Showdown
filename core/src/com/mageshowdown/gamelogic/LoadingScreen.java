package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Screen;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;

public class LoadingScreen implements Screen {
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (ClientAssetLoader.getInstance().manager.update()) {
            ClientAssetLoader.getInstance().setAssets();
            MageShowdownClient.getInstance().setScreen(MenuScreen.getInstance());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
