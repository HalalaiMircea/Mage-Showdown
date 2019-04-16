package com.mageshowdown.mygame.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mageshowdown.mygame.gameclient.ClientAssetLoader;
import com.mageshowdown.mygame.gameclient.MageShowdownClient;
import com.mageshowdown.mygame.packets.Network;

import java.io.IOException;


public class MainMenuScreen implements Screen {

    private final MageShowdownClient game;
    private Stage mainMenuStage;
    //private final GameScreen gameScreen;

    public MainMenuScreen(final MageShowdownClient game, final GameScreen gameScreen) {
        this.game = game;
        //this.gameScreen = gameScreen;
        mainMenuStage = new Stage();
        Gdx.input.setInputProcessor(mainMenuStage);

        Table foreground = new Table();
        Table background = new Table();

        background.setFillParent(true);
        foreground.setFillParent(true);
        foreground.debug();

        TextButton connectButton = new TextButton("Connect to:", ClientAssetLoader.interfaceSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.interfaceSkin);
        TextButton quitButton = new TextButton("Quit to Desktop", ClientAssetLoader.interfaceSkin);
        final TextField addressField = new TextField("", ClientAssetLoader.interfaceSkin);

        background.add(new Image(ClientAssetLoader.menuBackground));

        //Order sensitive addition of widgets into table
        foreground.add(connectButton).padBottom(20);
        foreground.add(addressField);
        foreground.row();
        foreground.add(optionsButton).padBottom(20);
        foreground.row();
        foreground.add(quitButton);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                game.setScreen(gameScreen);
                gameScreen.start();
                gameScreen.setGameState(GameScreen.GameState.GAME_RUNNING);

                String ipAddress = addressField.getText();
                game.clientStart(ipAddress);
            }
        });

        mainMenuStage.addActor(background);
        mainMenuStage.addActor(foreground);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainMenuStage.act(delta);
        mainMenuStage.draw();
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
        mainMenuStage.dispose();
    }
}
