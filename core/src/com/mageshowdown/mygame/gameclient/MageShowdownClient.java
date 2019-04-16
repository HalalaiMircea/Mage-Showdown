package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mageshowdown.mygame.packets.*;

import java.io.IOException;

public class MageShowdownClient extends Game {
	private GameScreen gameScreen;


	@Override
	public void create () {
		clientStart();
		GameWorld.setResolutionScale(Gdx.graphics.getWidth()/1280f);
		AssetLoader.load();

		gameScreen=new GameScreen(this);
	}

	@Override
	public void render () {
		super.render();

		Gdx.graphics.setTitle(GameWorld.getMousePos()+" ");

		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			gameScreen.start();
			this.setScreen(gameScreen);
		}

		GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
	}

	@Override
	public void dispose () {
		AssetLoader.dispose();
		gameScreen.dispose();
	}


	public void clientStart(){
		GameWorld.myClient=new Client();
		GameWorld.myClient.start();
		Kryo kryo=GameWorld.myClient.getKryo();
		kryo.register(MoveKeyDown.class);
		kryo.register(MoveKeyUp.class);
		kryo.register(CanMoveLeft.class);
		kryo.register(CanMoveRight.class);
		kryo.register(CanJump.class);


		try {
			GameWorld.myClient.connect(5000, "192.168.0.108", 1311, 1333);
		}catch(IOException e){
			System.out.println("na bueno no conexiona");
		}

		GameWorld.myClient.addListener(new Listener(){
			@Override
			public void connected(Connection connection) {
				System.out.println("I HAVE CONNECTED TO THE SERVER");
			}

			@Override
			public void disconnected(Connection connection) {
				System.out.println("I HAVE DISCONNECTED FROM THE SERVER");
			}

			@Override
			public void received(Connection connection, Object object) {
				if(object instanceof CanMoveLeft){
					gameScreen.getGameStage().getPlayerCharacter().setCanMoveLeft(((CanMoveLeft) object).ok);
				}else if(object instanceof CanMoveRight){
					gameScreen.getGameStage().getPlayerCharacter().setCanMoveRight(((CanMoveRight) object).ok);
				}else if(object instanceof CanJump){
					gameScreen.getGameStage().getPlayerCharacter().setCanJump(((CanJump) object).ok);
				}
			}
		});
	}
}
