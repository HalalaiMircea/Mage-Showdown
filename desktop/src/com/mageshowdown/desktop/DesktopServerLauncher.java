package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameserver.MageShowdownServer;


public class DesktopServerLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.allowSoftwareMode = true;
        config.width = 320;
        config.height = 320;

        new LwjglApplication(new MageShowdownServer(), config);
    }
}
