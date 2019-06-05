package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gamelogic.GameLevel;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.packets.Network;

import java.util.HashMap;
import java.util.TreeMap;

public class ClientGameStage extends Stage {
    private GameLevel gameLevel;
    private ClientPlayerCharacter playerCharacter;
    private HashMap<Integer, ClientPlayerCharacter> otherPlayers;
    private TreeMap<Integer, ClientPlayerCharacter> sortedPlayers;

    private Box2DDebugRenderer b2dr;

    public ClientGameStage() {
        super();
        Viewport viewport = new StretchViewport(1280f, 720f, new OrthographicCamera(getViewport().getScreenWidth(), getViewport().getScreenHeight()));
        setViewport(viewport);

        b2dr = new Box2DDebugRenderer();
        gameLevel = new GameLevel(this);
        otherPlayers = new HashMap<Integer, ClientPlayerCharacter>();
        sortedPlayers = new TreeMap<Integer, ClientPlayerCharacter>();
    }

    @Override
    public void act() {
        super.act();
        GameWorld.world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        /*
         * anything that affects the bodies inside a world has to be done after a world has stepped
         * otherwise the game will crash because the world is locked; here the positions and velocities of players' bodies are synchronized
         * and the bodies that have to be removed are removed
         */
        GameWorld.clearBodyCreationQueue();
        GameWorld.clearBodyRemovalQueue();
        if (playerCharacter != null)
            playerCharacter.clearQueue();
        for (ClientPlayerCharacter pc : otherPlayers.values()) {
            pc.clearQueue();
        }
    }

    @Override
    public void draw() {
        gameLevel.render();
        b2dr.render(GameWorld.world, getViewport().getCamera().combined.cpy().scale(100, 100, 1));
        super.draw();
    }

    public void start() {
        Gdx.input.setInputProcessor(this);
    }

    public ClientPlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public HashMap<Integer, ClientPlayerCharacter> getOtherPlayers() {
        return otherPlayers;
    }

    public TreeMap<Integer, ClientPlayerCharacter> getSortedPlayers() {
        return sortedPlayers;
    }

    public void removePlayerCharacter(int connectionId) {
        if (otherPlayers.get(connectionId) != null) {
            sortedPlayers.remove(connectionId);
            otherPlayers.get(connectionId).remove();
            otherPlayers.remove(connectionId);
        }
    }

    public void removeMyCharacter() {
        playerCharacter.remove();
    }

    public void spawnMyPlayerCharacter(Network.NewPlayerSpawned packet) {
        playerCharacter = new ClientPlayerCharacter(this, packet.pos, packet.orbEquipped, packet.userName, true);
        sortedPlayers.put(playerCharacter.getId(), playerCharacter);

        Gdx.input.setInputProcessor(playerCharacter);

        ClientRound.getInstance().start();
        addActor(ClientRound.getInstance());
    }

    public void spawnOtherPlayer(Network.NewPlayerSpawned packet) {
        ClientPlayerCharacter temp = new ClientPlayerCharacter(this, packet.pos, packet.orbEquipped, packet.userName, false);
        temp.setId(packet.id);
        otherPlayers.put(packet.id, temp);
        sortedPlayers.put(packet.id, temp);
    }

    @Override
    public void clear() {
        super.clear();
        removeMyCharacter();
        for (Integer key : getOtherPlayers().keySet()) {
            removePlayerCharacter(key);
        }

        //clear any leftover body in the box2d world
        Array<Body> bodies = new Array<Body>();
        GameWorld.world.getBodies(bodies);
        for (Body body : bodies)
            GameWorld.addToBodyRemovalQueue(body);

        while (GameWorld.world.getBodyCount() > 0) {
            GameWorld.clearBodyRemovalQueue();
        }
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }
}
