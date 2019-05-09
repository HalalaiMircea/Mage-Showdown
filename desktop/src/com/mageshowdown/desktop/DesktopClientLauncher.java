package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.GamePreferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

public class DesktopClientLauncher extends Application {

    static Stage mainStage;
    static Scene mainScene;
    static LwjglApplicationConfiguration config;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        prefs = new GamePreferences();
        mainStage = primaryStage;
        mainStage.setTitle("Mage Showdown Launcher");
        mainStage.setResizable(false);

        mainScene = new Scene((Parent) FXMLLoader.load(new URL("file", "localhost",
                "../../desktop/src/com/mageshowdown/desktop/resources/MainScene.fxml")));

        mainStage.setScene(mainScene);
        mainStage.show();
    }
}
