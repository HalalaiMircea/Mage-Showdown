package com.mageshowdown.gamelogic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mageshowdown.gameserver.GameServer;
import com.mageshowdown.gameserver.ServerPlayerCharacter;
import com.mageshowdown.packets.Network;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object obj1=contact.getFixtureA().getBody().getUserData(),
                obj2=contact.getFixtureB().getBody().getUserData();



        if(obj1 instanceof Projectile && obj2 instanceof ServerPlayerCharacter) {
            handlePlayerProjectileCollision((Projectile)obj1,(ServerPlayerCharacter)obj2);
        }
        else if (obj1 instanceof ServerPlayerCharacter && obj2 instanceof Projectile){
            handlePlayerProjectileCollision((Projectile)obj2,(ServerPlayerCharacter)obj1);
        }
    }

    private void handlePlayerProjectileCollision(Projectile projectile, ServerPlayerCharacter player){
      //a player cant damage himself so we check if the projectile's owner id is the same as the player's it hit
        if(player.getId()!=projectile.getOwnerId()){
            Network.ProjectileCollided pc=new Network.ProjectileCollided();
            player.damageBy(3);
            projectile.setCollided(true);

            pc.projId=projectile.getId();
            pc.ownerId=projectile.getOwnerId();
            pc.playerHitId=player.getId();

            GameServer.getInstance().sendToAllTCP(pc);
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
