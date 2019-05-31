package com.mageshowdown.gameclient;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mageshowdown.gamelogic.*;

import java.util.Map;


/*
 * client-side the collisions between the projectile and other objects like map hitboxes
 * and players will be verified for
 */

public class ClientCollisionManager extends CollisionManager{


    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
        Object obj1 = contact.getFixtureA().getBody().getUserData(),
                obj2 = contact.getFixtureB().getBody().getUserData();

        Vector2 collisionPoint=GameWorld.convertWorldToPixels(contact.getWorldManifold().getPoints()[0]);

        if (obj1 instanceof Laser && obj2 instanceof MapObjectHitbox) {
            handleLaserCollision((Laser) obj1, collisionPoint);
        } else if (obj1 instanceof MapObjectHitbox && obj2 instanceof Laser) {
            handleLaserCollision((Laser) obj2, collisionPoint);
        }

        if (obj1 instanceof FreezeProjectile && obj2 instanceof GameActor) {
            handleProjectileCollision((FreezeProjectile) obj1, (GameActor) obj2);
        } else if (obj1 instanceof GameActor && obj2 instanceof FreezeProjectile) {
            handleProjectileCollision((FreezeProjectile) obj2, (GameActor) obj1);
        }
    }

    @Override
    protected void handleProjectileCollision(FreezeProjectile projectile, GameActor actor) {
        super.handleProjectileCollision(projectile,actor);
        if(actor instanceof ClientPlayerCharacter && projectile.getOwnerId()!=((ClientPlayerCharacter) actor).getId())
            projectile.setCollided(true);
    }

    private void handleLaserCollision(Laser laser, Vector2 contactLocation) {
        laser.createBurningEffect(contactLocation);
    }

}
