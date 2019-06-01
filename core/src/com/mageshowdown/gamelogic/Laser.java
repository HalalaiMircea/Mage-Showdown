package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class Laser extends Spell implements AnimatedActorInterface {

    private static final float DURATION = .25f;

    public static class BurningEffect extends GameActor implements AnimatedActorInterface {
        private BurningEffect(Stage stage, Vector2 position, boolean isClient) {
            super(stage, new Vector2(position.x, position.y), new Vector2(15, 23), new Vector2(0, 0), 0, new Vector2(2f, 2f),isClient);

            if(CLIENT_ACTOR)
                addAnimation(4, 3, 2f, "idle", ClientAssetLoader.burningSpritesheet);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (passedTime >= 5.0f)
                remove();
            pickFrame();
        }

        @Override
        public void pickFrame() {
            if (animations.containsKey("idle"))
                currFrame = animations.get("idle").getKeyFrame(passedTime, true);
        }

    }

    public Laser(Stage stage, Vector2 position, float rotation, int id, int ownerId, boolean isClient) {
        super(stage, new Vector2(0.001f, 0.001f), position, new Vector2(220, 31), new Vector2(1.5f, 1.2f), new Vector2(325, 15), rotation, id, ownerId, 2, isClient);
        setOrigin(0, getOriginY());
        createBody(rotation, new Vector2(0, getOriginY() / 2), BodyDef.BodyType.DynamicBody);

        if(CLIENT_ACTOR)
            addAnimation(1, 7, .25f, "idle", ClientAssetLoader.fireLaserSpritesheet);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (passedTime >= DURATION) {
            setExpired(true);
        }

        if(CLIENT_ACTOR)
            pickFrame();
    }

    @Override
    protected void updatePositionFromBody() {
        Vector2 convPosition = GameWorld.convertWorldToPixels(body.getPosition());
        setPosition(convPosition.x, convPosition.y - getOriginY());
        setRotation(body.getAngle() * 180 / (float) Math.PI);
    }

    @Override
    public void pickFrame() {
        if (animations.containsKey("idle"))
            currFrame = animations.get("idle").getKeyFrame(passedTime, false);
    }

    public void createBurningEffect(Vector2 position) {
        System.out.println(position);
        new BurningEffect(getStage(), position,CLIENT_ACTOR);
    }
}
