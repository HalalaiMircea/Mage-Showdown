package com.mageshowdown.gameserver;

import com.esotericsoftware.kryonet.Connection;
import com.mageshowdown.packets.Network;


import java.util.ArrayList;

public class UpdatePlayerPositions extends Thread {

    private GameServer myServer = GameServer.getInstance();

    private Network.CharacterStates loc;
    private ServerGameStage gameStage;

    UpdatePlayerPositions(ServerGameStage gameStage) {
        loc = new Network.CharacterStates();
        loc.playerStates = new ArrayList<Network.OneCharacterState>();
        this.gameStage = gameStage;
        start();
    }

    public void run() {
        boolean sendPacket = true;
        for (Connection x : myServer.getConnections()) {
            Network.OneCharacterState oneLoc = new Network.OneCharacterState();
            ServerPlayerCharacter pc = gameStage.getPlayerById(x.getID());

            if (pc != null) {
                oneLoc.linVel = pc.getBody().getLinearVelocity();
                oneLoc.pos = pc.getBody().getPosition();
                oneLoc.id = x.getID();
                oneLoc.health = pc.getHealth();
                oneLoc.energyShield = pc.getEnergyShield();
                oneLoc.score = pc.getScore();
                oneLoc.dmgImmune = pc.isDmgImmune();
                oneLoc.frozen = pc.isFrozen();
                oneLoc.kills = pc.getKills();

                loc.playerStates.add(oneLoc);
            } else {
                sendPacket = false;
            }
        }
        if (sendPacket)
            myServer.sendToAllTCP(loc);
    }
}
