package com.mageshowdown.gameserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mageshowdown.gamelogic.CollisionListener;
import com.mageshowdown.gamelogic.GameLevel;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.Round;
import com.mageshowdown.packets.Network;

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


        GameWorld.world.setContactListener(new CollisionListener(this));
        addActor(ServerRound.getInstance());
    }

    @Override
    public void draw() {
    }

    @Override
    public void act() {
        super.act();
        getInput();


        GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);

        GameWorld.clearBodyRemovalQueue();

        for(ServerPlayerCharacter x:playerCharacters.values()){
            x.clearQueue();
        }
    }

    public void addPlayerCharacter(int connectionId, Vector2 pos){
        playerCharacters.put(connectionId,new ServerPlayerCharacter(this,pos,connectionId));
    }

    public ServerPlayerCharacter getPlayerById(int connectionId){
        return playerCharacters.get(connectionId);
    }

    public void removePlayerCharacter(int connectionId){
        playerCharacters.get(connectionId).destroyActor();
        playerCharacters.remove(connectionId);
    }

    public void start(){
        gameLevel.setMap(2);
        gameLevel.changeLevel();
    }

    public void startRound(){
        ServerRound.getInstance().start();
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    private void getInput(){

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            GameServer.getInstance().sendMapChange(1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
            GameServer.getInstance().sendMapChange(2);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
            GameServer.getInstance().sendMapChange(3);
        }
    }
}
