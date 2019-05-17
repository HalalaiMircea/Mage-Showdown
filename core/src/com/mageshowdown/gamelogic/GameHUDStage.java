package com.mageshowdown.gamelogic;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mageshowdown.gameclient.ClientGameStage;

import static com.mageshowdown.gameclient.ClientAssetLoader.hudSkin;

public class GameHUDStage extends Stage {
    private static GameHUDStage ourInstance = new GameHUDStage();

    private ProgressBar healthOrb;
    private ProgressBar shieldOrb;

    private ClientGameStage gameStage = GameScreen.getGameStage();

    private GameHUDStage() {
        Table root = new Table();
        root.setFillParent(true);
        root.debug();

        healthOrb = new ProgressBar(0, PlayerCharacter.getMaxHealth(), 1, true, hudSkin, "health-orb");
        shieldOrb = new ProgressBar(0, PlayerCharacter.getMaxShield(), 1, true, hudSkin, "mana-orb");

        root.left().bottom();
        root.add(healthOrb).left().expandX();
        root.add(shieldOrb).right().expandX();

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
            shieldOrb.setValue(gameStage.getPlayerCharacter().getEnergyShield());
        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}
