package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;
import com.mageshowdown.gameclient.MageShowdownClient;

public class GameScreen implements Screen {

    enum GameState {
        GAME_RUNNING,
        GAME_PAUSED
    }

    private static MageShowdownClient game;
    private static ClientGameStage gameStage;      //gameplay, or character control stage
    private static Stage escMenuStage;          //menu overlay after pressing escape during gameplay
    private static GameState gameState;

    public GameScreen(final MageShowdownClient game) {
        GameScreen.game = game;
        gameStage = new ClientGameStage();
        prepareEscMenu();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
            case GAME_RUNNING:
                gameStage.act();
                gameStage.draw();
                gameRunningInput();
                break;
            case GAME_PAUSED:
                gameStage.act();
                escMenuStage.act();
                gameStage.draw();
                escMenuStage.draw();
                gamePausedInput();
                break;
        }

    }

    public static void start() {
        gameStage.start();
    }

    public static ClientGameStage getGameStage() {
        return gameStage;
    }

    //setter to access gameState member variable from MenuScreen class
    public static void setGameState(GameState gameState) {
        GameScreen.gameState = gameState;
    }

    @Override
    public void show() {

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
        gameStage.dispose();
        escMenuStage.dispose();
    }

    private static void gameRunningInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_RUNNING) {
            gameState = GameState.GAME_PAUSED;
            Gdx.input.setInputProcessor(escMenuStage);
        }
    }

    private static void gamePausedInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_PAUSED) {
            gameState = GameState.GAME_RUNNING;
            Gdx.input.setInputProcessor(gameStage);
        }
//        } else if (Gdx.input.isKeyPressed(Input.Keys.Q))
//            Gdx.app.exit();
    }

    private static void prepareEscMenu() {
        escMenuStage = new Stage(gameStage.getViewport(), gameStage.getBatch());

        Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.debug();
        Table background = new Table();
        background.setFillParent(true);

        TextButton resumeButton = new TextButton("Resume Game", ClientAssetLoader.interfaceSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.interfaceSkin);
        TextButton quitButton = new TextButton("Quit to Desktop", ClientAssetLoader.interfaceSkin);
        Image semiTL = new Image(ClientAssetLoader.solidBlack);
        semiTL.setColor(0, 0, 0, 0.8f);

        menuTable.add(resumeButton).padBottom(20);
        menuTable.row();
        menuTable.add(optionsButton).padBottom(20);
        menuTable.row();
        menuTable.add(quitButton);
        background.add(semiTL);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameState = GameState.GAME_RUNNING;
                Gdx.input.setInputProcessor(gameStage);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        escMenuStage.addActor(background);
        escMenuStage.addActor(menuTable);
    }
}
