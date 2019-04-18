package com.mageshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.gameclient.MageShowdownClient;

public class DesktopClientLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width=1280;
		config.height=720;
		//config.fullscreen=true;
		//config.foregroundFPS=59;

		new LwjglApplication(new MageShowdownClient(), config);
	}
}
