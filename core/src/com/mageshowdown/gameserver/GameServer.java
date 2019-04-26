package com.mageshowdown.gameserver;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameServer extends Server {

    private static GameServer instance=new GameServer();

    //a hashmap where the values are the usernames and the keys the id's of the player
    private HashMap<Integer,String> users;
    private boolean updatePositions=false;
    private float timeSinceLastUpdate=0;
    private ServerGameStage gameStage;

    private GameServer(){
        super();
        registerClasses();
        users=new HashMap<Integer, String>();
    }


    @Override
    public void bind(int tcpPort, int udpPort){
        try{
            super.bind(tcpPort, udpPort);
        }catch(IOException e){
            System.out.println("Couldnt start to the server");
        }
    }

    private void registerClasses(){
        Kryo kryo = getKryo();
        kryo.register(Network.OneCharacterState.class);
        kryo.register(Network.CharacterStates.class);
        kryo.register(Network.PlayerConnected.class);
        kryo.register(Vector2.class);
        kryo.register(Network.UpdatePositions.class);
        kryo.register(ArrayList.class);
        kryo.register(Network.MoveKeyDown.class);
        kryo.register(Network.KeyUp.class);
        kryo.register(Network.ShootProjectile.class);
        kryo.register(Network.ProjectileCollided.class);
        kryo.register(Network.LoginRequest.class);
        kryo.register(Network.NewPlayerSpawned.class);
        kryo.register(Network.PlayerDisconnected.class);
        kryo.register(Network.CurrentMap.class);
        kryo.register(Network.PlayerDead.class);
    }

    public void sendMapChange(int nr){
        Network.CurrentMap mapToBeSent=new Network.CurrentMap();
        mapToBeSent.nr=nr;
        gameStage.getGameLevel().setMap(nr);
        gameStage.getGameLevel().changeLevel();

        for(Connection x:getConnections()){
            gameStage.getPlayerById(x.getID()).setQueuedPos(generateSpawnPoint(x.getID()));
        }

        GameServer.getInstance().sendToAllTCP(mapToBeSent);
    }

    public Vector2 generateSpawnPoint(int id){
        ArrayList<Vector2> spawnPoints=gameStage.getGameLevel().getSpawnPoints();
        Vector2 spawnPoint=new Vector2(spawnPoints.get(new Random().nextInt(spawnPoints.size())));

        //before we change the body's position to a spawn point it needs to be converted to box2d coordinates
        return GameWorld.convertPixelsToWorld(spawnPoint);
    }

    public HashMap<Integer, String> getUsers() {
        return users;
    }

    public void addUser(int id, String userName){
        users.put(id, userName);
    }

    public void removeUser(int id){
        users.remove(id);
    }

    public String getUserNameById(int id){
        return users.get(id);
    }

    public void setUpdatePositions(boolean updatePositions) {
        this.updatePositions = updatePositions;
    }

    public boolean getUpdatePositions(){
        return updatePositions;
    }

    public static GameServer getInstance() {
        return instance;
    }

    public void setGameStage(ServerGameStage gameStage) {
        this.gameStage = gameStage;
    }
}
