package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;
import com.mageshowdown.utils.PrefsKeys;

import java.util.TreeSet;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;
import static com.mageshowdown.gameclient.ClientAssetLoader.uiSkin;

public class OptionsStage extends Stage {

    private Image background;
    private Table root;
    private Texture backgroundTexture;
    private TextField playerNameField;
    private TextButton backButton;
    private TextButton applyButton;
    private SelectBox<String> resSelectBox;
    private SelectBox<String> modeSelectBox;
    private SelectBox<Integer> refreshSelectBox;
    private TextButton vsyncCheckBox;

    private Graphics.DisplayMode[] displayModes;

    public OptionsStage(Texture backgroundTexture) {
        super();
        init(backgroundTexture);
    }

    public OptionsStage(Viewport viewport, Texture backgroundTexture) {
        super(viewport);
        init(backgroundTexture);
    }

    public OptionsStage(Viewport viewport, Batch batch, Texture backgroundTexture) {
        super(viewport, batch);
        init(backgroundTexture);
    }

    private void init(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        displayModes = Gdx.graphics.getDisplayModes();

        setupLayoutView();
        setupWidgetData();
        handleWidgetEvents();

        this.addActor(background);
        this.addActor(root);
    }

    private void setupLayoutView() {
        background = new Image(backgroundTexture);
        if (backgroundTexture.equals(ClientAssetLoader.solidBlack))
            background.setColor(0, 0, 0, 0.8f);
        background.setFillParent(true);

        root = new Table();
        root.setFillParent(true);
        //root.debug();

        Label resLabel = new Label("Resolution", uiSkin, "menu-label");
        Label displayModeLabel = new Label("Display Mode", uiSkin, "menu-label");
        Label refreshLabel = new Label("Refresh Rate", uiSkin, "menu-label");
        resSelectBox = new SelectBox<String>(uiSkin);
        modeSelectBox = new SelectBox<String>(uiSkin);
        refreshSelectBox = new SelectBox<Integer>(uiSkin);
        vsyncCheckBox = new TextButton("Vertical Sync: ", uiSkin);

        Label playerNameLabel = new Label("Player name", uiSkin, "menu-label");
        playerNameField = new TextField(prefs.getString(PrefsKeys.PLAYERNAME), uiSkin);
        playerNameField.setMessageText("Enter your name...");
        backButton = new TextButton("Back", uiSkin);
        applyButton = new TextButton("Apply", uiSkin);

        //(1280x720)->290w 60h cells 25pad right left 20 top bottom
        root.defaults().space(20, 25, 20, 25).width(290).height(60);
        root.add(resLabel, resSelectBox);
        root.row();
        root.add(refreshLabel, refreshSelectBox);
        root.row();
        root.add(displayModeLabel, modeSelectBox);
        root.row();
        root.add(playerNameLabel, playerNameField);
        root.row();
        root.add(vsyncCheckBox).colspan(2).width(605);
        root.row();
        root.add(backButton, applyButton);
    }

    private void setupWidgetData() {
        Array<Graphics.DisplayMode> optimalDisplayModes = new Array<Graphics.DisplayMode>();
        for (Graphics.DisplayMode each : displayModes) {
            float aspectNum = ((float) each.width / (float) each.height) * 9f;
            if (each.width >= 1280 && each.height >= 720 && aspectNum >= 15.9f && aspectNum <= 16.1f)
                optimalDisplayModes.add(each);
        }
        TreeSet<String> resSet = new TreeSet<String>();
        TreeSet<Integer> refreshSet = new TreeSet<Integer>();
        Array<String> resArray = new Array<String>();
        Array<Integer> refreshArray = new Array<Integer>();
        for (Graphics.DisplayMode each : optimalDisplayModes) {
            resSet.add(each.width + "x" + each.height);
            refreshSet.add(each.refreshRate);
        }
        for (String each : resSet)
            resArray.add(each);
        for (Integer each : refreshSet)
            refreshArray.add(each);

        resSelectBox.setItems(resArray);
        resSelectBox.setSelected(prefs.getInteger(PrefsKeys.WIDTH) + "x" + prefs.getInteger(PrefsKeys.HEIGHT));

        modeSelectBox.setItems("Fullscreen", "Windowed");
        if (prefs.getBoolean(PrefsKeys.FULLSCREEN))
            modeSelectBox.setSelected("Fullscreen");
        else
            modeSelectBox.setSelected("Windowed");

        refreshSelectBox.setItems(refreshArray);
        if (prefs.contains(PrefsKeys.REFRESHRATE))
            refreshSelectBox.setSelected(prefs.getInteger(PrefsKeys.REFRESHRATE));
        else
            refreshSelectBox.setSelected(60);

        if (prefs.getBoolean(PrefsKeys.VSYNC))
            vsyncCheckBox.setText("Vertical Sync: ON");
        else
            vsyncCheckBox.setText("Vertical Sync: OFF");
        vsyncCheckBox.setChecked(prefs.getBoolean(PrefsKeys.VSYNC));

    }

    private void handleWidgetEvents() {
        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String[] str = resSelectBox.getSelected().split("x");
                if (modeSelectBox.getSelected().equals("Fullscreen")) {
                    for (Graphics.DisplayMode each : displayModes)
                        if (each.width == Integer.parseInt(str[0]) && each.height == Integer.parseInt(str[1])
                                && each.refreshRate == refreshSelectBox.getSelected()) {
                            Gdx.graphics.setFullscreenMode(each);
                            break;
                        }
                } else Gdx.graphics.setWindowedMode(Integer.parseInt(str[0]), Integer.parseInt(str[1]));

                Gdx.graphics.setVSync(vsyncCheckBox.isChecked());

                prefs.putInteger(PrefsKeys.WIDTH, Integer.parseInt(str[0]));
                prefs.putInteger(PrefsKeys.HEIGHT, Integer.parseInt(str[1]));
                prefs.putInteger(PrefsKeys.REFRESHRATE, refreshSelectBox.getSelected());
                if (modeSelectBox.getSelected().equals("Fullscreen"))
                    prefs.putBoolean(PrefsKeys.FULLSCREEN, true);
                else if (modeSelectBox.getSelected().equals("Windowed"))
                    prefs.putBoolean(PrefsKeys.FULLSCREEN, false);
                prefs.putString(PrefsKeys.PLAYERNAME, playerNameField.getText());
                prefs.putBoolean(PrefsKeys.VSYNC, vsyncCheckBox.isChecked());
                prefs.flush();

                //when we apply new graphics settings the resolution may have changed so we update the resolution scale
                GameWorld.updateResolutionScale();
            }
        });
        vsyncCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vsyncCheckBox.isChecked())
                    vsyncCheckBox.setText("Vertical Sync: ON");
                else
                    vsyncCheckBox.setText("Vertical Sync: OFF");
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (MageShowdownClient.getInstance().getScreen().equals(MenuScreen.getInstance())) {
                    MenuScreen.setStagePhase(MenuScreen.StagePhase.MAIN_MENU_STAGE);
                    Gdx.input.setInputProcessor(MenuScreen.getMainMenuStage());
                }
                if (MageShowdownClient.getInstance().getScreen().equals(GameScreen.getInstance())) {
                    GameScreen.setGameState(GameScreen.GameState.GAME_PAUSED);
                    Gdx.input.setInputProcessor(GameScreen.getEscMenuStage());
                }
            }
        });
    }

    @Override
    public void act() {
        super.act();
    }

    @Override
    public void draw() {
        super.draw();
    }
}
