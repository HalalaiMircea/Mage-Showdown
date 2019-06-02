package com.mageshowdown.gameclient;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class ClientAssetLoader {

    public static Texture solidBlack;
    public static Skin uiSkin;
    public static Skin hudSkin;
    public static Texture menuBackground;
    //fonts
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
    public static Texture burningSpritesheet;
    public static Texture energyShieldSpritesheet;
    public static Texture frozenSpritesheet;
    //sounds & music
    public static Sound btnClickSound;
    //preferences files
    public static Preferences prefs;
    //maps
    public static TiledMap map1;
    public static TiledMap dungeonMap;
    public static TiledMap purpleMap;

    public AssetManager manager;

    private static ClientAssetLoader ourInstance = new ClientAssetLoader();

    private ClientAssetLoader() {
        manager = new AssetManager();
    }

    public static ClientAssetLoader getInstance() {
        return ourInstance;
    }

    public void load() {
        //Friendly player spritesheets
        manager.load("Player Animations/Friendly player/idleAnimationSpritesheet.png", Texture.class);
        manager.load("Player Animations/Friendly player/jumpingAnimationSpritesheet.png", Texture.class);
        manager.load("Player Animations/Friendly player/runningAnimationSpritesheet.png", Texture.class);

        //Enemy player spritesheets
        manager.load("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png", Texture.class);
        manager.load("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png", Texture.class);
        manager.load("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png", Texture.class);

        //Freeze projectiles
        manager.load("Player Animations/Orb/CrystalSpritesheet.png", Texture.class);
        manager.load("Player Animations/Spell/freezeProjectileSpritesheet.png", Texture.class);
        manager.load("Player Animations/Spell/freezeProjectileImpactSpritesheet.png", Texture.class);
        manager.load("Player Animations/Effect/armFreezeBombSpritesheet.png", Texture.class);
        manager.load("Player Animations/Effect/freezeBombSpritesheet.png", Texture.class);

        //Flame laser
        manager.load("Player Animations/Orb/flameSpritesheet.png", Texture.class);
        manager.load("Player Animations/Spell/fireLaserSpritesheet.png", Texture.class);
        manager.load("Player Animations/Effect/burningSpritesheet.png", Texture.class);
        manager.load("Player Animations/Effect/armFireBombSpritesheet.png", Texture.class);
        manager.load("Player Animations/Effect/fireBombSpritesheet.png", Texture.class);

        manager.load("Player Animations/Effect/energyShieldSpriteSheet.png", Texture.class);
        manager.load("Player Animations/Effect/frozenSpritesheet.png", Texture.class);

        manager.load("Sounds/click.ogg", Sound.class);

        manager.load("UIAssets/placeholder.jpg", Texture.class);
        manager.load("UIAssets/Black_1080p.png", Texture.class);

        manager.load("UIAssets/uiskin.json", Skin.class);
        manager.load("UIAssets/golden-ui-skin.json", Skin.class);

        generateFonts();

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("Maps/level1.tmx", TiledMap.class);
        manager.load("Maps/level2.tmx", TiledMap.class);
        manager.load("Maps/level3.tmx", TiledMap.class);
    }

    private void generateFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontFileName = "UIAssets/joystix monospace.ttf";
        parameter.fontParameters.size = 12;
        manager.load("joystixNormal.ttf", BitmapFont.class, parameter);

        FreetypeFontLoader.FreeTypeFontLoaderParameter bigParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        bigParameter.fontFileName = "UIAssets/joystix monospace.ttf";
        bigParameter.fontParameters.size = 24;
        bigParameter.fontParameters.shadowColor = Color.DARK_GRAY;
        bigParameter.fontParameters.shadowOffsetX = 3;
        bigParameter.fontParameters.shadowOffsetY = 3;
        manager.load("joystixBig.ttf", BitmapFont.class, bigParameter);
    }

    public void setAssets() {
        friendlyIdleSpritesheet = manager.get("Player Animations/Friendly player/idleAnimationSpritesheet.png", Texture.class);
        friendlyJumpingSpritesheet = manager.get("Player Animations/Friendly player/jumpingAnimationSpritesheet.png", Texture.class);
        friendlyRunningSpritesheet = manager.get("Player Animations/Friendly player/runningAnimationSpritesheet.png", Texture.class);

        enemyIdleSpritesheet = manager.get("Player Animations/Enemy player/enemyIdleAnimationSpritesheet.png", Texture.class);
        enemyJumpingSpritesheet = manager.get("Player Animations/Enemy player/enemyJumpingAnimationSpritesheet.png", Texture.class);
        enemyRunningSpritesheet = manager.get("Player Animations/Enemy player/enemyRunningAnimationSpritesheet.png", Texture.class);

        crystalSpritesheet = manager.get("Player Animations/Orb/CrystalSpritesheet.png", Texture.class);
        freezeProjectileSpritesheet = manager.get("Player Animations/Spell/freezeProjectileSpritesheet.png", Texture.class);
        freezeProjectileImpactSpritesheet = manager.get("Player Animations/Spell/freezeProjectileImpactSpritesheet.png", Texture.class);
        armFreezeBombSpritesheet = manager.get("Player Animations/Effect/armFreezeBombSpritesheet.png", Texture.class);
        freezeBombSpritesheet = manager.get("Player Animations/Effect/freezeBombSpritesheet.png", Texture.class);

        flameSpritesheet = manager.get("Player Animations/Orb/flameSpritesheet.png", Texture.class);
        fireLaserSpritesheet = manager.get("Player Animations/Spell/fireLaserSpritesheet.png", Texture.class);
        burningSpritesheet = manager.get("Player Animations/Effect/burningSpritesheet.png", Texture.class);
        armFireBombSpritesheet = manager.get("Player Animations/Effect/armFireBombSpritesheet.png", Texture.class);
        fireBombSpritesheet = manager.get("Player Animations/Effect/fireBombSpritesheet.png", Texture.class);

        energyShieldSpritesheet = manager.get("Player Animations/Effect/energyShieldSpriteSheet.png", Texture.class);
        frozenSpritesheet = manager.get("Player Animations/Effect/frozenSpritesheet.png", Texture.class);

        btnClickSound = manager.get("Sounds/click.ogg", Sound.class);

        menuBackground = manager.get("UIAssets/placeholder.jpg", Texture.class);
        solidBlack = manager.get("UIAssets/Black_1080p.png", Texture.class);

        uiSkin = manager.get("UIAssets/uiskin.json", Skin.class);
        hudSkin = manager.get("UIAssets/golden-ui-skin.json", Skin.class);

        bigSizeFont = manager.get("joystixBig.ttf", BitmapFont.class);
        normalSizeFont = manager.get("joystixNormal.ttf", BitmapFont.class);

        uiSkin.get("default", TextButton.TextButtonStyle.class).font = bigSizeFont;
        uiSkin.get("default", TextField.TextFieldStyle.class).font = bigSizeFont;
        uiSkin.get("default", SelectBox.SelectBoxStyle.class).font =
                uiSkin.get("default", SelectBox.SelectBoxStyle.class).listStyle.font = bigSizeFont;
        uiSkin.add("menu-label", new Label.LabelStyle(bigSizeFont, Color.WHITE), Label.LabelStyle.class);

        map1 = manager.get("Maps/level1.tmx", TiledMap.class);
        dungeonMap = manager.get("Maps/level2.tmx", TiledMap.class);
        purpleMap = manager.get("Maps/level3.tmx", TiledMap.class);
    }

    public void dispose() {
        manager.dispose();
    }
}
