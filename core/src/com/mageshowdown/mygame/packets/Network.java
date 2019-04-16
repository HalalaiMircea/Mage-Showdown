package com.mageshowdown.mygame.packets;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Network {

    public final static int TCP_PORT=1311;
    public final static int UDP_PORT=1333;

    public static class OneCharacterLocation{
        public int horizontalState;
        public int verticalState;
        public Vector2 pos;
        public Vector2 linVel;
        public int id;
    }

    public static class CharacterLocations {
        public ArrayList<OneCharacterLocation> playersPos;
    }

    public static class PlayerConnected {
        public int id;
        public Vector2 spawnLocation;
    }

    public static class UpdatePositions {
        public boolean ok=true;
    }

    public static class KeyPress{
        public int keycode;
    }
}
