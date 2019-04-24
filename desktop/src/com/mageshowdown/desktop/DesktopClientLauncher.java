package com.mageshowdown.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;

public class DesktopClientLauncher {
    public static void main(String[] arg) {
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
        new LwjglApplication(MageShowdownClient.getInstance(), config);
    }

    private static void testStuff() {
        Graphics.DisplayMode[] kek = LwjglApplicationConfiguration.getDisplayModes();
        for (Graphics.DisplayMode each : kek) {
            System.out.println(each.toString());
        }
    }
}
