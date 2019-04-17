package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    private MainMenuScreen mainMenuScreen;


    @Override
    public void create() {
        GameWorld.setResolutionScale(Gdx.graphics.getWidth() / 1280f);
        ClientAssetLoader.load();

        gameScreen = new GameScreen(this);
        mainMenuScreen = new MainMenuScreen(this, gameScreen);
        this.setScreen(mainMenuScreen);


    }

    @Override
    public void render() {
        super.render();

        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + " ");
    }

    @Override
    public void dispose() {
        ClientAssetLoader.dispose();
        gameScreen.dispose();
        mainMenuScreen.dispose();
    }


    public void clientStart(String ipAddress) {
        GameWorld.myClient = new Client();
        GameWorld.myClient.start();
        Kryo kryo = GameWorld.myClient.getKryo();
        kryo.register(OneCharacterLocation.class);
        kryo.register(CharacterLocations.class);
        kryo.register(PlayerConnected.class);
        kryo.register(Vector2.class);
        kryo.register(UpdatePositions.class);
        kryo.register(ArrayList.class);
        kryo.register(KeyDown.class);
        kryo.register(KeyUp.class);

        try {
            GameWorld.myClient.connect(5000, ipAddress, Network.TCP_PORT, Network.UDP_PORT);
        } catch (IOException e) {
            System.out.println("not good no connection...");
        }

        GameWorld.myClient.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof CharacterLocations) {
                    /*
                    * In order to make sure the box2d world doesnt get locked when we synchronize the
                    * velocity and position of a body, we have to queue these assignments
                    * and do them after the world has stepped;
                     */
                    for (OneCharacterLocation x : ((CharacterLocations) object).playersPos) {
                        if (x.id == connection.getID()) {
                            ClientPlayerCharacter pc = gameScreen.getGameStage().getPlayerCharacter();
                            pc.setQueuedPos(x.pos);
                            pc.setQueuedVel(x.linVel);
                        } else {
                            ClientPlayerCharacter currPlayer = gameScreen.getGameStage().getOtherPlayers().get(x.id);
                            if (currPlayer != null) {
                                currPlayer.setQueuedPos(x.pos);
                                currPlayer.setQueuedVel(x.linVel);
                            } else {
                                gameScreen.getGameStage().spawnOtherPlayer(x.id, x.pos);
                            }
                        }
                    }
                }
                if (object instanceof PlayerConnected) {
                    gameScreen.getGameStage().spawnMyPlayerCharacter(((PlayerConnected) object).spawnLocation);
                    gameScreen.getGameStage().getPlayerCharacter().setMyPlayer(true);
                }
                if (object instanceof OneCharacterLocation) {
                    if (connection.getID() == ((OneCharacterLocation) object).id)
                        gameScreen.getGameStage().getPlayerCharacter().setPosition(((OneCharacterLocation) object).pos);
                    else
                        gameScreen.getGameStage().getOtherPlayers().get(((OneCharacterLocation) object).id).setPosition(((OneCharacterLocation) object).pos);
                }
            }
        });
    }
}
