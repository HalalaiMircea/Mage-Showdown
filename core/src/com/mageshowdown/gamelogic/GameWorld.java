package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mageshowdown.gameclient.GameClient;
import com.mageshowdown.gameserver.GameServer;

import javax.swing.*;
import java.util.LinkedList;
import java.util.concurrent.Callable;

public class GameWorld {
    public static final World world;
    public static float resolutionScale;
    /* queues for body removals and creations that will happen after the world has stepped
     * because each actor may want to create a specific type of body, for body creation i use
     * a queue of callables and in the call method of these
     * i put the specific createBody() call of a particular actor
     */
    private static LinkedList<Body> bodiesToBeRemoved;
    private static LinkedList<Callable> bodiesToBeCreated;

    static {
        world = new World(new Vector2(0, -9.8f), true);
        bodiesToBeRemoved = new LinkedList<Body>();
        bodiesToBeCreated = new LinkedList<Callable>();
    }

    /*
    * since the size of the world remains static, we need to know how much smaller/bigger it is than the resolution
    * in order to find the correct position of the mouse
     */
    public static void updateResolutionScale() {
        resolutionScale = Gdx.graphics.getWidth() / 1280f;
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

    public static void addToBodyCreationQueue(Callable<Void> bodyCreateCall) {
        bodiesToBeCreated.add(bodyCreateCall);
    }

    public static void clearBodyCreationQueue() {
        while (!bodiesToBeCreated.isEmpty()) {
            try {
                bodiesToBeCreated.remove().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addToBodyRemovalQueue(Body body) {
        bodiesToBeRemoved.add(body);
    }

    public static void clearBodyRemovalQueue() {
        Array<Body> existingBodies = new Array<Body>();
        while (!bodiesToBeRemoved.isEmpty()) {
            world.getBodies(existingBodies);
            //we make sure the body exists before we delete it; if it doesn't we just pop the queue
            if (existingBodies.contains(bodiesToBeRemoved.peek(), false)) {
                Body body = bodiesToBeRemoved.remove();
                world.destroyBody(body);
            } else {
                bodiesToBeRemoved.remove();
            }
        }
    }

}
