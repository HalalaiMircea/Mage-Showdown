package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClientAssetLoader {
    public static Texture solidBlack;
    public static Skin uiSkin;
    public static Texture menuBackground;
    //fonts
    public static BitmapFont font1;
    public static BitmapFont normalSizeFont;
    public static BitmapFont bigSizeFont;
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
    public static Texture roundOverScreen;
    //preferences files
    public static Preferences prefs;
    //maps
    public static TiledMap map1;
    public static TiledMap dungeonMap;
    public static TiledMap purpleMap;
    private static FreeTypeFontGenerator retroFontGen;

    public static void load() {
        friendlyIdleSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/idleAnimationSpritesheet.png"));
        friendlyJumpingSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/jumpingAnimationSpritesheet.png"));
        friendlyRunningSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/runningAnimationSpritesheet.png"));

        enemyIdleSpritesheet = new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png"));
        enemyJumpingSpritesheet = new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png"));
        enemyRunningSpritesheet = new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png"));

        freezeProjectileTexture = new Texture("Player Animations/Ammo/freezeProjectile.png");
        crystalSpritesheet = new Texture("Player Animations/Weapon/CrystalSpritesheet.png");
        fireLaserSpritesheet = new Texture(Gdx.files.internal("Player Animations/Ammo/fireLaserSpritesheet.png"));
        sphereSpriteSheet = new Texture(Gdx.files.internal("Player Animations/Weapon/sphereSpritesheet.png"));

        energyShieldSpritesheet = new Texture(Gdx.files.internal("energyShieldSpriteSheet.png"));
        frozenSpritesheet = new Texture(Gdx.files.internal("frozenSpriteSheet.png"));
        uiSkin = new Skin(Gdx.files.internal("UIAssets/uiskin.json"));
        menuBackground = new Texture(Gdx.files.internal("UIAssets/placeholder.jpg"));
        solidBlack = new Texture(Gdx.files.internal("UIAssets/Black_1080p.png"));
        roundOverScreen = new Texture(Gdx.files.internal("round over.png"));

        generateFonts();

        map1 = new TmxMapLoader().load(Gdx.files.internal("Maps/level1.tmx").toString());
        dungeonMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level2.tmx").toString());
        purpleMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level3.tmx").toString());
    }

    private static void generateFonts() {
        font1 = new BitmapFont(Gdx.files.internal("UIAssets/default.fnt"));

        retroFontGen = new FreeTypeFontGenerator(Gdx.files.internal("UIAssets/joystix monospace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 12;
        normalSizeFont = retroFontGen.generateFont(fontParameter);

        fontParameter.size = 24;
        fontParameter.shadowOffsetX = 2;
        fontParameter.shadowOffsetY = 2;
        bigSizeFont = retroFontGen.generateFont(fontParameter);
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

        retroFontGen.dispose();
        font1.dispose();
        normalSizeFont.dispose();
        bigSizeFont.dispose();

        map1.dispose();
        dungeonMap.dispose();
        purpleMap.dispose();
        energyShieldSpritesheet.dispose();
        frozenSpritesheet.dispose();
    }
}
