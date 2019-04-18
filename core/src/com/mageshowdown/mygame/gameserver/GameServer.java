package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.packets.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer extends Server {

    //a hashmap where the values are the usernames and the keys the id's of the player
    private HashMap<Integer,String> users;
    private boolean updatePositions=false;
    private float timeSinceLastUpdate=0;

    public GameServer(){
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
        kryo.register(Network.OneCharacterLocation.class);
        kryo.register(Network.CharacterLocations.class);
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
}
