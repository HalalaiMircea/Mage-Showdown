package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;
import com.mageshowdown.gameclient.MageShowdownClient;

public class GameScreen implements Screen {

    enum GameState {
        GAME_RUNNING,
        GAME_PAUSED,
        GAME_OPTIONS
    }

    //Singleton omegalul
    private static final GameScreen INSTANCE = new GameScreen();

    private static ClientGameStage gameStage;      //gameplay, or character control stage
    private static OptionsStage gameOptionsStage;
    private static Stage escMenuStage;          //menu overlay after pressing escape during gameplay
    private static GameState gameState;

    private GameScreen() {
        gameStage = new ClientGameStage();
        escMenuStage = new Stage(gameStage.getViewport(), gameStage.getBatch());
        gameOptionsStage = new OptionsStage(gameStage.getViewport(), gameStage.getBatch(),
                ClientAssetLoader.solidBlack);

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
            case GAME_OPTIONS:
                gameStage.act();
                gameOptionsStage.act();
                gameStage.draw();
                gameOptionsStage.draw();
                break;
        }

    }

    public static GameScreen getInstance() {
        return INSTANCE;
    }

    public static ClientGameStage getGameStage() {
        return gameStage;
    }

    public static Stage getEscMenuStage() {
        return escMenuStage;
    }

    //setter to access gameState member variable from MenuScreen class
    public static void setGameState(GameState gameState) {
        GameScreen.gameState = gameState;
    }

    public static void start() {
        gameStage.start();
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
        gameOptionsStage.dispose();
        INSTANCE.dispose();
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
    }

    private static void prepareEscMenu() {
        Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.debug();
        Table background = new Table();
        background.setFillParent(true);

        TextButton resumeButton = new TextButton("Resume Game", ClientAssetLoader.uiSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.uiSkin);
        TextButton quitButton = new TextButton("Quit to Desktop", ClientAssetLoader.uiSkin);
        TextButton disconnectButton = new TextButton("Disconnect", ClientAssetLoader.uiSkin);
        Image semiTL = new Image(ClientAssetLoader.solidBlack);
        semiTL.setColor(0, 0, 0, 0.8f);

        menuTable.add(resumeButton).padBottom(20);
        menuTable.row();
        menuTable.add(optionsButton).padBottom(20);
        menuTable.row();
        menuTable.add(disconnectButton).padBottom(20);
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

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameState = GameState.GAME_OPTIONS;
                Gdx.input.setInputProcessor(gameOptionsStage);
            }
        });

        disconnectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MageShowdownClient.getInstance().setScreen(MenuScreen.getInstance());
                Gdx.input.setInputProcessor(MenuScreen.getMainMenuStage());
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
