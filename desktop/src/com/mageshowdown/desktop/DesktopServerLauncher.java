package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.mageshowdown.gameserver.MageShowdownServer;

import javax.swing.*;


public class DesktopServerLauncher {

    public static void main(String[] arg) {
        //LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        //config.width = 320;
        //config.height = 320;

        //new LwjglApplication(new MageShowdownServer(), config);
        LwjglFrame kkt = new LwjglFrame(new MageShowdownServer(), "Mage Dedicated Server", 800, 500);
        kkt.add(new JButton("INTERFATA GRAFICA PT SERVER WOW!!!"));
    }
}
