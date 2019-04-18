package com.mageshowdown.gameserver;

import com.esotericsoftware.kryonet.Connection;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;


import java.util.ArrayList;

public class UpdatePlayerPositions extends Thread{

    private Network.CharacterLocations loc;
    private ServerGameStage gameStage;

    UpdatePlayerPositions(ServerGameStage gameStage){
        loc=new Network.CharacterLocations();
        loc.playersPos=new ArrayList<Network.OneCharacterLocation>();
        this.gameStage=gameStage;
        start();
    }

    public void run(){
        for(Connection x: GameWorld.myServer.getConnections()){
            Network.OneCharacterLocation oneLoc=new Network.OneCharacterLocation();
            ServerPlayerCharacter pc=gameStage.getPlayerById(x.getID());

            oneLoc.linVel=pc.getBody().getLinearVelocity();
            oneLoc.pos=pc.getBody().getPosition();
            oneLoc.id=x.getID();

            loc.playersPos.add(oneLoc);
        }
        GameWorld.myServer.sendToAllTCP(loc);
    }
}
