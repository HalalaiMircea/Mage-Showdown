package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;

public class MenuScreen implements Screen {

    public enum StagePhase {
        MAIN_MENU_STAGE,
        OPTIONS_STAGE
    }

    //Singleton instantiation
    private static final MenuScreen INSTANCE = new MenuScreen();

    private static Stage mainMenuStage;
    private static OptionsStage menuOptionsStage;
    private static StagePhase stagePhase;

    private MenuScreen() {
        mainMenuStage = new Stage();
        menuOptionsStage = new OptionsStage(mainMenuStage.getViewport(), mainMenuStage.getBatch(),
                ClientAssetLoader.menuBackground);
        System.out.println("MENU SCREEN WAS SET");

        prepareMainMenuStage();

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
                mainMenuStage.act();
                mainMenuStage.draw();
                break;
            case OPTIONS_STAGE:
                menuOptionsStage.act();
                menuOptionsStage.draw();
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        mainMenuStage.getViewport().update(width, height, true);
        menuOptionsStage.getViewport().update(width, height, true);
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
        menuOptionsStage.dispose();
        INSTANCE.dispose();
    }

    public static MenuScreen getInstance() {
        return INSTANCE;
    }

    public static Stage getMainMenuStage() {
        return mainMenuStage;
    }

    public static OptionsStage getMenuOptionsStage() {
        return menuOptionsStage;
    }

    public static void setStagePhase(StagePhase stagePhase) {
        MenuScreen.stagePhase = stagePhase;
    }

    private static void prepareMainMenuStage() {
        Table background = new Table();
        background.add(new Image(ClientAssetLoader.menuBackground));
        background.setFillParent(true);


        Table foreground = new Table();
        foreground.setFillParent(true);
        foreground.debug();

        //Widgets declarations
        TextButton connectButton = new TextButton("Connect to:", ClientAssetLoader.uiSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.uiSkin);
        TextButton quitButton = new TextButton("Quit to Desktop", ClientAssetLoader.uiSkin);
        final TextField addressField = new TextField("127.0.0.1", ClientAssetLoader.uiSkin);

        //
        //Order sensitive addition and positioning of widgets into table
        //
        foreground.defaults().padBottom(20);
        foreground.add(connectButton);
        foreground.add(addressField);
        foreground.row();
        foreground.add(optionsButton).colspan(2).width(200);
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
                MageShowdownClient.getInstance().setScreen(GameScreen.getInstance());
                GameScreen.start();
                GameScreen.setGameState(GameScreen.GameState.GAME_RUNNING);
                String ipAddress = addressField.getText();
                MageShowdownClient.getInstance().clientStart(ipAddress);
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stagePhase = StagePhase.OPTIONS_STAGE;
                Gdx.input.setInputProcessor(menuOptionsStage);
            }
        });
        mainMenuStage.addActor(background);
        mainMenuStage.addActor(foreground);
    }
}
