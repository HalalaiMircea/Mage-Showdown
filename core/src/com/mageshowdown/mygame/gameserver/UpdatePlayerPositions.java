package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.packets.Network.*;
import com.mageshowdown.mygame.gamelogic.*;


import java.util.ArrayList;

public class UpdatePlayerPositions extends Thread{

    private CharacterLocations loc;
    private ServerGameStage gameStage;

    UpdatePlayerPositions(ServerGameStage gameStage){
        loc=new CharacterLocations();
        loc.playersPos=new ArrayList<OneCharacterLocation>();
        this.gameStage=gameStage;
        start();
    }

    public void run(){
        for(Connection x:GameWorld.myServer.getConnections()){
            OneCharacterLocation oneLoc=new OneCharacterLocation();
            ServerPlayerCharacter pc=gameStage.getPlayerById(x.getID());

            oneLoc.linVel=pc.getBody().getLinearVelocity();
            oneLoc.pos=pc.getBody().getPosition();
            oneLoc.id=x.getID();

            loc.playersPos.add(oneLoc);
        }
        GameWorld.myServer.sendToAllTCP(loc);
    }
}
