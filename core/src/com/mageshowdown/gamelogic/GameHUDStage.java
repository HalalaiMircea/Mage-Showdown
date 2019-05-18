package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;

public class GameHUDStage extends Stage {
    private static GameHUDStage ourInstance = new GameHUDStage();

    private Label healthLabel;
    private Label shieldLabel;
    private Label ammoLabel;
    private ProgressBar healthBar;
    private ProgressBar shieldBar;

    private ClientGameStage gameStage = GameScreen.getInstance().getGameStage();

    private GameHUDStage() {
        Table root = new Table();
        root.setFillParent(true);
        root.debug();

        healthLabel = new Label("HEALTH", new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.RED));
        shieldLabel = new Label("SHIELD", new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.BLUE));
        ammoLabel = new Label("AMMO", new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.YELLOW));
        //healthBar = new ProgressBar(0, PlayerCharacter.getMaxHealth(), 1, false, ClientAssetLoader.uiSkin);
        //shieldBar = new ProgressBar(0, PlayerCharacter.getMaxShield(), 1, false, ClientAssetLoader.uiSkin);

        root.left().bottom();
        root.add(healthLabel).padRight(20);
        root.add(shieldLabel);
        root.add(ammoLabel).expandX().right();

        this.addActor(root);
    }

    public static GameHUDStage getInstance() {
        return ourInstance;
    }

    @Override
    public void act() {
        super.act();
        if (gameStage.getPlayerCharacter() != null) {
            healthLabel.setText("HEALTH: " + (int) gameStage.getPlayerCharacter().getHealth());
            shieldLabel.setText("SHIELD: " + (int) gameStage.getPlayerCharacter().getEnergyShield());
            //healthBar.setValue(gameStage.getPlayerCharacter().getHealth());
            //shieldBar.setValue(gameStage.getPlayerCharacter().getEnergyShield());
        }else{
            System.out.println("halp");
        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}
