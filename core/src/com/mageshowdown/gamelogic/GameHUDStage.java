package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;

import static com.mageshowdown.gameclient.ClientAssetLoader.hudSkin;

public class GameHUDStage extends Stage {
    private ProgressBar healthOrb;
    private ProgressBar shieldBar;
    private ProgressBar ammoBar;
    private Label healthText;
    private Label shieldText;
    private Label ammoText;

    private ClientGameStage gameStage = GameScreen.getInstance().getGameStage();

    public GameHUDStage() {
        super();
        init();
    }

    public GameHUDStage(Viewport viewport) {
        super(viewport);
        init();
    }

    public GameHUDStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        init();
    }

    private void init() {
        Table root = new Table();
        root.setFillParent(true);
        //root.debug();

        Label.LabelStyle resourceTextStyle = new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.WHITE);
        TiledDrawable tiledDrawable = hudSkin.getTiledDrawable("health-orb-fill");
        tiledDrawable.setMinHeight(0f);
        hudSkin.get("health-orb", ProgressBar.ProgressBarStyle.class).knobBefore = tiledDrawable;
        healthOrb = new ProgressBar(0f, PlayerCharacter.getMaxHealth(), 1f, true, hudSkin, "health-orb");
        healthOrb.setAnimateDuration(0.1f);
        healthText = new Label("", resourceTextStyle);
        Stack healthStack = new Stack();
        healthStack.add(healthOrb);
        healthStack.add(healthText);

        tiledDrawable = hudSkin.getTiledDrawable("progress-bar-mana-v");
        tiledDrawable.setMinHeight(0f);
        hudSkin.get("mana-vertical", ProgressBar.ProgressBarStyle.class).knobBefore = tiledDrawable;
        shieldBar = new ProgressBar(0f, PlayerCharacter.getMaxShield(), 1f, true, hudSkin, "mana-vertical");
        shieldBar.setAnimateDuration(0.1f);
        shieldText = new Label("", resourceTextStyle);
        Stack shieldStack = new Stack();
        shieldStack.add(shieldBar);
        shieldStack.add(shieldText);

        tiledDrawable = hudSkin.getTiledDrawable("progress-bar-mana");
        tiledDrawable.setMinWidth(0f);
        hudSkin.get("mana", ProgressBar.ProgressBarStyle.class).knobBefore = tiledDrawable;
        ammoBar = new ProgressBar(0, 25, 1f, false, hudSkin, "mana");
        ammoBar.setAnimateDuration(0.1f);
        ammoText = new Label("", resourceTextStyle);
        Stack ammoStack = new Stack();
        ammoStack.add(ammoBar);
        ammoStack.add(ammoText);

        root.left().bottom();
        root.add(healthStack).width(201).height(164).left();
        root.add(shieldStack).height(175).padLeft(20).left().expandX();
        root.add(ammoStack).width(175);

        this.addActor(root);
    }

    @Override
    public void act() {
        super.act();
        if (gameStage.getPlayerCharacter() != null) {
            healthOrb.setValue(gameStage.getPlayerCharacter().getHealth());
            healthText.setText("    " + (int) gameStage.getPlayerCharacter().getHealth());

            shieldBar.setValue(gameStage.getPlayerCharacter().getEnergyShield());
            shieldText.setText(" " + (int) gameStage.getPlayerCharacter().getEnergyShield());

            float maxCapacity = gameStage.getPlayerCharacter().getCurrWeapon().getMaxCapacity();
            float currCapacity = gameStage.getPlayerCharacter().getCurrWeapon().getCurrentCapacity();
            ammoBar.setRange(0f, maxCapacity);
            ammoBar.setValue((int) currCapacity);
            ammoText.setText("  " + (int) currCapacity + "/" + (int) maxCapacity);
        }
    }
}
