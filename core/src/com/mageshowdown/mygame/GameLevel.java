package com.mageshowdown.mygame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class GameLevel {
    private ArrayList<MapObjectHitbox> platforms;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;
    private Stage stage;

    public GameLevel(Stage stage){
        platforms=new ArrayList<MapObjectHitbox>();
        cam=(OrthographicCamera)stage.getCamera();
        this.stage=stage;
    }

    public void changeTo(TiledMap map){
        this.map=map;
        renderer=new OrthogonalTiledMapRenderer(map,1f);
        /*
        * we have to be careful when creating the maps for the object layer to be the first one
        */
        for(MapObject x:map.getLayers().get(0).getObjects()){
            RectangleMapObject currObj=(RectangleMapObject)x;
            Vector2 position= new Vector2(currObj.getRectangle().getX(),currObj.getRectangle().getY());
            Vector2 size=new Vector2(currObj.getRectangle().getWidth(),currObj.getRectangle().getHeight());

            platforms.add(new MapObjectHitbox(stage,position,size));
        }


    }

    public void render(){
        cam.update();
        renderer.setView(cam);
        renderer.render();
    }
}
