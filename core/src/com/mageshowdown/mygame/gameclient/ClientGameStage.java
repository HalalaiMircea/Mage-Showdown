package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mageshowdown.mygame.gamelogic.*;


import java.util.HashMap;

public class ClientGameStage extends Stage {
    private GameLevel gameLevel;
    private OrthographicCamera camera;
    private ClientPlayerCharacter playerCharacter;
    private HashMap<Integer,ClientPlayerCharacter> otherPlayers;

    public ClientGameStage() {
        super();
        camera=new OrthographicCamera(1280f,720f);
        camera.position.x=640f;
        camera.position.y= 360f;
        setViewport(new StretchViewport(1280f,720f,camera));
        gameLevel=new GameLevel(this);
        otherPlayers=new HashMap<Integer, ClientPlayerCharacter>();

        }

    @Override
    public void act() {
        super.act();
        GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
        if(playerCharacter!=null)
            playerCharacter.clearQueue();
        for(ClientPlayerCharacter pc:otherPlayers.values()){
            pc.clearQueue();
        }
    }

    @Override
    public void draw() {
        gameLevel.render();
        super.draw();
    }

    public void start(){
        gameLevel.changeTo(ClientAssetLoader.map1);
        Gdx.input.setInputProcessor(this);
    }

    public ClientPlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public HashMap<Integer, ClientPlayerCharacter> getOtherPlayers() {
        return otherPlayers;
    }

    public void spawnMyPlayerCharacter(Vector2 position){
        playerCharacter=new ClientPlayerCharacter(this,position);
        setKeyboardFocus(playerCharacter);
    }

    public void spawnOtherPlayer(int id, Vector2 position){
        otherPlayers.put(id,new ClientPlayerCharacter(this,position));
    }
}
