package com.mageshowdown.gameserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class ServerAssetLoader {
    public static TiledMap map1;
    public static TiledMap dungeonMap;
    public static TiledMap purpleMap;

    public static void load() {
        map1 = new TmxMapLoader().load(Gdx.files.internal("Maps/level1.tmx").toString());
        dungeonMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level2.tmx").toString());
        purpleMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level3.tmx").toString());
    }

    public static void dispose() {
        map1.dispose();
        dungeonMap.dispose();
    }
}
