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
    public static Texture waterSphereSpriteSheet;
    public static Texture laserShotTexture;
    public static BitmapFont font1;

    public static Preferences prefs;

    public static TiledMap map1;
    public static TiledMap dungeonMap;


    public static void load() {
        idlePlayerSpriteSheet = new Texture("idleAnimationSpritesheet.png");
        jumpingPlayerSpritesheet = new Texture("jumpingAnimationSpritesheet.png");
        runningPlayerSpritesheet = new Texture("runningAnimationSpritesheet.png");
        laserShotTexture = new Texture("laser shot3.png");
        uiSkin = new Skin(Gdx.files.internal("UIAssets/uiskin.json"));
        menuBackground = new Texture(Gdx.files.internal("UIAssets/placeholder.jpg"));
        solidBlack = new Texture(Gdx.files.internal("UIAssets/Black_1080p.png"));
        waterSphereSpriteSheet = new Texture("water sphere 6.png");
        map1 = new TmxMapLoader().load("Maps\\level1.tmx");
        font1=new BitmapFont(Gdx.files.internal("UIAssets/default.fnt"));
        prefs = Gdx.app.getPreferences("MageShowdownPrefs");

        dungeonMap=new TmxMapLoader().load("Maps\\level2.tmx");
    }


    public static void dispose() {
        uiSkin.dispose();
        menuBackground.dispose();
        solidBlack.dispose();
        idlePlayerSpriteSheet.dispose();
        jumpingPlayerSpritesheet.dispose();
        runningPlayerSpritesheet.dispose();
        waterSphereSpriteSheet.dispose();
        laserShotTexture.dispose();
        map1.dispose();
        font1.dispose();
        dungeonMap.dispose();
    }
}
