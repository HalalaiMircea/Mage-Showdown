package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mageshowdown.gamelogic.GameLevel;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.MenuScreen;


import java.util.HashMap;

public class ClientGameStage extends Stage {
    private GameLevel gameLevel;
    private OrthographicCamera camera;
    private ClientPlayerCharacter playerCharacter;
    private HashMap<Integer,ClientPlayerCharacter> otherPlayers;
    private Table menuTable;
    private SpriteBatch batch;
    private boolean drawOverlay = false;

    public ClientGameStage() {
        super();
        camera=new OrthographicCamera(1280f,720f);
        camera.position.x=640f;
        camera.position.y= 360f;
        setViewport(new StretchViewport(1280f,720f,camera));
        gameLevel=new GameLevel(this);
        otherPlayers=new HashMap<Integer, ClientPlayerCharacter>();
        prepEscOverlay();
        }

    @Override
    public void act() {
        super.act();
        GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);

        /*
        * anything that affects the bodies inside a world has to be done after a world has stepped
        * otherwise it can cause it to lock; here the positions and velocities of players' bodies are synchronized
        * and the bodies that have to be removed are removed
        */
        GameWorld.clearBodyRemovalQueue();

        if(playerCharacter!=null)
            playerCharacter.clearQueue();
        for(ClientPlayerCharacter pc:otherPlayers.values()){
            pc.clearQueue();
        }
    }

    @Override
    public void draw() {
        gameLevel.render();
        if(drawOverlay) {
            batch.begin();
            menuTable.draw(batch, 1f);
            batch.end();
        }
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

    public void removePlayerCharacter(int connectionId){
        GameWorld.bodiesToBeRemoved.add(otherPlayers.get(connectionId).getBody());
        otherPlayers.get(connectionId).remove();
        otherPlayers.remove(connectionId);
    }

    public void spawnMyPlayerCharacter(Vector2 position, String userName){
        playerCharacter=new ClientPlayerCharacter(this,position,userName);
        setKeyboardFocus(playerCharacter);
    }

    public void spawnOtherPlayer(int id, Vector2 position, String userName){
        otherPlayers.put(id,new ClientPlayerCharacter(this,position,userName));
    }

    public void setDrawOverlay(boolean drawOverlay) {
        this.drawOverlay = drawOverlay;
    }

    private void prepEscOverlay(){
        menuTable = new Table();
        batch = new SpriteBatch();
        menuTable.setFillParent(true);

        TextButton resumeButton = new TextButton("Resume game", ClientAssetLoader.interfaceSkin);
        TextButton optionsButton = new TextButton("Options", ClientAssetLoader.interfaceSkin);
        TextButton quitButton = new TextButton("Quit to Desktop", ClientAssetLoader.interfaceSkin);

        menuTable.add(resumeButton).padBottom(20);
        menuTable.row();
        menuTable.add(optionsButton).padBottom(20);
        menuTable.row();
        menuTable.add(quitButton);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                drawOverlay = false;
            }
        });
    }
}
