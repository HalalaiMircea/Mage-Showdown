package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;

public class GameHUDStage extends Stage {
    private static GameHUDStage ourInstance = new GameHUDStage();

    private Label healthLabel;
    private Label shieldLabel;
    private Label ammoLabel;

    private ClientGameStage gameStage = GameScreen.getGameStage();

    private GameHUDStage() {
        Table root = new Table();
        root.setFillParent(true);
        root.debug();

        healthLabel = new Label("HEALTH", new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.RED));
        shieldLabel = new Label("SHIELD", new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.BLUE));
        ammoLabel = new Label("AMMO", new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.YELLOW));

        root.left().bottom();
        root.add(healthLabel);
        root.add(shieldLabel).expandX();
        root.add(ammoLabel);

        this.addActor(root);
    }

    public static GameHUDStage getInstance() {
        return ourInstance;
    }

    @Override
    public void act() {
        super.act();
        //healthLabel.setText("HEALTH: " + gameStage.getPlayerCharacter().getHealth());
        //shieldLabel.setText("SHIELD: " + gameStage.getPlayerCharacter().getEnergyShield());

    }

    @Override
    public void draw() {
        super.draw();
    }
}
