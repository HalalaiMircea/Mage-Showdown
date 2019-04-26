package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class ClientAssetLoader {
    public static Texture solidBlack;
    public static Skin uiSkin;
    public static Texture menuBackground;
    public static Texture idlePlayerSpriteSheet;
    public static Texture jumpingPlayerSpritesheet;
    public static Texture runningPlayerSpritesheet;
    public static Texture crystalSpriteSheet;
    public static Texture laserShotTexture;
    public static BitmapFont font1;

    public static Preferences prefs;

    public static TiledMap map1;
    public static TiledMap dungeonMap;
    public static TiledMap purpleMap;


    public static void load() {
        idlePlayerSpriteSheet = new Texture("idleAnimationSpritesheet.png");
        jumpingPlayerSpritesheet = new Texture("jumpingAnimationSpritesheet.png");
        runningPlayerSpritesheet = new Texture("runningAnimationSpritesheet.png");
        laserShotTexture = new Texture("laser shot3.png");
        uiSkin = new Skin(Gdx.files.internal("UIAssets/uiskin.json"));
        menuBackground = new Texture(Gdx.files.internal("UIAssets/placeholder.jpg"));
        solidBlack = new Texture(Gdx.files.internal("UIAssets/Black_1080p.png"));
        crystalSpriteSheet = new Texture("Crystal.png");
        font1 = new BitmapFont(Gdx.files.internal("UIAssets/default.fnt"));

        map1 = new TmxMapLoader().load(Gdx.files.internal("Maps/level1.tmx").toString());
        dungeonMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level2.tmx").toString());
        purpleMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level3.tmx").toString());
    }


    public static void dispose() {
        uiSkin.dispose();
        menuBackground.dispose();
        solidBlack.dispose();
        idlePlayerSpriteSheet.dispose();
        jumpingPlayerSpritesheet.dispose();
        runningPlayerSpritesheet.dispose();
        crystalSpriteSheet.dispose();
        laserShotTexture.dispose();
        font1.dispose();
        map1.dispose();
        dungeonMap.dispose();
        purpleMap.dispose();
    }
}
