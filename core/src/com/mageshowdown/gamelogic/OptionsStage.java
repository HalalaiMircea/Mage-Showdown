package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;
import com.mageshowdown.utils.PrefsKeys;

import java.util.TreeSet;
import java.util.stream.Stream;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;
import static com.mageshowdown.gameclient.ClientAssetLoader.uiSkin;

public class OptionsStage extends Stage {

    private Image background;
    private Texture backgroundTexture;
    private Table root;
    private TextField playerNameField;
    private TextButton vsyncCheckBox;
    private TextButton showFPSCheckBox;
    private TextButton backButton;
    private TextButton applyButton;
    private SelectBox<String> resSelectBox;
    private SelectBox<String> modeSelectBox;
    private SelectBox<Integer> refreshSelectBox;
    private Slider soundVolumeSlider;
    private Slider musicVolumeSlider;
    private Label[] labels;

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
        Table contextTable = new Table();
        //contextTable.debug();
        Table bottomTable = new Table();
        //bottomTable.debug();

        labels = new Label[7];
        labels[0] = new Label("Options Menu", uiSkin, "menu-label");
        labels[1] = new Label("Resolution", uiSkin, "menu-label");
        labels[2] = new Label("Refresh Rate", uiSkin, "menu-label");
        labels[3] = new Label("Display Mode", uiSkin, "menu-label");
        labels[4] = new Label("Player name", uiSkin, "menu-label");
        labels[5] = new Label("Sound", uiSkin, "menu-label");
        labels[6] = new Label("Music", uiSkin, "menu-label");
        Stream.of(labels).forEach(label -> label.setAlignment(Align.center));

        resSelectBox = new SelectBox<>(uiSkin);
        modeSelectBox = new SelectBox<>(uiSkin);
        refreshSelectBox = new SelectBox<>(uiSkin);
        vsyncCheckBox = new TextButton("", uiSkin);
        showFPSCheckBox = new TextButton("", uiSkin);
        soundVolumeSlider = new Slider(0f, 1f, 0.05f, false, uiSkin);
        musicVolumeSlider = new Slider(0f, 1f, 0.05f, false, uiSkin);
        playerNameField = new TextField(prefs.getString(PrefsKeys.PLAYERNAME), uiSkin);
        playerNameField.setMessageText("Enter your name...");

        backButton = new TextButton("Back", uiSkin);
        applyButton = new TextButton("Apply", uiSkin);

        //(1280x720)->290w 60h cells 25pad right left 20 top bottom
        //Here we position the widgets in the context table
        contextTable.defaults().space(20, 25, 20, 25).width(290).height(60);
        contextTable.add(labels[0]).colspan(2);
        contextTable.row();
        contextTable.add(labels[1], resSelectBox);
        contextTable.row();
        contextTable.add(labels[2], refreshSelectBox);
        contextTable.row();
        contextTable.add(labels[3], modeSelectBox);
        contextTable.row();
        contextTable.add(labels[4], playerNameField);
        contextTable.row();
        contextTable.add(vsyncCheckBox, showFPSCheckBox);
        contextTable.row();
        contextTable.defaults().space(20, 25, 20, 25).width(290).height(60);
        contextTable.add(labels[5], soundVolumeSlider);
        contextTable.row();
        contextTable.add(labels[6], musicVolumeSlider);

        //And here we position the back and apply buttons in the bottom table
        bottomTable.defaults().space(20, 25, 20, 25).width(200).height(60);
        bottomTable.add(backButton, applyButton);

        //Finally, here we position the 2 tables into the root table
        root.add(contextTable).expand();
        root.row();
        root.add(bottomTable).bottom().left().pad(20, 20, 20, 20);

