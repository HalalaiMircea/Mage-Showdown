package com.mageshowdown.mygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.mygame.gameclient.MageShowdownClient;

public class DesktopClientLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width=1920;
		config.height=1080;
		config.fullscreen=true;

		new LwjglApplication(new MageShowdownClient(), config);
	}
}
