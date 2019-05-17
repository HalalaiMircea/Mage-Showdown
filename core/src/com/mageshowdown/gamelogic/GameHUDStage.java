package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;

import static com.mageshowdown.gameclient.ClientAssetLoader.hudSkin;

public class GameHUDStage extends Stage {
    private static GameHUDStage ourInstance = new GameHUDStage();

    private ProgressBar healthOrb;
    private ProgressBar shieldBar;
    private Label healthText;
    private Label shieldText;

    private ClientGameStage gameStage = GameScreen.getGameStage();

    private GameHUDStage() {
        Table root = new Table();
        root.setFillParent(true);
        //root.debug();

        Label.LabelStyle resourceTextStyle = new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.WHITE);
        TiledDrawable tiledDrawable = hudSkin.getTiledDrawable("health-orb-fill");
        tiledDrawable.setMinHeight(0.0f);
        hudSkin.get("health-orb", ProgressBar.ProgressBarStyle.class).knobBefore = tiledDrawable;
        healthOrb = new ProgressBar(0f, PlayerCharacter.getMaxHealth(), 0.5f, true, hudSkin, "health-orb");
        healthOrb.setAnimateDuration(0.1f);
        healthText = new Label("", resourceTextStyle);
        Stack healthStack = new Stack();
        healthStack.add(healthOrb);
        healthStack.add(healthText);

        tiledDrawable = hudSkin.getTiledDrawable("progress-bar-mana-v");
        tiledDrawable.setMinHeight(0.0f);
        hudSkin.get("mana-vertical", ProgressBar.ProgressBarStyle.class).knobBefore = tiledDrawable;
        shieldBar = new ProgressBar(0f, PlayerCharacter.getMaxShield(), 0.5f, true, hudSkin, "mana-vertical");
        shieldBar.setAnimateDuration(0.1f);
        shieldText = new Label("", resourceTextStyle);
        Stack shieldStack = new Stack();
        shieldStack.add(shieldBar);
        shieldStack.add(shieldText);

        root.left().bottom();
        root.add(healthStack).width(201).height(164).left();
        root.add(shieldStack).height(175).padLeft(20).left().expandX();

        this.addActor(root);
    }

    public static GameHUDStage getInstance() {
        return ourInstance;
    }

    @Override
    public void act() {
        super.act();
        if (gameStage.getPlayerCharacter() != null) {
            healthOrb.setValue(gameStage.getPlayerCharacter().getHealth());
            healthText.setText("    " + (int) gameStage.getPlayerCharacter().getHealth());
            shieldBar.setValue(gameStage.getPlayerCharacter().getEnergyShield());
            shieldText.setText(" " + (int) gameStage.getPlayerCharacter().getEnergyShield());
        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}
