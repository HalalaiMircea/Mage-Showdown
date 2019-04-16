package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.gamelogic.*;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.packets.Network;

import java.io.IOException;
import java.util.ArrayList;

public class MageShowdownServer extends Game {
    private Server server;
    private ServerGameStage gameStage;

    @Override
    public void create () {
        gameStage=new ServerGameStage();
        serverStart();
        ServerAssetLoader.load();
        gameStage.start();
    }

    @Override
    public void render () {
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond()+" ");
        new UpdatePlayerPositions(server,gameStage);
        gameStage.act();
        GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
    }

    @Override
    public void dispose () {
        ServerAssetLoader.dispose();
    }

    public void serverStart(){
        server=new Server();
        server.start();
        Kryo kryo=server.getKryo();
        kryo.register(OneCharacterLocation.class);
        kryo.register(CharacterLocations.class);
        kryo.register(PlayerConnected.class);
        kryo.register(Vector2.class);
        kryo.register(UpdatePositions.class);
        kryo.register(ArrayList.class);
        kryo.register(KeyPress.class);


        try {
            server.bind(Network.TCP_PORT, Network.UDP_PORT);
        }catch(IOException e){
            System.out.println("na bueno no conexiona");
        }

        server.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                /*
                * when a new player connects, we send that player its spawn location
                */
                PlayerConnected x=new PlayerConnected();
                x.id=connection.getID();
                x.spawnLocation=new Vector2(500f+(float)Math.random()*(900f-500f),200f+(float)Math.random()*(600f-200f));
                gameStage.addPlayerCharacter(connection.getID(),x.spawnLocation);
                server.sendToTCP(connection.getID(),x);
                /*
                for(Connection con:server.getConnections()){
                    if(con.getID()!=connection.getID()){
                        server.sendToTCP(con.getID(),x);
                    }
               }*/
            }

            @Override
            public void disconnected(Connection connection) {
                gameStage.removePlayerCharacter(connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof KeyPress){
                    gameStage.getPlayerById(connection.getID()).setMoveDirection(((KeyPress) object).keycode);
                }

            }
        });

    }

}
