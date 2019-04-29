package com.mageshowdown.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.GamePreferences;
import com.mageshowdown.gameclient.MageShowdownClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DesktopClientLauncher {
    public static void main(String[] arg) throws IOException {
        final JFrame launcherFrame = new JFrame();
        launcherFrame.setTitle("Mage Showdown Launcher");
        launcherFrame.setVisible(true);
        launcherFrame.setLayout(new BorderLayout());
        launcherFrame.setSize(800, 600);
        final JButton launchButton = new JButton("Launch game!");
        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 1280;
                config.height = 720;
                config.resizable = false;
                config.foregroundFPS = 0;
                //config.backgroundFPS = 0;
                config.vSyncEnabled = true;
                //config.useGL30=true;
                //config.fullscreen = true;
                //testStuff();
                try {
                    ClientAssetLoader.prefs = new GamePreferences();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                new LwjglApplication(MageShowdownClient.getInstance(), config);
                launcherFrame.setVisible(false);
            }
        });
        launcherFrame.add(launchButton);
    }

    private static void testStuff() {
        Graphics.DisplayMode[] kek = LwjglApplicationConfiguration.getDisplayModes();
        for (Graphics.DisplayMode each : kek) {
            System.out.println(each.toString());
        }
    }

    public static void settingsInterface() throws IOException {
        ClientAssetLoader.prefs = new GamePreferences();
    }
}
