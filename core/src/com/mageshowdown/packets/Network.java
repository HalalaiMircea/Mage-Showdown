package com.mageshowdown.packets;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Network {

    public final static int TCP_PORT=1311;
    public final static int UDP_PORT=1333;

    public static class OneCharacterLocation{
        public Vector2 pos;
        public Vector2 linVel;
        public int id;
    }

    public static class CharacterLocations {
        public ArrayList<OneCharacterLocation> playersPos;
    }

    public static class PlayerConnected {
        public Vector2 spawnLocation;
    }

    public static class UpdatePositions {
        public boolean ok=true;
    }

    public static class MoveKeyDown {
        public int keycode;
    }

    public static class KeyUp{
        public int keycode;
    }

    public static class ShootProjectile{
        public int id;
        public float rot;
        public Vector2 dir;
    }

    public static class ProjectileCollided{
        public int projId;
        public int ownerId;
        public int playerHitId;
    }

    public static class LoginRequest{
        public String user;
    }

    public static class NewPlayerSpawned{
        public int id;
        public String userName;
        public Vector2 pos;
        public float roundTimePassed;
    }

    public static class PlayerDisconnected{
        public int id;
    }

}