        /*MoveToAction action = new MoveToAction();
        action.setTarget(contextTable);
        action.setStartPosition(Gdx.graphics.getWidth() / 2f - contextTable.getWidth() / 2f, 0);
        action.setPosition(contextTable.getX(), contextTable.getY());
        contextTable.addAction(action);
        action.setDuration(1f);*/
    }

    private void setupWidgetData() {
        Array<Graphics.DisplayMode> optimalDisplayModes = new Array<>();
        for (Graphics.DisplayMode each : displayModes) {
            float aspectNum = ((float) each.width / (float) each.height) * 9f;
            if (each.width >= 1280 && each.height >= 720 && aspectNum >= 15.9f && aspectNum <= 16.1f)
                optimalDisplayModes.add(each);
        }
        TreeSet<String> resSet = new TreeSet<>();
        TreeSet<Integer> refreshSet = new TreeSet<>();
        Array<String> resArray = new Array<>();
        Array<Integer> refreshArray = new Array<>();
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
        refreshSelectBox.setSelected(prefs.getInteger(PrefsKeys.REFRESHRATE));

        if (prefs.getBoolean(PrefsKeys.VSYNC))
            vsyncCheckBox.setText("VSync: ON");
        else
            vsyncCheckBox.setText("VSync: OFF");
        vsyncCheckBox.setChecked(prefs.getBoolean(PrefsKeys.VSYNC));

        if (prefs.getBoolean(PrefsKeys.SHOWFPS))
            showFPSCheckBox.setText("Show FPS: ON");
        else
            showFPSCheckBox.setText("Show FPS: OFF");
        showFPSCheckBox.setChecked(prefs.getBoolean(PrefsKeys.SHOWFPS));

        soundVolumeSlider.setValue(prefs.getFloat(PrefsKeys.SOUNDVOLUME));
        labels[5].setText("Sound: " + (int) (soundVolumeSlider.getPercent() * 100) + "%");
        musicVolumeSlider.setValue(prefs.getFloat(PrefsKeys.MUSICVOLUME));
        labels[6].setText("Music: " + (int) (musicVolumeSlider.getPercent() * 100) + "%");
    }

    private void handleWidgetEvents() {
        vsyncCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

                if (vsyncCheckBox.isChecked())
                    vsyncCheckBox.setText("VSync: ON");
                else
                    vsyncCheckBox.setText("VSync: OFF");
            }
        });

        showFPSCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));
                if (showFPSCheckBox.isChecked())
                    showFPSCheckBox.setText("Show FPS: ON");
                else
                    showFPSCheckBox.setText("Show FPS: OFF");
            }
        });

        soundVolumeSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                prefs.putFloat(PrefsKeys.SOUNDVOLUME, soundVolumeSlider.getValue());
                prefs.flush();
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));
                labels[5].setText("Sound: " + (int) (soundVolumeSlider.getPercent() * 100) + "%");
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                labels[5].setText("Sound: " + (int) (soundVolumeSlider.getPercent() * 100) + "%");
            }
        });

        musicVolumeSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                prefs.putFloat(PrefsKeys.MUSICVOLUME, musicVolumeSlider.getValue());
                prefs.flush();
                ClientAssetLoader.gameplayMusic.setVolume(prefs.getFloat(PrefsKeys.MUSICVOLUME) * 0.5f);
                ClientAssetLoader.menuMusic.setVolume(prefs.getFloat(PrefsKeys.MUSICVOLUME));
                labels[6].setText("Music: " + (int) (musicVolumeSlider.getPercent() * 100) + "%");
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                labels[6].setText("Music: " + (int) (musicVolumeSlider.getPercent() * 100) + "%");
            }
        });

        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Apply new settings
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

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
                MageShowdownClient.getInstance().setCanDrawFont(showFPSCheckBox.isChecked());

                //Save settings to the Preferences Map
                prefs.putInteger(PrefsKeys.WIDTH, Integer.parseInt(str[0]));
                prefs.putInteger(PrefsKeys.HEIGHT, Integer.parseInt(str[1]));
                prefs.putInteger(PrefsKeys.REFRESHRATE, refreshSelectBox.getSelected());
                if (modeSelectBox.getSelected().equals("Fullscreen"))
                    prefs.putBoolean(PrefsKeys.FULLSCREEN, true);
                else if (modeSelectBox.getSelected().equals("Windowed"))
                    prefs.putBoolean(PrefsKeys.FULLSCREEN, false);
                prefs.putString(PrefsKeys.PLAYERNAME, playerNameField.getText());
                prefs.putBoolean(PrefsKeys.VSYNC, vsyncCheckBox.isChecked());
                prefs.putBoolean(PrefsKeys.SHOWFPS, showFPSCheckBox.isChecked());

                prefs.flush();

                //when we apply new graphics settings the resolution may have changed so we update the resolution scale
                GameWorld.updateResolutionScale();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ClientAssetLoader.btnClickSound.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME));

                if (MageShowdownClient.getInstance().getScreen().equals(MenuScreen.getInstance())) {
                    MenuScreen.setStagePhase(MenuScreen.StagePhase.MAIN_MENU_STAGE);
                    Gdx.input.setInputProcessor(MenuScreen.getMainMenuStage());
                    MenuScreen.getMenuOptionsStage().dispose();
                }
                if (MageShowdownClient.getInstance().getScreen().equals(GameScreen.getInstance())) {
                    GameScreen.setGameState(GameScreen.GameState.GAME_PAUSED);
                    Gdx.input.setInputProcessor(GameScreen.getEscMenuStage());
                    GameScreen.getGameOptionsStage().dispose();
                }
            }
        });
    }
}
