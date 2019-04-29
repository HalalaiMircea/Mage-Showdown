package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.MageShowdownClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static com.mageshowdown.desktop.DesktopClientLauncher.config;
import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

class MainSceneBase extends AnchorPane {

    private static final MainSceneBase INSTANCE = new MainSceneBase();

    private final Button playButton;
    private final Button configButton;
    private final Button exitButton;

    private MainSceneBase() {

        VBox vBox = new VBox();
        playButton = new Button();
        configButton = new Button();
        exitButton = new Button();

        setPrefHeight(400.0);
        setPrefWidth(835.0);

        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        vBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        vBox.setLayoutX(735.0);
        vBox.setLayoutY(100.0);
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(118.0);
        vBox.setSpacing(20.0);

        playButton.setMnemonicParsing(false);
        playButton.setText("PLAY");
        playButton.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        playButton.setFont(new Font("System Bold", 16.0));
        playButton.setCursor(Cursor.HAND);

        configButton.setMnemonicParsing(false);
        configButton.setText("CONFIGURE");

        exitButton.setMnemonicParsing(false);
        exitButton.setText("EXIT");
        vBox.setPadding(new Insets(20.0, 20.0, 0.0, 0.0));

        eventHandling();

        vBox.getChildren().add(playButton);
        vBox.getChildren().add(configButton);
        vBox.getChildren().add(exitButton);
        getChildren().add(vBox);

    }

    private void eventHandling() {
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                config = new LwjglApplicationConfiguration();
                config.width = prefs.getInteger("width");
                config.height = prefs.getInteger("height");
                config.resizable = false;
                config.foregroundFPS = prefs.getInteger("foregroundFPS");
                config.backgroundFPS = prefs.getInteger("backgroundFPS");
                config.vSyncEnabled = prefs.getBoolean("vSyncEnabled");
                config.useGL30 = prefs.getBoolean("useGL30");
                config.fullscreen = prefs.getBoolean("fullscreen");
                config.title = "Mage Showdown";


                new LwjglApplication(MageShowdownClient.getInstance(), config);
                DesktopClientLauncher.mainStage.close();
            }
        });
        configButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConfigSceneBase.rezChoiceBox.setValue(prefs.getInteger("width") + "x" + prefs.getInteger("height"));
                if (prefs.getBoolean("fullscreen"))
                    ConfigSceneBase.modeChoiceBox.setValue("Fullscreen");
                else if (!prefs.getBoolean("fullscreen"))
                    ConfigSceneBase.modeChoiceBox.setValue("Windowed");

                ConfigSceneBase.foreFpsField.setText(String.valueOf(prefs.getInteger("foregroundFPS")));
                ConfigSceneBase.backFpsField.setText(String.valueOf(prefs.getInteger("backgroundFPS")));
                ConfigSceneBase.vsyncCheckBox.setSelected(prefs.getBoolean("vSyncEnabled"));
                ConfigSceneBase.glCheckBox.setSelected(prefs.getBoolean("useGL30"));
                DesktopClientLauncher.mainStage.setScene(DesktopClientLauncher.configScene);
            }
        });
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DesktopClientLauncher.mainStage.close();
            }
        });
    }

    static MainSceneBase getInstance() {
        return INSTANCE;
    }
}
