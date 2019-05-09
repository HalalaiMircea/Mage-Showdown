package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.MageShowdownClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.mageshowdown.desktop.DesktopClientLauncher.config;
import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

public class MainSceneController implements Initializable {

    @FXML
    private Button playButton;
    @FXML
    private Button configButton;
    @FXML
    private Button exitButton;

    @FXML
    void playBtnClicked(ActionEvent actionEvent) {
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

    @FXML
    void configBtnClicked(ActionEvent actionEvent) throws IOException {
        DesktopClientLauncher.mainStage.setScene(new Scene((Parent) FXMLLoader.load(new URL("file", "localhost",
                "../../desktop/src/com/mageshowdown/desktop/resources/ConfigScene.fxml"))));
    }

    @FXML
    void exitBtnClicked(ActionEvent actionEvent) {
        DesktopClientLauncher.mainStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Application started...");
    }
}
