package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.*;
import com.mageshowdown.utils.PrefsKeys;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private static final GameScreen INSTANCE = new GameScreen();
    private static Viewport viewport;
    private static Batch batch;

    private static ClientGameStage gameStage;
    private static GameHUDStage hudStage;
    private static OptionsStage gameOptionsStage;
    private static Stage escMenuStage;
    private static ScoreboardStage scoreboardStage;
    private static RoundEndStage roundEndStage;

    private static GameState gameState;

    public static void start() {
        viewport = new ScreenViewport();
        batch = new SpriteBatch();

        gameStage = new ClientGameStage();
        hudStage = new GameHUDStage(viewport, batch);
        scoreboardStage = new ScoreboardStage(viewport, batch);
        escMenuStage = new Stage(viewport, batch);
        gameOptionsStage = new OptionsStage(viewport, batch, ClientAssetLoader.solidBlack);
        roundEndStage = new RoundEndStage(viewport, batch);

        prepareEscMenu();
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
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (ClientRound.getInstance().isFinished()) {
            ClientRound.getInstance().act(delta);
            roundEndStage.act();
            gameStage.draw();
            roundEndStage.draw();
        } else {
            gameStage.act();
            gameStage.draw();
        }

        switch (gameState) {
            case GAME_RUNNING:
                gameRunningInput();
                hudStage.act();
                hudStage.draw();
                break;
            case GAME_PAUSED:
                gamePausedInput();
                escMenuStage.act();
                escMenuStage.draw();
                break;
            case GAME_OPTIONS:
                gameOptionsStage.act();
                gameOptionsStage.draw();
                break;
            case SCOREBOARD:
                scoreboardInput();
                scoreboardStage.act();
                scoreboardStage.draw();
                break;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        gameStage.dispose();
        escMenuStage.dispose();
        gameOptionsStage.dispose();
        scoreboardStage.dispose();
        hudStage.dispose();
        roundEndStage.dispose();
        batch.dispose();
    }

    private void gameRunningInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_RUNNING) {
            gameState = GameState.GAME_PAUSED;
            Gdx.input.setInputProcessor(escMenuStage);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && gameState == GameState.GAME_RUNNING)
            gameState = GameState.SCOREBOARD;
    }

    private void gamePausedInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_PAUSED) {
            gameState = GameState.GAME_RUNNING;
            Gdx.input.setInputProcessor(gameStage.getPlayerCharacter());
        }
    }

    private void scoreboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && gameState == GameState.SCOREBOARD)
            gameState = GameState.GAME_RUNNING;
    }

    private static void prepareEscMenu() {
        Image background = new Image(ClientAssetLoader.menuBackground);
        background.setFillParent(true);
        background.setColor(0, 0, 0, 0.8f);

        Table root = new Table();
        root.setFillParent(true);
        //root.debug();

        TextButton resumeButton = new TextButton("Resume Game", ClientAssetLoader.uiSkin);
        TextButton optionsButton = new TextButton("Options...", ClientAssetLoader.uiSkin);
        TextButton quitButton = new TextButton("Quit Game", ClientAssetLoader.uiSkin);
        TextButton disconnectButton = new TextButton("Disconnect", ClientAssetLoader.uiSkin);

        //(1280x720)->290w 60h cells 25pad right left 20 top bottom
        root.defaults().space(20, 25, 20, 25).width(605).height(60).colspan(2);
        root.add(resumeButton);
        root.row();
        root.add(optionsButton);
        root.row();
        root.defaults().width(290).colspan(1);
        root.add(disconnectButton, quitButton);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

                gameState = GameState.GAME_RUNNING;
                Gdx.input.setInputProcessor(gameStage.getPlayerCharacter());
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

                gameState = GameState.GAME_OPTIONS;
                Gdx.input.setInputProcessor(gameOptionsStage);
            }
        });

        disconnectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

                GameClient.getInstance().stop();
                ClientRound.getInstance().stop();

                MageShowdownClient.getInstance().setScreen(MenuScreen.getInstance());

                Gdx.input.setInputProcessor(MenuScreen.getMainMenuStage());
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

                final Dialog dialog = new Dialog("", ClientAssetLoader.uiSkin);
                dialog.text("Are you sure?", ClientAssetLoader.uiSkin.get("menu-label", Label.LabelStyle.class));
                Button confirmBtn = new TextButton("Yes, quit the Game!", ClientAssetLoader.uiSkin);
                Button cancelBtn = new TextButton("No, take me back!", ClientAssetLoader.uiSkin);
                confirmBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));
                        Gdx.app.exit();
                    }
                });
                cancelBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));
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
        escMenuStage.addActor(root);
    }

    enum GameState {
        GAME_RUNNING, GAME_PAUSED, GAME_OPTIONS, SCOREBOARD
    }
}
