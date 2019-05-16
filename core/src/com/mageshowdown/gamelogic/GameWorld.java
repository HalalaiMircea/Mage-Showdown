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

    /*
     * box2d doesnt like deleting too many bodies in one go so we set a hard limit
     */
    private final static int BODY_REMOVAL_LIMIT = 5;

    static {
        world = new World(new Vector2(0, -9.8f), true);
        bodiesToBeRemoved = new LinkedList<Body>();
    }

    public static void setResolutionScale(float _resolutionScale) {
        resolutionScale = _resolutionScale;
    }

    public static float getMouseVectorAngle(Vector2 startPoint) {
        double dy = startPoint.y - getMousePos().y;
        double dx = startPoint.x - getMousePos().x;

        float rotation = (float) Math.atan2(dy, dx) * 180 / (float) Math.PI;

        return rotation + 180f;
    }

    //returns the normalized direction vector for where the mouse is pointing
    public static Vector2 getNormalizedMouseVector(Vector2 startPoint) {
        return new Vector2((getMousePos().x - startPoint.x), (getMousePos().y - startPoint.y)).nor();
    }

    /*
     * because we use a stretchviewport we need to get the mouse coordinates in terms of game world, 1280x720, not whatever resolution we're running,
     * so we get the mouse position within the world
     */
    public static Vector2 getMousePos(Vector2 middle) {
        return new Vector2(Gdx.input.getX() / resolutionScale - middle.x, (720f - Gdx.input.getY() / resolutionScale) - middle.y);
    }

    public static Vector2 getMousePos() {
        return getMousePos(new Vector2(0, 0));
    }

    //1m in box2d world == 100 pixels in game world
    public static Vector2 convertPixelsToWorld(Vector2 pixels) {
        return new Vector2(pixels.x * 0.01f, pixels.y * 0.01f);
    }

    public static Vector2 convertWorldToPixels(Vector2 worldCoord) {
        return new Vector2(worldCoord.x * 100f, worldCoord.y * 100f);
    }

    public static void clearBodyRemovalQueue() {
        Array<Body> existingBodies = new Array<Body>();
        int count = 0;
        while (bodiesToBeRemoved.size() > 0 && count < BODY_REMOVAL_LIMIT) {
            world.getBodies(existingBodies);
            //we make sure the body exists before we delete it; if it does we just pop the queue
            if (existingBodies.contains(bodiesToBeRemoved.peek(), true)) {
                if (!world.isLocked()) {
                    Body body = bodiesToBeRemoved.remove();
                    System.out.println("trying to destroy body at" + convertWorldToPixels(body.getPosition()));
                    world.destroyBody(body);
                    System.out.println("destroyed the body");
                } else return;
                count++;
            } else {
                bodiesToBeRemoved.remove();
            }
        }
    }

}
