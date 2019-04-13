package com.mageshowdown.mygame;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object obj1=contact.getFixtureA().getBody().getUserData(),
                obj2=contact.getFixtureB().getBody().getUserData();



        if(obj1 instanceof Projectile && obj2 instanceof MapObjectHitbox) {
            ((Projectile) obj1).setCollided(true);
        }
        else if (obj1 instanceof MapObjectHitbox && obj2 instanceof Projectile){
            ((Projectile) obj2).setCollided(true);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
