package com.mageshowdown.packets;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Network {

    public static final int TCP_PORT=1311;
    public static final int UDP_PORT=1333;

    public static class OneCharacterState{
        public Vector2 pos;
        public Vector2 linVel;
        public int id;
        public float health;
        public float energyShield;
        public int score;
        public boolean dmgImmune;
        public boolean frozen;
        public int kills;
    }

    public static class CharacterStates {
        public ArrayList<OneCharacterState> playerStates;
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

    public static class CastSpellProjectile {
        public int id;
        public float rot;
    }

    public static class CastBomb {
        public int id;
        public Vector2 pos;
    }


    public static class LoginRequest{
        public String user;
    }

    public static class NewPlayerSpawned{
        public int id;
        public String userName;
        public Vector2 pos;
        public float roundTimePassed;
        public int orbEquipped;  //1 for frost, 2 for fire
    }

    public static class PlayerDisconnected{
        public int id;
    }

    public static class CurrentMap{
        public int nr;
    }

    public static class PlayerDead{
        public int id;
        public Vector2 respawnPos;
    }

    public static class SwitchOrbs {
        public int id;
    }
}
