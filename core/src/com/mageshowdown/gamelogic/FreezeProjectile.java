package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class FreezeProjectile extends Spell implements AnimatedActorInterface {

    public FreezeProjectile(Stage stage, Vector2 position, float rotation, Vector2 direction, int id, int ownerId) {
        super(stage, new Vector2(3.5f * direction.x, 3.5f * direction.y), position, new Vector2(46, 31), new Vector2(1f, 1f), new Vector2(24, 12), rotation, id, ownerId, 3);
        createBody(getRotation(), new Vector2(getOriginX() / 2, getOriginY() / 2), BodyDef.BodyType.DynamicBody);
        addAnimation(9, 1, 1f, "idle", ClientAssetLoader.freezeProjectileSpritesheet);
        addAnimation(1, 8, 1f, "impact", ClientAssetLoader.freezeProjectileImpactSpritesheet);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        if (collided && animations.get("impact").isAnimationFinished(passedTime))
            destroyable = true;
        pickFrame();
    }

    @Override
    public void pickFrame() {
        if (!collided && animations.containsKey("idle")) {
            currFrame = animations.get("idle").getKeyFrame(passedTime, true);
        } else if (collided && animations.containsKey("impact")) {
            currFrame = animations.get("impact").getKeyFrame(passedTime, false);
        }
    }

    @Override
    public void setCollided(boolean collided) {
        super.setCollided(collided);
        //when the projectile collides with something we reset the internal passed time and use it for the impact animation
        passedTime = 0f;
        if (collided) {
            velocity = new Vector2(0, 0);
            setScale(1.5f);
        }
    }
}
