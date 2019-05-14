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

    private Table foreground;
    private Table background;
    private Texture backgroundTexture;
    private TextField playerNameField;
    private TextButton backButton;
    private TextButton applyButton;
    private SelectBox<String> resSelectBox;
    private SelectBox<String> modeSelectBox;
    private SelectBox<Integer> refreshSelectBox;
    private CheckBox vsyncCheckBox;

    private Graphics.DisplayMode[] displayModes;

    public OptionsStage(Viewport viewport, Batch batch, Texture backgroundTexture) {
        super(viewport, batch);
        this.backgroundTexture = backgroundTexture;
        displayModes = Gdx.graphics.getDisplayModes();

        setupLayoutView();
        setupWidgetData();
        handleWidgetEvents();

        this.addActor(background);
        this.addActor(foreground);
    }

    private void setupLayoutView() {
        background = new Table();
        Image bgImage = new Image(backgroundTexture);
        if (backgroundTexture.equals(ClientAssetLoader.solidBlack))
            bgImage.setColor(0, 0, 0, 0.8f);
        background.add(bgImage);
        background.setFillParent(true);

        foreground = new Table();
        foreground.setFillParent(true);
        //foreground.debug();

        Label resLabel = new Label("Resolution", uiSkin);
        Label displayModeLabel = new Label("Display Mode", uiSkin);
        Label refreshLabel = new Label("Refresh Rate", uiSkin);
        resSelectBox = new SelectBox<String>(uiSkin);
        modeSelectBox = new SelectBox<String>(uiSkin);
        refreshSelectBox = new SelectBox<Integer>(uiSkin);
        vsyncCheckBox = new CheckBox("Vertical Sync", uiSkin);

        Label playerNameLabel = new Label("Player name", uiSkin);
        playerNameField = new TextField(prefs.getString(PrefsKeys.PLAYERNAME), uiSkin);
        playerNameField.setMessageText("Enter your name...");
        backButton = new TextButton("Back", uiSkin);
        applyButton = new TextButton("Apply", uiSkin);

        foreground.defaults().padBottom(20).padRight(10);
        foreground.add(resLabel, resSelectBox);
        foreground.row();
        foreground.add(refreshLabel, refreshSelectBox);
        foreground.row();
        foreground.add(displayModeLabel, modeSelectBox);
        foreground.row();
        foreground.add(playerNameLabel, playerNameField);
        foreground.row();
        foreground.add(vsyncCheckBox).colspan(2);
        foreground.row();
        foreground.add(backButton, applyButton);
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
        else {
            refreshSelectBox.setSelected(60);
            prefs.putInteger(PrefsKeys.REFRESHRATE, refreshSelectBox.getSelected());
        }
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
