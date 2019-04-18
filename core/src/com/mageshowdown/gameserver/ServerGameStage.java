package com.mageshowdown.gameserver;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mageshowdown.gamelogic.CollisionListener;
import com.mageshowdown.gamelogic.GameLevel;
import com.mageshowdown.gamelogic.GameWorld;

import java.util.HashMap;

public class ServerGameStage extends Stage {
    private GameLevel gameLevel;
    private OrthographicCamera camera;
    private HashMap<Integer,ServerPlayerCharacter> playerCharacters;

    public ServerGameStage() {
        super();
        camera=new OrthographicCamera(1280f,720f);
        camera.position.x=640f;
        camera.position.y= 360f;
        setViewport(new StretchViewport(1280f,720f,camera));
        gameLevel=new GameLevel(this);
        playerCharacters=new HashMap<Integer, ServerPlayerCharacter>();

        GameWorld.world.setContactListener(new CollisionListener());
    }

    @Override
    public void draw() {
    }

    @Override
    public void act() {
        super.act();
    }

    public void addPlayerCharacter(int connectionId, Vector2 pos){
        playerCharacters.put(connectionId,new ServerPlayerCharacter(this,pos,connectionId));
    }

    public ServerPlayerCharacter getPlayerById(int connectionId){
        return playerCharacters.get(connectionId);
    }

    public void removePlayerCharacter(int connectionId){
        GameWorld.bodiesToBeRemoved.add(playerCharacters.get(connectionId).getBody());
        playerCharacters.get(connectionId).remove();
        playerCharacters.remove(connectionId);
    }

    public void start(){
        gameLevel.changeTo(ServerAssetLoader.map1);
    }
}
