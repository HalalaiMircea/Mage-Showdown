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

    //player animations
    public static Texture friendlyIdleSpritesheet;
    public static Texture enemyIdleSpritesheet;
    public static Texture friendlyJumpingSpritesheet;
    public static Texture enemyJumpingSpritesheet;
    public static Texture friendlyRunningSpritesheet;
    public static Texture enemyRunningSpritesheet;


    public static Texture crystalSpritesheet;
    public static Texture freezeProjectileTexture;
    public static Texture sphereSpriteSheet;
    public static Texture fireLaserSpritesheet;

    public static Texture energyShieldSpritesheet;
    public static Texture frozenSpritesheet;
    public static BitmapFont font1;
    public static Texture roundOverScreen;

    public static Preferences prefs;

    public static TiledMap map1;
    public static TiledMap dungeonMap;
    public static TiledMap purpleMap;


    public static void load() {
        friendlyIdleSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/idleAnimationSpritesheet.png"));
        friendlyJumpingSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/jumpingAnimationSpritesheet.png"));
        friendlyRunningSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/runningAnimationSpritesheet.png"));

        enemyIdleSpritesheet=new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png"));
        enemyJumpingSpritesheet=new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png"));
        enemyRunningSpritesheet=new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png"));

        freezeProjectileTexture = new Texture("Player Animations/Ammo/freezeProjectile.png");
        crystalSpritesheet = new Texture("Player Animations/Weapon/CrystalSpritesheet.png");
        fireLaserSpritesheet=new Texture(Gdx.files.internal("Player Animations/Ammo/fireLaserSpritesheet.png"));
        sphereSpriteSheet=new Texture(Gdx.files.internal("Player Animations/Weapon/sphereSpritesheet.png"));

        energyShieldSpritesheet=new Texture(Gdx.files.internal("energyShieldSpriteSheet.png"));
        frozenSpritesheet=new Texture(Gdx.files.internal("frozenSpriteSheet.png"));
        uiSkin = new Skin(Gdx.files.internal("UIAssets/uiskin.json"));
        menuBackground = new Texture(Gdx.files.internal("UIAssets/placeholder.jpg"));
        solidBlack = new Texture(Gdx.files.internal("UIAssets/Black_1080p.png"));
        font1 = new BitmapFont(Gdx.files.internal("UIAssets/default.fnt"));
        roundOverScreen=new Texture(Gdx.files.internal("round over.png"));

        map1 = new TmxMapLoader().load(Gdx.files.internal("Maps/level1.tmx").toString());
        dungeonMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level2.tmx").toString());
        purpleMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level3.tmx").toString());
    }


    public static void dispose() {
        uiSkin.dispose();
        menuBackground.dispose();
        solidBlack.dispose();

        friendlyIdleSpritesheet.dispose();
        friendlyJumpingSpritesheet.dispose();
        friendlyRunningSpritesheet.dispose();

        enemyIdleSpritesheet.dispose();
        enemyJumpingSpritesheet.dispose();
        enemyRunningSpritesheet.dispose();

        crystalSpritesheet.dispose();
        sphereSpriteSheet.dispose();
        freezeProjectileTexture.dispose();
        fireLaserSpritesheet.dispose();

        font1.dispose();
        map1.dispose();
        dungeonMap.dispose();
        purpleMap.dispose();
        energyShieldSpritesheet.dispose();
        frozenSpritesheet.dispose();
    }
}
