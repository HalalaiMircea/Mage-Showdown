package com.mageshowdown.gameclient;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mageshowdown.gamelogic.GameScreen;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;

import java.util.Scanner;

public class ClientListener extends Listener {

    private GameClient myClient=GameClient.getInstance();

    private GameScreen gameScreen;

    public ClientListener(GameScreen gameScreen) {
        this.gameScreen=gameScreen;
    }

    @Override
    public void connected(Connection connection) {
    }

    @Override
    public void received(Connection connection, Object object) {
        handleLoginServerRequest(connection,object);
        handleNewPlayerSpawned(connection,object);
        handleUpdateCharacterLocations(connection,object);
        handleShootProjectile(connection,object);
        handleProjectileCollided(connection,object);
        handlePlayerDisconnected(connection,object);
    }

    private void handleLoginServerRequest(Connection connection,Object object){
        if(object instanceof Network.LoginRequest){
            Network.LoginRequest packet=((Network.LoginRequest) object);
            
            packet.user=myClient.getUserName();
            myClient.sendTCP(packet);
        }
    }

    private void handleUpdateCharacterLocations(Connection connection, Object object) {
        if(object instanceof Network.CharacterLocations){
            Network.CharacterLocations packet=((Network.CharacterLocations) object);
            /*
             * In order to make sure the box2d world doesnt get locked when we synchronize the
             * velocity and position of a body, we have to queue these assignments
             * and do them after the world has stepped;
             */
            for(Network.OneCharacterLocation x:packet.playersPos){
                ClientPlayerCharacter pc;
                //if the character is mine i update my position
                if(x.id==connection.getID()){
                    pc=gameScreen.getGameStage().getPlayerCharacter();
                }
                else{
                    pc=gameScreen.getGameStage().getOtherPlayers().get(x.id);
                }

                pc.setQueuedPos(x.pos);
                pc.setQueuedVel(x.linVel);
            }
        }
    }

    private void handleNewPlayerSpawned(Connection connection, Object object){
        if(object instanceof Network.NewPlayerSpawned){
            Network.NewPlayerSpawned packet=(Network.NewPlayerSpawned)object;

            gameScreen.getGameStage().getRound().setTimePassed(packet.roundTimePassed);
            if(connection.getID()==packet.id)
                gameScreen.getGameStage().spawnMyPlayerCharacter(packet.pos,packet.userName);
            else gameScreen.getGameStage().spawnOtherPlayer(packet.id,packet.pos,packet.userName);
        }
    }

    private void handleShootProjectile(Connection connection, Object object){
        if(object instanceof Network.ShootProjectile){
            Network.ShootProjectile packet=(Network.ShootProjectile)object;

            gameScreen.getGameStage().getOtherPlayers().get(packet.id).getMyWeapon().shoot(packet.dir,packet.rot,packet.id);
        }
    }

    private void handleProjectileCollided(Connection connection, Object object){
        if(object instanceof Network.ProjectileCollided){
            Network.ProjectileCollided packet=(Network.ProjectileCollided)object;

            //if the bullet is the client's player's
            if(packet.ownerId==connection.getID()){
                gameScreen.getGameStage().getPlayerCharacter().getMyWeapon().projectileHasCollided(packet.projId);
            }
            //if the bullet is some other client's player's
            else{
                gameScreen.getGameStage().getOtherPlayers().get(packet.ownerId).getMyWeapon().projectileHasCollided(packet.projId);
            }
         //   gameScreen.getGameStage().getOtherPlayers().get(packet.playerHitId).damageBy(3);
        }
    }

    private void handlePlayerDisconnected(Connection connection, Object object){
        if(object instanceof Network.PlayerDisconnected){
            System.out.println("dadsadsa");
            Network.PlayerDisconnected packet=(Network.PlayerDisconnected)object;

            gameScreen.getGameStage().removePlayerCharacter(packet.id);
        }
    }
}

