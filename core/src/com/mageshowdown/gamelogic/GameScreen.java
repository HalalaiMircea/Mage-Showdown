package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mageshowdown.gameclient.*;

public class GameScreen implements Screen {

    enum GameState {
        GAME_RUNNING, GAME_PAUSED, GAME_OPTIONS, SCOREBOARD
    }

    private static final GameScreen INSTANCE = new GameScreen();

    private static ClientGameStage gameStage;
    private static GameHUDStage hudStage;
    private static OptionsStage gameOptionsStage;
    private static Stage escMenuStage;
    private static GameState gameState;

    private GameScreen() {
    }

    public static void start() {
        gameStage = new ClientGameStage();
        hudStage = new GameHUDStage();
        escMenuStage = new Stage(MenuScreen.getMainMenuStage().getViewport(), gameStage.getBatch());
        gameOptionsStage = new OptionsStage(ClientAssetLoader.solidBlack);

        prepareEscMenu();
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
                hudStage.act();
                hudStage.draw();
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

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
        escMenuStage.getViewport().update(width, height, true);
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
        hudStage.dispose();
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
            Gdx.input.setInputProcessor(gameStage.getPlayerCharacter());
        }
    }

    private static void scoreboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && gameState == GameState.SCOREBOARD)
            gameState = GameState.GAME_RUNNING;
    }

    private static void prepareEscMenu() {
        Table menuTable = new Table();
        menuTable.setFillParent(true);
        //menuTable.debug();
        Table background = new Table();
        background.setFillParent(true);

        TextButton resumeButton = new TextButton("Resume Game", ClientAssetLoader.uiSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.uiSkin);
        TextButton quitButton = new TextButton("Quit Game", ClientAssetLoader.uiSkin);
        TextButton disconnectButton = new TextButton("Disconnect", ClientAssetLoader.uiSkin);
        Image semiTL = new Image(ClientAssetLoader.solidBlack);
        semiTL.setColor(0, 0, 0, 0.8f);

        //(1280x720)->290w 60h cells 25pad right left 20 top bottom
        menuTable.defaults().space(20, 25, 20, 25).width(605).height(60).colspan(2);
        menuTable.add(resumeButton);
        menuTable.row();
        menuTable.add(optionsButton);
        menuTable.row();
        menuTable.defaults().width(290).colspan(1);
        menuTable.add(disconnectButton, quitButton);
        background.add(semiTL);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameState = GameState.GAME_RUNNING;
                Gdx.input.setInputProcessor(gameStage.getPlayerCharacter());
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
                final Dialog dialog = new Dialog("", ClientAssetLoader.uiSkin);
                dialog.text("Are you sure?");
                Button confirmBtn = new TextButton("Yes, quit the Game!", ClientAssetLoader.uiSkin);
                Button cancelBtn = new TextButton("No, take me back!", ClientAssetLoader.uiSkin);
                confirmBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Gdx.app.exit();
                    }
                });
                cancelBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        dialog.hide();
                    }
                });
                dialog.button(confirmBtn);
                dialog.button(cancelBtn);
                dialog.setMovable(false);
                dialog.show(escMenuStage);
            }
        });

        escMenuStage.addActor(background);
        escMenuStage.addActor(menuTable);
    }
}
