package com.mageshowdown.gameserver;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;

public class ServerListener extends Listener {
    private ServerGameStage gameStage;

    public ServerListener(ServerGameStage gameStage) {
        super();
        this.gameStage=gameStage;
    }


    @Override
    public void connected(Connection connection) {
        GameWorld.myServer.sendToTCP(connection.getID(),new Network.LoginRequest());
    }

    @Override
    public void received(Connection connection, Object object) {
        handleLoginRequest(connection,object);
        handleMoveKeyDown(connection,object);
        handleShootProjectile(connection,object);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        Network.PlayerDisconnected toBeSent=new Network.PlayerDisconnected();
        toBeSent.id=connection.getID();

        GameWorld.myServer.removeUser(connection.getID());
        gameStage.removePlayerCharacter(connection.getID());

        GameWorld.myServer.sendToAllExceptTCP(connection.getID(),toBeSent);
    }

    private void handleLoginRequest(Connection connection, Object object){
        if(object instanceof Network.LoginRequest) {
            /*
            * when a player connects, we send him a login request
            * when we get that packet back, we add him as an user
            * and tell all players that a new player has logged in by sending their username and spawn point
            */
            Network.LoginRequest packet=(Network.LoginRequest)object;

            GameWorld.myServer.addUser(connection.getID(),packet.user);
            Network.NewPlayerSpawned toBeSent=new Network.NewPlayerSpawned();
            toBeSent.userName=packet.user;
            toBeSent.id=connection.getID();
            toBeSent.pos=new Vector2(500f+(float)Math.random()*(900f-500f),200f+(float)Math.random()*(600f-200f));
            gameStage.addPlayerCharacter(connection.getID(),toBeSent.pos);

            GameWorld.myServer.sendToAllTCP(toBeSent);

            /*
            * for the player that just logged in we also need to send him packets
            * with who was already logged in
             */
            for(Connection con:GameWorld.myServer.getConnections()){
                if(con.getID()!=connection.getID()){
                    toBeSent.userName=GameWorld.myServer.getUserNameById(con.getID());
                    toBeSent.id=con.getID();
                    toBeSent.pos=gameStage.getPlayerById(con.getID()).getBody().getPosition();

                    GameWorld.myServer.sendToTCP(connection.getID(),toBeSent);
                }
            }
        }
    }

    private void handleMoveKeyDown(Connection connection, Object object){
        if(object instanceof Network.MoveKeyDown){
            Network.MoveKeyDown packet=(Network.MoveKeyDown)object;
            GameWorld.myServer.setUpdatePositions(true);

            gameStage.getPlayerById(connection.getID()).setMoveDirection(packet.keycode);
        }
    }

    private void handleKeyUp(Connection connection, Object object){
        if(object instanceof Network.KeyUp){
            GameWorld.myServer.setUpdatePositions(true);
        }
    }

    private void handleShootProjectile(Connection connection, Object object){
        if(object instanceof Network.ShootProjectile){
            Network.ShootProjectile packet=(Network.ShootProjectile)object;

            gameStage.getPlayerById(connection.getID()).getMyWeapon().shoot(packet.dir,packet.rot,connection.getID());
            GameWorld.myServer.sendToAllExceptTCP(connection.getID(),packet);

        }
    }

}
