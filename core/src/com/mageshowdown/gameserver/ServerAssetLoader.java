package com.mageshowdown.gameserver;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ServerAssetLoader {
    public static TiledMap map1;
    public static TiledMap dungeonMap;

    public static void load(){
        map1=new TmxMapLoader().load("Maps\\level1.tmx");
        dungeonMap=new TmxMapLoader().load("Maps\\level2.tmx");
    }

    public static void dispose(){
        map1.dispose();
        dungeonMap.dispose();
    }
}
