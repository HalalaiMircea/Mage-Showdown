package com.mageshowdown.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {
    public static World world;
    public static float resolutionScale;

    static{
        world=new World(new Vector2(0,-9.8f),true);
    }

    public static void setResolutionScale(float _resolutionScale){
        resolutionScale=_resolutionScale;
    }
    public static Vector2 scalePosition(Vector2 position){return new Vector2(position.x*resolutionScale,position.y*resolutionScale);}

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

    public static Vector2 getMousePos(){
        return new Vector2(Gdx.input.getX()/resolutionScale, (Gdx.graphics.getHeight()-Gdx.input.getY())/resolutionScale);
    }

    //1m in box2d world == 100 pixels in game world
    public static Vector2 convertPixelsToWorld(Vector2 pixels){
        return new Vector2(pixels.x/resolutionScale*0.01f,pixels.y/resolutionScale*0.01f);
    }

    public static Vector2 convertWorldToPixels(Vector2 worldCoord){
        return new Vector2(worldCoord.x*resolutionScale*100f,worldCoord.y*resolutionScale*100f);
    }

}
