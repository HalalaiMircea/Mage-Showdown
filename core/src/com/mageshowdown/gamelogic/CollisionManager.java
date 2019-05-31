package com.mageshowdown.gamelogic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public abstract class CollisionManager implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Object obj1 = contact.getFixtureA().getBody().getUserData(),
                obj2 = contact.getFixtureB().getBody().getUserData();

        if (obj1 instanceof FreezeProjectile && obj2 instanceof GameActor) {
            handleProjectileCollision((FreezeProjectile) obj1, (GameActor) obj2);
        } else if (obj1 instanceof GameActor && obj2 instanceof FreezeProjectile) {
            handleProjectileCollision((FreezeProjectile) obj2, (GameActor) obj1);
        }
    }

    protected void handleProjectileCollision(FreezeProjectile projectile, GameActor actor) {
        if (actor instanceof MapObjectHitbox) {
            projectile.setCollided(true);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Object obj1 = contact.getFixtureA().getBody().getUserData(),
                obj2 = contact.getFixtureB().getBody().getUserData();

        /*
         * if an ammo wants to react to a collision with something, we disable the contact
         * this is needed because if we'd rather make the fixture a sensor,
         * we dont obtain the proper contact point between the ammo and something else
         */
        if(obj1 instanceof Ammo || obj2 instanceof Ammo)
            contact.setEnabled(false);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
