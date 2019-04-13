package com.mageshowdown.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;

public class GameStage extends Stage {

    private GameLevel gameLevel;
    private OrthographicCamera camera;

    public GameStage() {
        super();
        camera=new OrthographicCamera(1280f,720f);
        camera.position.x=640f;
        camera.position.y= 360f;
        setViewport(new StretchViewport(1280f,720f,camera));

        gameLevel=new GameLevel(this);
    }

    public void create(){
        PlayerCharacter playerCharacter=new PlayerCharacter(this);
        gameLevel.loadFrom("Maps\\level1.tmx",this);

        setKeyboardFocus(playerCharacter);
    }

    @Override
    public void draw() {
        gameLevel.render();
        super.draw();
    }
}
