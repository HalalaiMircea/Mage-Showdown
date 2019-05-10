package com.mageshowdown.gamelogic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mageshowdown.gameserver.GameServer;
import com.mageshowdown.gameserver.ServerGameStage;
import com.mageshowdown.gameserver.ServerPlayerCharacter;
import com.mageshowdown.packets.Network;

public class CollisionListener implements ContactListener {

    private ServerGameStage gameStage;

    public CollisionListener(ServerGameStage gameStage){
        this.gameStage=gameStage;
    }


    @Override
    public void beginContact(Contact contact) {
        Object obj1=contact.getFixtureA().getBody().getUserData(),
                obj2=contact.getFixtureB().getBody().getUserData();



        if(obj1 instanceof Ammo && obj2 instanceof ServerPlayerCharacter) {
            handlePlayerProjectileCollision((Ammo)obj1,(ServerPlayerCharacter)obj2);
        }
        else if (obj1 instanceof ServerPlayerCharacter && obj2 instanceof Ammo){
            handlePlayerProjectileCollision((Ammo)obj2,(ServerPlayerCharacter)obj1);
        }
    }

    private void handlePlayerProjectileCollision(Ammo projectile, ServerPlayerCharacter player){
      //a player cant damage itself so we check if the projectile's owner id is the same as the player's it hit
        if(player.getId()!=projectile.getOwnerId()){
            //Network.ProjectileCollided pc=new Network.ProjectileCollided();
            Network.PlayerDead packet=new Network.PlayerDead();
            player.damageBy(projectile.getDamageValue(), projectile);
            projectile.setCollided(true);
/*
            pc.projId=projectile.getId();
            pc.ownerId=projectile.getOwnerId();
            pc.playerHitId=player.getId();
  */
            //if the player died from the hit
            if(player.getHealth()<0){
                gameStage.getPlayerById(projectile.ownerId).addKill();
                packet.id=player.getId();
                packet.respawnPos=GameServer.getInstance().generateSpawnPoint(packet.id);
                player.respawn(packet.respawnPos);
                gameStage.getPlayerById(projectile.ownerId).raiseScore(1);
                GameServer.getInstance().sendToAllTCP(packet);
            }
           // GameServer.getInstance().sendToAllTCP(pc);
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
