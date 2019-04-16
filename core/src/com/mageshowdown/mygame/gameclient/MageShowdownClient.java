package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mageshowdown.mygame.packets.Network;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.gamelogic.*;


import java.io.IOException;
import java.util.ArrayList;

public class MageShowdownClient extends Game {
	private GameScreen gameScreen;


	@Override
	public void create () {
		GameWorld.setResolutionScale(Gdx.graphics.getWidth()/1280f);
		ClientAssetLoader.load();

		gameScreen=new GameScreen(this);

		clientStart();
	}

	@Override
	public void render () {
		super.render();

		Gdx.graphics.setTitle(GameWorld.getMousePos()+" ");

		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			gameScreen.start();
			this.setScreen(gameScreen);
		}
		//GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
	}

	@Override
	public void dispose () {
		ClientAssetLoader.dispose();
		gameScreen.dispose();
	}


	public void clientStart(){
		GameWorld.myClient=new Client();
		GameWorld.myClient.start();
		Kryo kryo=GameWorld.myClient.getKryo();
		kryo.register(OneCharacterLocation.class);
		kryo.register(CharacterLocations.class);
		kryo.register(PlayerConnected.class);
		kryo.register(Vector2.class);
		kryo.register(UpdatePositions.class);
		kryo.register(ArrayList.class);
		kryo.register(KeyPress.class);


		try {
			GameWorld.myClient.connect(5000, "192.168.0.108", Network.TCP_PORT, Network.UDP_PORT);
		}catch(IOException e){
			System.out.println("na bueno no conexiona");
		}

		GameWorld.myClient.addListener(new Listener(){
			@Override
			public void connected(Connection connection) {
			}

			@Override
			public void received(Connection connection, Object object) {
				if(object instanceof CharacterLocations){
					for(OneCharacterLocation x:((CharacterLocations)object).playersPos){
						if(x.id==connection.getID()){
							ClientPlayerCharacter pc=gameScreen.getGameStage().getPlayerCharacter();
							pc.setPosition(x.pos);
							pc.setHorizontalState(DynamicGameActor.HorizontalState.valueOf(x.horizontalState));
							pc.setVerticalState(DynamicGameActor.VerticalState.valueOf(x.verticalState));
						}else{
							ClientPlayerCharacter currPlayer=gameScreen.getGameStage().getOtherPlayers().get(x.id);
							if(currPlayer!=null) {
								currPlayer.setPosition(x.pos);
								currPlayer.setHorizontalState(DynamicGameActor.HorizontalState.valueOf(x.horizontalState));
								currPlayer.setVerticalState(DynamicGameActor.VerticalState.valueOf(x.verticalState));
							}else{
								gameScreen.getGameStage().spawnOtherPlayer(x.id,x.pos);
							}
						}
					}
				}
				if(object instanceof PlayerConnected){
					gameScreen.getGameStage().spawnMyPlayerCharacter(((PlayerConnected) object).spawnLocation);
					//gameScreen.getGameStage().getPlayerCharacter().setMyPlayer(true);
				}
				if(object instanceof OneCharacterLocation){
					if(connection.getID()==((OneCharacterLocation) object).id)
						gameScreen.getGameStage().getPlayerCharacter().setPosition(((OneCharacterLocation) object).pos);
					else gameScreen.getGameStage().getOtherPlayers().get(((OneCharacterLocation) object).id).setPosition(((OneCharacterLocation) object).pos);
				}
			}
		});
	}
}
