package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;


public class MenuScreen implements Screen {

    public enum StagePhase {
        MAIN_MENU_STAGE,
        OPTIONS_STAGE
    }

    private static StagePhase stagePhase;
    private static Stage mainMenuStage;
    private Stage optionsStage;
    private GameScreen gameScreen;
    private final MageShowdownClient game;

    public MenuScreen(final MageShowdownClient game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        prepareMainMenuStage();
        prepareOptionsStage();

        stagePhase = StagePhase.MAIN_MENU_STAGE;
        Gdx.input.setInputProcessor(mainMenuStage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (stagePhase) {
            case MAIN_MENU_STAGE:
                mainMenuStage.act(delta);
                mainMenuStage.draw();
                break;
            case OPTIONS_STAGE:
                optionsStage.act(delta);
                optionsStage.draw();
                break;
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
        mainMenuStage.dispose();
        optionsStage.dispose();
    }

    public static Stage getMainMenuStage() {
        return mainMenuStage;
    }

    private void prepareMainMenuStage() {
        mainMenuStage = new Stage();

        Table background = new Table();
        background.add(new Image(ClientAssetLoader.menuBackground));
        background.setFillParent(true);

        Table foreground = new Table();
        foreground.setFillParent(true);
        //foreground.debug();

        //Widgets declarations
        TextButton connectButton = new TextButton("Connect to:", ClientAssetLoader.interfaceSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.interfaceSkin);
        TextButton quitButton = new TextButton("Quit to Desktop", ClientAssetLoader.interfaceSkin);
        final TextField addressField = new TextField("127.0.0.1", ClientAssetLoader.interfaceSkin);

        //
        //Order sensitive addition and positioning of widgets into table
        //
        foreground.add(connectButton).padBottom(20);
        foreground.add(addressField).padBottom(20);
        foreground.row();
        foreground.add(optionsButton).padBottom(20).colspan(2).width(200);
        foreground.row();
        foreground.add(quitButton).colspan(2).width(200);

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

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stagePhase = StagePhase.OPTIONS_STAGE;
                Gdx.input.setInputProcessor(optionsStage);
            }
        });
        mainMenuStage.addActor(background);
        mainMenuStage.addActor(foreground);
    }

    private void prepareOptionsStage() {
        optionsStage = new Stage();

        Table background = new Table();
        background.add(new Image(ClientAssetLoader.menuBackground));
        background.setFillParent(true);

        Table foreground = new Table();
        foreground.setFillParent(true);
        foreground.debug();

        TextButton backButton = new TextButton("Back", ClientAssetLoader.interfaceSkin);

        foreground.add(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stagePhase = StagePhase.MAIN_MENU_STAGE;
                Gdx.input.setInputProcessor(mainMenuStage);
            }
        });
        optionsStage.addActor(background);
        optionsStage.addActor(foreground);
    }
}
