package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameserver.ServerAssetLoader;

import java.util.ArrayList;
import java.util.ListIterator;

public class GameLevel {
    private ArrayList<MapObjectHitbox> platforms;
    private ArrayList<Vector2> spawnPoints;

    private TiledMap map;
    private int mapNr;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;
    private Stage stage;

    private Boolean changedLevel = false;

    public GameLevel(Stage stage) {
        platforms = new ArrayList<MapObjectHitbox>();
        spawnPoints = new ArrayList<Vector2>();
        cam = (OrthographicCamera) stage.getCamera();
        this.stage = stage;
        renderer = new OrthogonalTiledMapRenderer(map, 1f);
    }

    public void changeLevel() {

        changedLevel = true;
        renderer.setMap(map);
        //first empty the hitboxes and spawn points of the previous map
        clearHitboxes();
        clearSpawnPoints();

        /*
         * we have to be careful when creating the maps for the layer with hitboxes to be the first one
         * and the one with spawn points on the second one
         */
        for (MapObject x : map.getLayers().get(0).getObjects()) {
            RectangleMapObject currObj = (RectangleMapObject) x;
            Vector2 position = new Vector2(currObj.getRectangle().getX(), currObj.getRectangle().getY());
            Vector2 size = new Vector2(currObj.getRectangle().getWidth(), currObj.getRectangle().getHeight());

            platforms.add(new MapObjectHitbox(stage, position, size));
        }

        for (MapObject x : map.getLayers().get(1).getObjects()) {
            RectangleMapObject currObj = (RectangleMapObject) x;

            spawnPoints.add(new Vector2(currObj.getRectangle().getX(), currObj.getRectangle().getY()));

        }

    }

    public void render() {
        //if the level was just changed
        if (changedLevel) {
            cam.update();
            renderer.setView(cam);
            renderer.render();
        }
    }

    public void setMap(int nr) {
        mapNr = nr;
        switch (mapNr) {
            case 1:
                map = ClientAssetLoader.map1;
                break;
            case 2:
                map = ClientAssetLoader.dungeonMap;
                break;
            case 3:
                map = ClientAssetLoader.purpleMap;
                break;
        }
    }

    public void clearHitboxes(){
        ListIterator<MapObjectHitbox> iter = platforms.listIterator();
        while (iter.hasNext()) {
            MapObjectHitbox hitbox = iter.next();
            hitbox.remove();
            iter.remove();
        }
    }

    private void clearSpawnPoints(){
        ListIterator<Vector2> spawnIter = spawnPoints.listIterator();
        while (spawnIter.hasNext()) {
            spawnIter.next();
            spawnIter.remove();
        }
    }

    public int getMapNr() {
        return mapNr;
    }

    public ArrayList<Vector2> getSpawnPoints() {
        return spawnPoints;
    }
}
