package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class ClientAssetLoader {

    //private static ClientAssetLoader ourInstance = new ClientAssetLoader();

    //public AssetManager manager;
    public static Texture solidBlack;
    public static Skin uiSkin;
    public static Skin hudSkin;
    public static Texture menuBackground;
    //fonts
    //public static BitmapFont font1;
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
    public static Texture freezeProjectileSpritesheet;
    public static Texture freezeProjectileImpactSpritesheet;
    public static Texture freezeBombSpritesheet;
    public static Texture armFreezeBombSpritesheet;
    public static Texture armFireBombSpritesheet;
    public static Texture fireBombSpritesheet;
    public static Texture flameSpritesheet;
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

//    private ClientAssetLoader() {
//        manager = new AssetManager();
//        System.out.println("ASSET MANAGER INSTANTIATED");
//    }

    public static void load() {
//        manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
//        manager.load("Player Animations/Friendly player/idleAnimationSpritesheet.png", Texture.class);
//        manager.load("Player Animations/Friendly player/jumpingAnimationSpritesheet.png", Texture.class);
//        manager.load("Player Animations/Friendly player/runningAnimationSpritesheet.png", Texture.class);
//
//        manager.load("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png", Texture.class);
//        manager.load("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png", Texture.class);
//        manager.load("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png", Texture.class);
//
//        manager.load("Player Animations/Ammo/freezeProjectile.png", Texture.class);
//        manager.load("Player Animations/Weapon/CrystalSpritesheet.png", Texture.class);
//        manager.load("Player Animations/Ammo/fireLaserSpritesheet.png", Texture.class);
//        manager.load("Player Animations/Weapon/sphereSpritesheet.png", Texture.class);
//
//        manager.load("energyShieldSpriteSheet.png", Texture.class);
//        manager.load("frozenSpriteSheet.png", Texture.class);
//
//        manager.load("UIAssets/placeholder.jpg", Texture.class);
//        manager.load("UIAssets/Black_1080p.png", Texture.class);
//        manager.load("round over.png", Texture.class);
//
//        manager.load("UIAssets/uiskin.json", Skin.class);
//
//        generateFonts();
//
//        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
//        manager.load("Maps/level1.tmx", TiledMap.class);
//        manager.load("Maps/level2.tmx", TiledMap.class);
//        manager.load("Maps/level3.tmx", TiledMap.class);
//
        friendlyIdleSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/idleAnimationSpritesheet.png"));
        friendlyJumpingSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/jumpingAnimationSpritesheet.png"));
        friendlyRunningSpritesheet = new Texture(Gdx.files.internal("Player Animations/Friendly player/runningAnimationSpritesheet.png"));

        enemyIdleSpritesheet = new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png"));
        enemyJumpingSpritesheet = new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png"));
        enemyRunningSpritesheet = new Texture(Gdx.files.internal("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png"));

        freezeProjectileSpritesheet = new Texture(Gdx.files.internal("Player Animations/Ammo/freezeProjectileSpritesheet.png"));
        freezeProjectileImpactSpritesheet=new Texture(Gdx.files.internal("Player Animations/Ammo/freezeProjectileImpactSpritesheet.png"));
        armFreezeBombSpritesheet = new Texture(Gdx.files.internal("Player Animations/Effect/armFreezeBombSpritesheet.png"));
        freezeBombSpritesheet = new Texture(Gdx.files.internal("Player Animations/Effect/freezeBombSpritesheet.png"));
        crystalSpritesheet = new Texture("Player Animations/Weapon/CrystalSpritesheet.png");
        fireLaserSpritesheet = new Texture(Gdx.files.internal("Player Animations/Ammo/fireLaserSpritesheet.png"));
        armFireBombSpritesheet = new Texture(Gdx.files.internal("Player Animations/Effect/armFireBombSpritesheet.png"));
        fireBombSpritesheet = new Texture(Gdx.files.internal("Player Animations/Effect/fireBombSpritesheet.png"));
        flameSpritesheet = new Texture(Gdx.files.internal("Player Animations/Weapon/flameSpritesheet.png"));

        energyShieldSpritesheet = new Texture(Gdx.files.internal("Player Animations/Effect/energyShieldSpriteSheet.png"));
        frozenSpritesheet = new Texture(Gdx.files.internal("Player Animations/Effect/frozenSpritesheet.png"));

        uiSkin = new Skin(Gdx.files.internal("UIAssets/uiskin.json"));
        hudSkin = new Skin(Gdx.files.internal("UIAssets/golden-ui-skin.json"));

        menuBackground = new Texture(Gdx.files.internal("UIAssets/placeholder.jpg"));
        solidBlack = new Texture(Gdx.files.internal("UIAssets/Black_1080p.png"));
        roundOverScreen = new Texture(Gdx.files.internal("UIAssets/round over.png"));

        generateFonts();

        map1 = new TmxMapLoader().load(Gdx.files.internal("Maps/level1.tmx").toString());
        dungeonMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level2.tmx").toString());
        purpleMap = new TmxMapLoader().load(Gdx.files.internal("Maps/level3.tmx").toString());
    }

    private static void generateFonts() {
//        FileHandleResolver resolver = new InternalFileHandleResolver();
//        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
//        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
//
//        FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
//        parameter.fontFileName = "UIAssets/joystix monospace.ttf";
//        parameter.fontParameters.size = 12;
//        manager.load("joystixNormal.ttf", BitmapFont.class, parameter);
//
//        FreetypeFontLoader.FreeTypeFontLoaderParameter bigParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
//        bigParameter.fontFileName = "UIAssets/joystix monospace.ttf";
//        bigParameter.fontParameters.size = 24;
//        bigParameter.fontParameters.shadowOffsetX = 2;
//        bigParameter.fontParameters.shadowOffsetY = 2;
//        manager.load("joystixBig.ttf", BitmapFont.class, bigParameter);

        //font1 = new BitmapFont(Gdx.files.internal("UIAssets/default.fnt"));

        retroFontGen = new FreeTypeFontGenerator(Gdx.files.internal("UIAssets/joystix monospace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 12;
        normalSizeFont = retroFontGen.generateFont(fontParameter);

        fontParameter.size = 24;
        fontParameter.shadowColor = Color.DARK_GRAY;
        fontParameter.shadowOffsetX = 3;
        fontParameter.shadowOffsetY = 3;
        bigSizeFont = retroFontGen.generateFont(fontParameter);

        uiSkin.get("default", TextButton.TextButtonStyle.class).font = bigSizeFont;
        uiSkin.get("default", TextField.TextFieldStyle.class).font = bigSizeFont;
        uiSkin.get("default", SelectBox.SelectBoxStyle.class).font =
                uiSkin.get("default", SelectBox.SelectBoxStyle.class).listStyle.font = bigSizeFont;
        uiSkin.add("menu-label", new Label.LabelStyle(bigSizeFont, Color.WHITE), Label.LabelStyle.class);

    }

//    public void setAssets() {
//        friendlyIdleSpritesheet = manager.get("Player Animations/Friendly player/idleAnimationSpritesheet.png", Texture.class);
//        friendlyJumpingSpritesheet = manager.get("Player Animations/Friendly player/jumpingAnimationSpritesheet.png", Texture.class);
//        friendlyRunningSpritesheet = manager.get("Player Animations/Friendly player/runningAnimationSpritesheet.png", Texture.class);
//
//        enemyIdleSpritesheet = manager.get("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png", Texture.class);
//        enemyJumpingSpritesheet = manager.get("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png", Texture.class);
//        enemyRunningSpritesheet = manager.get("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png", Texture.class);
//
//        freezeProjectileSpritesheet = manager.get("Player Animations/Ammo/freezeProjectile.png", Texture.class);
//        crystalSpritesheet = manager.get("Player Animations/Weapon/CrystalSpritesheet.png", Texture.class);
//        fireLaserSpritesheet = manager.get("Player Animations/Ammo/fireLaserSpritesheet.png", Texture.class);
//        flameSpritesheet = manager.get("Player Animations/Weapon/sphereSpritesheet.png", Texture.class);
//
//        energyShieldSpritesheet = manager.get("energyShieldSpriteSheet.png", Texture.class);
//        frozenSpritesheet = manager.get("frozenSpriteSheet.png", Texture.class);
//        menuBackground = manager.get("UIAssets/placeholder.jpg", Texture.class);
//        solidBlack = manager.get("UIAssets/Black_1080p.png", Texture.class);
//        roundOverScreen = manager.get("round over.png", Texture.class);
//
//        uiSkin = manager.get("UIAssets/uiskin.json", Skin.class);
//
//        bigSizeFont = manager.get("joystixBig.ttf", BitmapFont.class);
//        normalSizeFont = manager.get("joystixNormal.ttf", BitmapFont.class);
//    }

    public static void dispose() {
        friendlyIdleSpritesheet.dispose();
        friendlyJumpingSpritesheet.dispose();
        friendlyRunningSpritesheet.dispose();

        enemyIdleSpritesheet.dispose();
        enemyJumpingSpritesheet.dispose();
        enemyRunningSpritesheet.dispose();

        crystalSpritesheet.dispose();
        flameSpritesheet.dispose();
        freezeProjectileSpritesheet.dispose();
        freezeProjectileImpactSpritesheet.dispose();
        fireLaserSpritesheet.dispose();
        armFireBombSpritesheet.dispose();
        fireBombSpritesheet.dispose();
        flameSpritesheet.dispose();

        energyShieldSpritesheet.dispose();
        frozenSpritesheet.dispose();

        uiSkin.dispose();
        hudSkin.dispose();

        menuBackground.dispose();
        solidBlack.dispose();
        roundOverScreen.dispose();

        map1.dispose();
        dungeonMap.dispose();
        purpleMap.dispose();

        normalSizeFont.dispose();
        bigSizeFont.dispose();
        retroFontGen.dispose();
        //manager.dispose();
    }

//    public static ClientAssetLoader getInstance() {
//        return ourInstance;
//    }
}
