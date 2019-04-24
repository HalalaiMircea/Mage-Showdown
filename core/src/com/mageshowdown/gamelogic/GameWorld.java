package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mageshowdown.gameclient.GameClient;
import com.mageshowdown.gameserver.GameServer;

import java.util.LinkedList;

public class GameWorld {
    public static final World world;
    public static float resolutionScale;
    public static LinkedList<Body> bodiesToBeRemoved;

    static{
        world=new World(new Vector2(0,-9.8f),true);
        bodiesToBeRemoved=new LinkedList<Body>();
    }

    public static void setResolutionScale(float _resolutionScale){
        resolutionScale=_resolutionScale;
    }

    public static float getMouseVectorAngle(Vector2 startPoint){
        double dy=startPoint.y-getMousePos().y;
        double dx=startPoint.x-getMousePos().x;

        float rotation=(float)Math.atan2(dy,dx)* 180 / (float) Math.PI;

        return rotation;
    }

    //returns the normalized direction vector for where the mouse is pointing
    public static Vector2 getNormalizedMouseVector(Vector2 startPoint){
        return new Vector2((getMousePos().x-startPoint.x),(getMousePos().y-startPoint.y)).nor();
    }

    /*
    * because we use a stretchviewport we need to get the mouse coordinates in terms of game world, 1280x720, not whatever resolution we're running,
    * so we get the mouse position within the world
    */
    public static Vector2 getMousePos(){
        return new Vector2(Gdx.input.getX()/resolutionScale, (720f-Gdx.input.getY()/resolutionScale));
    }

    //1m in box2d world == 100 pixels in game world
    public static Vector2 convertPixelsToWorld(Vector2 pixels){
        return new Vector2(pixels.x*0.01f,pixels.y*0.01f);
    }

    public static Vector2 convertWorldToPixels(Vector2 worldCoord){
        return new Vector2(worldCoord.x*100f,worldCoord.y*100f);
    }

    public static void clearBodyRemovalQueue(){
        Array<Body> existingBodies=new Array<Body>();
        while(bodiesToBeRemoved.size()>0){
            world.getBodies(existingBodies);
            //we make sure the body exists before we delete it; if it does we just pop the queue
            if(existingBodies.contains(bodiesToBeRemoved.peek(),true))
                world.destroyBody(bodiesToBeRemoved.remove());
            else{
                bodiesToBeRemoved.remove();
            }
        }
    }

}
