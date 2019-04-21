package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class ClientAssetLoader {
    public static Texture solidBlack;
    public static Skin interfaceSkin;
    public static Texture menuBackground;
    public static Texture groundTexture;
    public static Texture borderTexture;
    public static Texture idlePlayerSpriteSheet;
    public static Texture jumpingPlayerSpritesheet;
    public static Texture runningPlayerSpritesheet;
    public static Texture gunTexture;
    public static Texture waterSphereSpriteSheet;
    public static Texture laserShotTexture;
    public static TiledMap map1;

    public static void load() {
        idlePlayerSpriteSheet = new Texture("idleAnimationSpritesheet.png");
        jumpingPlayerSpritesheet = new Texture("jumpingAnimationSpritesheet.png");
        runningPlayerSpritesheet = new Texture("runningAnimationSpritesheet.png");
        laserShotTexture = new Texture("laser shot3.png");
        interfaceSkin = new Skin(Gdx.files.internal("UIAssets/uiskin.json"));
        menuBackground = new Texture(Gdx.files.internal("UIAssets/placeholder.jpg"));
        solidBlack = new Texture(Gdx.files.internal("UIAssets/Black_1080p.png"));
        groundTexture = new Texture("ground.png");
        borderTexture = new Texture("border.png");
        gunTexture = new Texture("gun.png");
        waterSphereSpriteSheet = new Texture("water sphere 6.png");
        map1 = new TmxMapLoader().load("Maps\\level1.tmx");
    }

    public static void dispose() {
        interfaceSkin.dispose();
        menuBackground.dispose();
        solidBlack.dispose();
        groundTexture.dispose();
        borderTexture.dispose();
        idlePlayerSpriteSheet.dispose();
        jumpingPlayerSpritesheet.dispose();
        runningPlayerSpritesheet.dispose();
        gunTexture.dispose();
        waterSphereSpriteSheet.dispose();
        laserShotTexture.dispose();
        map1.dispose();
    }
}
