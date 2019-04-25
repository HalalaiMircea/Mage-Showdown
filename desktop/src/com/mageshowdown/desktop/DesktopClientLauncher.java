package com.mageshowdown.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.GamePreferences;
import com.mageshowdown.gameclient.MageShowdownClient;

import java.io.IOException;

public class DesktopClientLauncher {
    public static void main(String[] arg) throws IOException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        config.foregroundFPS = 0;
        //config.backgroundFPS = 0;
        config.vSyncEnabled = true;
        //config.useGL30=true;
        //config.fullscreen=true;
        //testStuff();
        settingsInterface();
        new LwjglApplication(MageShowdownClient.getInstance(), config);
    }

    private static void testStuff() {
        Graphics.DisplayMode[] kek = LwjglApplicationConfiguration.getDisplayModes();
        for (Graphics.DisplayMode each : kek) {
            System.out.println(each.toString());
        }
    }

    private static void settingsInterface() throws IOException {
        ClientAssetLoader.prefs = new GamePreferences();
        ClientAssetLoader.prefs.putString("PlayerName", "Default");
        ClientAssetLoader.prefs.flush();
    }
}
