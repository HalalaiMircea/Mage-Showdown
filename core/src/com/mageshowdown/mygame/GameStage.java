package com.mageshowdown.mygame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStage extends Stage {

    public GameStage(Viewport viewport) {
        super(viewport);
    }

    public void create(){
        PlayerCharacter boxActor=new PlayerCharacter(this);
        Platform groundActor=new Platform(this, new Vector2(0,0), new Vector2(AssetLoader.groundTexture.getWidth(), AssetLoader.groundTexture.getHeight()),
                AssetLoader.groundTexture);

        setKeyboardFocus(boxActor);
    }

}
