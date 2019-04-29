package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.GamePreferences;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

public class DesktopClientLauncher extends Application {

    static Stage mainStage;
    static Scene mainScene;
    static Scene configScene;
    static LwjglApplicationConfiguration config;

    public static void main(String[] arg) {
        launch(arg);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        prefs = new GamePreferences();
        testStuff();
        mainStage = primaryStage;
        mainStage.setTitle("Mage Showdown Launcher");
        mainStage.setResizable(false);

        mainScene = new Scene(MainSceneBase.getInstance());
        configScene = new Scene(ConfigSceneBase.getInstance());
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    private static void testStuff() {
        //System.out.println(Arrays.toString(LwjglApplicationConfiguration.getDisplayModes()));
    }
}
