package com.mageshowdown.mygame.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mageshowdown.mygame.*;
import com.mageshowdown.mygame.packets.*;

import java.io.IOException;

public class MageShowdownServer extends Game {
    private Server server;

    @Override
    public void create () {
        serverStart();
    }

    @Override
    public void render () {
    }

    @Override
    public void dispose () {
    }

    public void serverStart(){
        server=new Server();
        server.start();
        Kryo kryo=server.getKryo();
        kryo.register(MoveKeyDown.class);
        kryo.register(MoveKeyUp.class);
        kryo.register(CanMoveLeft.class);
        kryo.register(CanMoveRight.class);
        kryo.register(CanJump.class);

        try {
            server.bind(1311, 1333);
        }catch(IOException e){
            System.out.println("na bueno no conexiona");
        }

        server.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.println(connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof MoveKeyDown){
                    switch(((MoveKeyDown) object).keycode){
                        case Input.Keys.A:
                            server.sendToUDP(connection.getID(),new CanMoveLeft());
                            break;
                        case Input.Keys.D:
                            server.sendToUDP(connection.getID(),new CanMoveRight());
                            break;
                        case Input.Keys.W:
                            server.sendToUDP(connection.getID(),new CanJump());
                            break;
                    }

                }else if (object instanceof MoveKeyUp){

                    switch(((MoveKeyUp) object).keycode){
                        case Input.Keys.A:
                            CanMoveRight cmr=new CanMoveRight();
                            cmr.ok=false;
                            server.sendToUDP(connection.getID(),cmr);
                            break;
                        case Input.Keys.D:
                            CanMoveLeft cml=new CanMoveLeft();
                            cml.ok=false;
                            server.sendToUDP(connection.getID(),cml);
                            break;
                        case Input.Keys.W:
                            CanJump cj=new CanJump();
                            cj.ok=false;
                            server.sendToUDP(connection.getID(),cj);
                            break;
                    }
                }
            }
        });

    }

}
