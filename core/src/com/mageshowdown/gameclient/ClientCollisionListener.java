package com.mageshowdown.gameclient;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mageshowdown.gamelogic.FreezeProjectile;
import com.mageshowdown.gamelogic.GameActor;


/*
* client-side the collisions between the projectile and other objects like map hitboxes
* and players will be verified for
*/

public class ClientCollisionListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Object obj1 = contact.getFixtureA().getBody().getUserData(),
                obj2 = contact.getFixtureB().getBody().getUserData();


        if (obj1 instanceof FreezeProjectile && obj2 instanceof GameActor) {
            handleProjectileCollision((FreezeProjectile)obj1,(GameActor)obj2);
         } else if (obj1 instanceof GameActor && obj2 instanceof FreezeProjectile) {
            handleProjectileCollision((FreezeProjectile)obj2,(GameActor)obj1);
        }
    }

    private void handleProjectileCollision(FreezeProjectile projectile, GameActor actor){
        //if the collision takes place with a character, we make sure it isnt the one who created the projectile
        if(actor instanceof ClientPlayerCharacter){
            if(projectile.getOwnerId()!=((ClientPlayerCharacter) actor).getId()){
                projectile.setCollided(true);
            }
        }else{
            projectile.setCollided(true);
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
