package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientListener;
import com.mageshowdown.gameclient.GameClient;
import com.mageshowdown.gameclient.MageShowdownClient;
import com.mageshowdown.packets.Network;
import com.mageshowdown.utils.PrefsKeys;

import java.io.IOException;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

public class MenuScreen implements Screen {

    //Singleton instantiation
    private static final MenuScreen INSTANCE = new MenuScreen();
    private static Viewport viewport;
    private static Batch batch;

    private static Stage mainMenuStage;
    private static Stage menuOptionsStage;
    private static StagePhase stagePhase;
    private GameClient myClient = GameClient.getInstance();

    public static MenuScreen getInstance() {
        return INSTANCE;
    }

    public static Stage getMainMenuStage() {
        return mainMenuStage;
    }

    public static void setStagePhase(StagePhase stagePhase) {
        MenuScreen.stagePhase = stagePhase;
    }

    @Override
    public void show() {
        //Show method is called when the current screen becomes this
        viewport = new ScreenViewport();
        batch = new SpriteBatch();

        mainMenuStage = new Stage(viewport, batch);
        menuOptionsStage = new OptionsStage(viewport, batch, ClientAssetLoader.menuBackground);

        prepareMainMenuStage();

        stagePhase = StagePhase.MAIN_MENU_STAGE;
        Gdx.input.setInputProcessor(mainMenuStage);
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
        //hide method is called when we change to a new screen or before the Application exits
        this.dispose();
    }

    @Override
    public void dispose() {
        mainMenuStage.dispose();
        menuOptionsStage.dispose();
        batch.dispose();
    }

    private void prepareMainMenuStage() {
        Image background = new Image(ClientAssetLoader.menuBackground);
        background.setFillParent(true);

        Table root = new Table();
        root.setFillParent(true);
        //root.debug();

        TextButton connectButton = new TextButton("Connect", ClientAssetLoader.uiSkin);
        TextButton optionsButton = new TextButton("Options...", ClientAssetLoader.uiSkin);
        TextButton quitButton = new TextButton("Quit Game", ClientAssetLoader.uiSkin);
        final TextField addressField = new TextField(prefs.getString(PrefsKeys.LASTENTEREDIP), ClientAssetLoader.uiSkin);

        //(1280x720)->290w 60h cells 25pad right left 20 top bottom
        root.defaults().space(20, 25, 20, 25).width(290).height(60);
        root.add(connectButton);
        root.add(addressField);
        root.row();
        root.defaults().width(605).colspan(2);
        root.add(optionsButton);
        root.row();
        root.add(quitButton);

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                if (GameWorld.world.getBodyCount() == 0) {
                    String ipAddress = addressField.getText();
                    prefs.putString(PrefsKeys.LASTENTEREDIP, ipAddress).flush();
                    clientStart(ipAddress);
                } else
                    System.out.println(GameWorld.world.getBodyCount());
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stagePhase = StagePhase.OPTIONS_STAGE;
                Gdx.input.setInputProcessor(menuOptionsStage);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        mainMenuStage.addActor(background);
        mainMenuStage.addActor(root);
    }

    private void clientStart(String ipAddress) {
        try {
            myClient.setUserName(prefs.getString(PrefsKeys.PLAYERNAME));
            myClient.start();
            GameScreen.start();
            GameScreen.setGameState(GameScreen.GameState.GAME_RUNNING);
            myClient.connect(5000, ipAddress, Network.TCP_PORT, Network.UDP_PORT);
            MageShowdownClient.getInstance().setScreen(GameScreen.getInstance());

        } catch (IOException e) {
            final Dialog dialog = new Dialog("", ClientAssetLoader.uiSkin);
            dialog.text(e.toString(), ClientAssetLoader.uiSkin.get("menu-label", Label.LabelStyle.class));
            Button backBtn = new TextButton("Back", ClientAssetLoader.uiSkin);
            backBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    dialog.hide();
                }
            });
            dialog.button(backBtn);
            dialog.setMovable(false);
            dialog.show(mainMenuStage);
        }
        myClient.addListener(new ClientListener());
    }

    public enum StagePhase {
        MAIN_MENU_STAGE,
        OPTIONS_STAGE
    }
}