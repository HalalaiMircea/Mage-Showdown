package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Game;
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
import com.mageshowdown.gameclient.*;

public class GameScreen implements Screen {

    enum GameState {
        GAME_RUNNING, GAME_PAUSED, GAME_OPTIONS, SCOREBOARD
    }

    private static final GameScreen INSTANCE = new GameScreen();

    private static ClientGameStage gameStage;
    private static OptionsStage gameOptionsStage;
    private static Stage escMenuStage;
    private static GameState gameState;

    private GameScreen() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
            case GAME_RUNNING:
                if (!ClientRound.getInstance().isFinished())
                    gameStage.act();
                else
                    ClientRound.getInstance().act(Gdx.graphics.getDeltaTime());
                gameRunningInput();
                gameStage.draw();
                GameHUDStage.getInstance().act();
                GameHUDStage.getInstance().draw();
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
            case SCOREBOARD:
                gameStage.act();
                gameStage.draw();
                ScoreboardStage.getInstance().act();
                ScoreboardStage.getInstance().draw();
                scoreboardInput();
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

    public static void setGameState(GameState gameState) {
        GameScreen.gameState = gameState;
    }

    public static void start() {
        gameStage = new ClientGameStage();
        escMenuStage = new Stage(MenuScreen.getMainMenuStage().getViewport(), gameStage.getBatch());
        gameOptionsStage = new OptionsStage(MenuScreen.getMainMenuStage().getViewport(), gameStage.getBatch(),
                ClientAssetLoader.solidBlack);

        prepareEscMenu();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
        gameOptionsStage.getViewport().update(width, height, true);
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
        ScoreboardStage.getInstance().dispose();
        GameHUDStage.getInstance().dispose();
        INSTANCE.dispose();
    }

    private static void gameRunningInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_RUNNING) {
            gameState = GameState.GAME_PAUSED;
            Gdx.input.setInputProcessor(escMenuStage);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && gameState == GameState.GAME_RUNNING)
            gameState = GameState.SCOREBOARD;
    }

    private static void gamePausedInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_PAUSED) {
            gameState = GameState.GAME_RUNNING;
            Gdx.input.setInputProcessor(gameStage);
        }
    }

    private static void scoreboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && gameState == GameState.SCOREBOARD)
            gameState = GameState.GAME_RUNNING;
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
                GameClient.getInstance().stop();
                ClientRound.getInstance().stop();
                gameStage.clear();

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
