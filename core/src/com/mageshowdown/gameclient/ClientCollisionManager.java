package com.mageshowdown.gameclient;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.mageshowdown.gamelogic.*;


/*
 * client-side the collisions between the projectile and other objects like map hitboxes
 * and players will be verified for
 */

public class ClientCollisionManager extends CollisionManager {


    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
        Object obj1 = contact.getFixtureA().getBody().getUserData(),
                obj2 = contact.getFixtureB().getBody().getUserData();

        Vector2[] collisionPoints = contact.getWorldManifold().getPoints();

        if (obj1 instanceof Laser && obj2 instanceof MapObjectHitbox) {
            handleLaserCollision((Laser) obj1, collisionPoints);
        } else if (obj1 instanceof MapObjectHitbox && obj2 instanceof Laser) {
            handleLaserCollision((Laser) obj2, collisionPoints);
        }

        if (obj1 instanceof FrostProjectile && obj2 instanceof GameActor) {
            handleProjectileCollision((FrostProjectile) obj1, (GameActor) obj2);
        } else if (obj1 instanceof GameActor && obj2 instanceof FrostProjectile) {
            handleProjectileCollision((FrostProjectile) obj2, (GameActor) obj1);
        }
    }

    @Override
    protected void handleProjectileCollision(FrostProjectile projectile, GameActor actor) {
        super.handleProjectileCollision(projectile, actor);
        if (actor instanceof ClientPlayerCharacter && projectile.getOwnerId() != ((ClientPlayerCharacter) actor).getId())
        {
            projectile.setCollided(true);
        }
    }

    private void handleLaserCollision(Laser laser, Vector2[] contactLocations) {
        for (Vector2 contactLocation : contactLocations)
            laser.createBurningEffect(GameWorld.convertWorldToPixels(contactLocation));
    }

}
