package com.mageshowdown.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameStage extends Stage {
    private GameLevel gameLevel;
    private OrthographicCamera camera;
    private PlayerCharacter playerCharacter;

    public GameStage() {
        super();
        camera=new OrthographicCamera(1280f,720f);
        camera.position.x=640f;
        camera.position.y= 360f;
        setViewport(new StretchViewport(1280f,720f,camera));
        gameLevel=new GameLevel(this);

        GameWorld.world.setContactListener(new CollisionListener());
    }

    @Override
    public void draw() {
        gameLevel.render();
        super.draw();
    }

    public void start(){
        if(playerCharacter==null)
            playerCharacter=new PlayerCharacter(this);

        gameLevel.changeTo(AssetLoader.map1);
        Gdx.input.setInputProcessor(this);
        setKeyboardFocus(playerCharacter);
    }


}
