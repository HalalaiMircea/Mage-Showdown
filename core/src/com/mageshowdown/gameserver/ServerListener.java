package com.mageshowdown.gameserver;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;

public class ServerListener extends Listener {

    private GameServer myServer=GameServer.getInstance();

    private ServerGameStage gameStage;

    public ServerListener(ServerGameStage gameStage) {
        super();
        this.gameStage=gameStage;
    }


    @Override
    public void connected(Connection connection) {
        myServer.sendToTCP(connection.getID(),new Network.LoginRequest());
    }

    @Override
    public void received(Connection connection, Object object) {
        handleLoginRequest(connection,object);
        handleMoveKeyDown(connection,object);
        handleShootProjectile(connection,object);
        handleSwitchWeapons(connection,object);
        handlePlantBomb(connection,object);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        Network.PlayerDisconnected toBeSent=new Network.PlayerDisconnected();
        toBeSent.id=connection.getID();

        myServer.removeUser(connection.getID());
        gameStage.removePlayerCharacter(connection.getID());

        myServer.sendToAllExceptTCP(connection.getID(),toBeSent);
    }

    private void handleLoginRequest(Connection connection, Object object){
        if(object instanceof Network.LoginRequest) {
            /*
            * when a player connects, we send him a login request
            * when we get that packet back, we add him as an user, send him the current map
            * and tell all players that a new player has logged in by sending their username and spawn point
            */
            Network.LoginRequest packet=(Network.LoginRequest)object;

            myServer.addUser(connection.getID(),packet.user);
            Network.NewPlayerSpawned toBeSent=new Network.NewPlayerSpawned();
            toBeSent.userName=packet.user;
            toBeSent.id=connection.getID();
            toBeSent.pos=GameWorld.convertWorldToPixels(myServer.generateSpawnPoint(connection.getID()));
            toBeSent.roundTimePassed=ServerRound.getInstance().getTimePassed();
            toBeSent.weaponEquipped=1;//players always start with the freeze weapon equipped
            gameStage.addPlayerCharacter(toBeSent);

            myServer.sendToAllTCP(toBeSent);

            Network.CurrentMap mapToBeSent=new Network.CurrentMap();

            mapToBeSent.nr=gameStage.getGameLevel().getMapNr();
            myServer.sendToTCP(connection.getID(),mapToBeSent);
            /*
            * for the player that just logged in we also need to send him packets
            * with who was already logged in
             */
            System.out.println(myServer.getConnections().length);
            for(Connection con:myServer.getConnections()){
                if(con.getID()!=connection.getID()){
                    toBeSent.userName=myServer.getUserNameById(con.getID());
                    toBeSent.id=con.getID();
                    toBeSent.pos= GameWorld.convertPixelsToWorld(gameStage.getPlayerById(con.getID()).getBody().getPosition());
                    toBeSent.weaponEquipped=gameStage.getPlayerById(con.getID()).getWeaponEquipped();

                    myServer.sendToTCP(connection.getID(),toBeSent);
                }
            }
        }
    }

    private void handleMoveKeyDown(Connection connection, Object object){
        if(object instanceof Network.MoveKeyDown){
            Network.MoveKeyDown packet=(Network.MoveKeyDown)object;
            myServer.setUpdatePositions(true);

            gameStage.getPlayerById(connection.getID()).setMoveDirection(packet.keycode);
        }
    }

    private void handleKeyUp(Connection connection, Object object){
        if(object instanceof Network.KeyUp){
            myServer.setUpdatePositions(true);
        }
    }

    private void handleShootProjectile(Connection connection, Object object){
        if(object instanceof Network.ShootProjectile){
            Network.ShootProjectile packet=(Network.ShootProjectile)object;

            gameStage.getPlayerById(connection.getID()).shootProjectile(packet);
        }
    }

    private void handlePlantBomb(Connection connection, Object object){
        if(object instanceof Network.PlantBomb){
            Network.PlantBomb packet=(Network.PlantBomb)object;

            gameStage.getPlayerById(connection.getID()).plantBomb(packet);
        }
    }

    private void handleSwitchWeapons(Connection connection, Object object){
        if(object instanceof Network.SwitchWeapons){
            Network.SwitchWeapons packet=(Network.SwitchWeapons)object;

            packet.id=connection.getID();
            gameStage.getPlayerById(packet.id).switchMyWeapons();
            myServer.sendToAllExceptTCP(connection.getID(),packet);
        }
    }


}
