package com.mageshowdown.mygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.mygame.gameserver.MageShowdownServer;

public class DesktopServerLauncher {

    public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    config.width=320;
    config.height=320;

    new LwjglApplication(new MageShowdownServer(), config);
    }
}
