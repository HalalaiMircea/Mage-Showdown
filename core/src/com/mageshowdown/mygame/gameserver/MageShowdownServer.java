package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.gameclient.ClientAssetLoader;
import com.mageshowdown.mygame.gamelogic.*;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.packets.Network;

import java.io.IOException;
import java.util.ArrayList;

public class MageShowdownServer extends Game {
    private ServerGameStage gameStage;
    private boolean updatePositions=false;

    @Override
    public void create () {
        GameWorld.resolutionScale=1f;
        gameStage=new ServerGameStage();
        serverStart();
        ServerAssetLoader.load();
        ClientAssetLoader.load();
        gameStage.start();
    }

    @Override
    public void render () {
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond()+" ");
        if(updatePositions)
        {
            new UpdatePlayerPositions(GameWorld.myServer,gameStage);
            updatePositions=false;
        }

        gameStage.act();
        GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
    }

    @Override
    public void dispose () {
        ServerAssetLoader.dispose();
    }

    public void serverStart(){
        GameWorld.myServer=new Server();
        GameWorld.myServer.start();
        Kryo kryo=GameWorld.myServer.getKryo();
        kryo.register(OneCharacterLocation.class);
        kryo.register(CharacterLocations.class);
        kryo.register(PlayerConnected.class);
        kryo.register(Vector2.class);
        kryo.register(UpdatePositions.class);
        kryo.register(ArrayList.class);
        kryo.register(MoveKeyDown.class);
        kryo.register(KeyUp.class);
        kryo.register(ShootProjectile.class);
        kryo.register(ProjectileCollided.class);


        try {
            GameWorld.myServer.bind(Network.TCP_PORT, Network.UDP_PORT);
        }catch(IOException e){
            System.out.println("na bueno no conexiona");
        }

        GameWorld.myServer.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                /*
                * when a new player connects, we send that player its spawn location
                * and update everyone's positions
                */
                PlayerConnected x=new PlayerConnected();
                x.id=connection.getID();
                x.spawnLocation=new Vector2(500f+(float)Math.random()*(900f-500f),200f+(float)Math.random()*(600f-200f));
                gameStage.addPlayerCharacter(connection.getID(),x.spawnLocation);
                GameWorld.myServer.sendToTCP(connection.getID(),x);

                updatePositions=true;
            }

            @Override
            public void disconnected(Connection connection) {
                gameStage.removePlayerCharacter(connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
                updatePositions=true;
                /*
                * we will only send updates about player positions whenever we receive a packet
                * that signifies an input
                 */
                if(object instanceof MoveKeyDown){
                    gameStage.getPlayerById(connection.getID()).setMoveDirection(((MoveKeyDown) object).keycode);
                    updatePositions=true;
                }else if(object instanceof KeyUp){
                    updatePositions=true;
                }else if(object instanceof ShootProjectile){
                    GameWorld.myServer.sendToAllExceptTCP(connection.getID(),object);
                    gameStage.getPlayerById(connection.getID()).getMyWeapon().shoot(((ShootProjectile) object).dir,((ShootProjectile) object).rot,connection.getID());
                }

            }
        });

    }

}
