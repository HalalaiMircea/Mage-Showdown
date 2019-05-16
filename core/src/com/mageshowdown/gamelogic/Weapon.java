package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;


public class Weapon extends GameActor implements AnimatedActorInterface {

    public enum AmmoType {
        FREEZE,
        FIRE
    }

    private AmmoType ammoType;
    private ArrayList<Ammo> ammunition;
    private final static HashMap<String, Float> ammoCosts;
    private boolean loadAnimation;

    /*
     * weapons will work like an energy shield; their "ammo" capacity will constantly regenerate
     * shooting the weapon interrupts that process, after which there is a small cooldown until the regeneration starts again
     */
    private final float COOLDOWN_TIME;
    private final float MAXIMUM_CAPACITY;
    private final float RECHARGE_RATE = 10f;   //recharge rate per second

    private float currentCapacity;
    private float rechargeTimer = 0f;
    private boolean recharge = false;

    static {
        ammoCosts = new HashMap<String, Float>();
        ammoCosts.put("freeze projectile", 2f);
        ammoCosts.put("laser", 1f);
        ammoCosts.put("freeze bomb", 6f);
        ammoCosts.put("fire bomb", 8f);
    }

    public Weapon(Stage stage, boolean loadAnimation, AmmoType ammoType, float cd, int capacity) {
        super(stage, new Vector2(0, 0), new Vector2(64, 66), 0f);
        COOLDOWN_TIME = cd;
        MAXIMUM_CAPACITY = capacity;
        currentCapacity = MAXIMUM_CAPACITY;
        this.ammoType = ammoType;
        ammunition = new ArrayList<Ammo>();
        this.loadAnimation = loadAnimation;


        if (loadAnimation)
            switch (ammoType) {
                case FREEZE:
                    addAnimation(6, 1, 1.5f, "idle", ClientAssetLoader.crystalSpritesheet);
                    break;
                case FIRE:
                    addAnimation(6, 2, 1.5f, "idle", ClientAssetLoader.sphereSpriteSheet);
                    break;
            }
    }

    public void updatePosition(Vector2 position) {
        Vector2 offset = new Vector2(-15f, 35f);
        setPosition(position.x + offset.x, position.y + offset.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        destroyEliminatedProjectiles();
        rechargeWeapon();

        if (loadAnimation)
            pickFrame();
    }

    private void destroyEliminatedProjectiles() {
        /*
         * if it has collided or out of the screen remove projectile from the arraylist and the stage so theres no reference to it left
         */
        ListIterator<Ammo> iter = ammunition.listIterator();
        while (iter.hasNext()) {
            Ammo x = iter.next();
            if (x.isOutOfBounds() || x.hasCollided() || x.isExpired()) {
                x.destroyActor();
                iter.remove();
            }
        }
    }

    public void projectileHasCollided(int projId) {
        for (Ammo x : ammunition) {
            if (x.getId() == projId) {
                x.setCollided(true);
                break;
            }
        }
    }

    public void shoot(Vector2 direction, float rotation, int ownerId) {
        switch (ammoType) {
            case FREEZE:
                if (currentCapacity > ammoCosts.get("freeze projectile")) {
                    ammunition.add(new FreezeProjectile(getStage(), getShootingOrigin(), rotation, direction, ammunition.size(), ownerId));
                    currentCapacity -= ammoCosts.get("freeze projectile");
                    recharge = false;
                }
                break;
            case FIRE:
                if (currentCapacity > ammoCosts.get("laser")) {
                    ammunition.add(new Laser(getStage(), getShootingOrigin(), rotation, ammunition.size(), ownerId));
                    currentCapacity -= ammoCosts.get("laser");
                    recharge = false;
                }
                break;
        }
    }

    public void plantBomb(Vector2 position,int ownerId) {
        System.out.println(getWidth()+" "+getHeight());
        switch (ammoType) {
            case FREEZE:
                if (currentCapacity > ammoCosts.get("freeze bomb")) {
                    ammunition.add(new Bomb(getStage(), position, 0, ammunition.size(), ownerId, ammoType));
                    currentCapacity -= ammoCosts.get("freeze bomb");
                    recharge = false;
                }
                break;
            case FIRE:
                if (currentCapacity > ammoCosts.get("fire bomb")) {
                    ammunition.add(new Bomb(getStage(), position, 0, ammunition.size(), ownerId, ammoType));
                    currentCapacity -= ammoCosts.get("fire bomb");
                    recharge = false;
                }
                break;
        }
    }

    private void rechargeWeapon() {
        if (recharge) {
            if (currentCapacity < MAXIMUM_CAPACITY) {
                currentCapacity += RECHARGE_RATE * Gdx.graphics.getDeltaTime();
            }
        } else {
            rechargeTimer += Gdx.graphics.getDeltaTime();
            if (rechargeTimer >= COOLDOWN_TIME) {
                recharge = true;
                rechargeTimer = 0f;
            }
        }
    }

    public Vector2 getShootingOrigin() {
        return new Vector2((getX() + getWidth() / 2), (getY() + getHeight() / 2));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (currFrame != null)
            batch.draw(currFrame, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    @Override
    public void pickFrame() {
        if (animations.get("idle") != null)
            currFrame = animations.get("idle").getKeyFrame(passedTime, true);
    }

    @Override
    public void clearQueue() {
        for (Ammo x : ammunition) {
            x.clearQueue();
        }
    }

}
