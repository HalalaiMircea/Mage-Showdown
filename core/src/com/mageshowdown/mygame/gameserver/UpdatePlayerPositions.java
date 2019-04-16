package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.gamelogic.*;


import java.util.ArrayList;

public class UpdatePlayerPositions extends Thread{

    private CharacterLocations loc;
    private Server server;
    private ServerGameStage gameStage;

    UpdatePlayerPositions(Server server, ServerGameStage gameStage){
        loc=new CharacterLocations();
        loc.playersPos=new ArrayList<OneCharacterLocation>();
        this.server=server;
        this.gameStage=gameStage;
        start();
    }

    public void run(){
        for(Connection x:server.getConnections()){
            OneCharacterLocation oneLoc=new OneCharacterLocation();
            ServerPlayerCharacter pc=gameStage.getPlayerById(x.getID());

            oneLoc.linVel=pc.getBody().getLinearVelocity();
            oneLoc.pos=new Vector2(pc.getX(),pc.getY());
            oneLoc.id=x.getID();
            oneLoc.horizontalState=pc.getHorizontalState().getNumValue();
            oneLoc.verticalState=pc.getVerticalState().getNumValue();

            loc.playersPos.add(oneLoc);
        }
        server.sendToAllTCP(loc);
    }
}
