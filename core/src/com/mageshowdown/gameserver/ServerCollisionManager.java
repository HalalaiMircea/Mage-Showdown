package com.mageshowdown.gameserver;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mageshowdown.gamelogic.*;
import com.mageshowdown.packets.Network;

public class ServerCollisionManager extends CollisionManager {
    private ServerGameStage gameStage;

    public ServerCollisionManager(ServerGameStage gameStage) {
        this.gameStage = gameStage;
    }


    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
        Object obj1 = contact.getFixtureA().getBody().getUserData(),
                obj2 = contact.getFixtureB().getBody().getUserData();


        if (obj1 instanceof Ammo && obj2 instanceof ServerPlayerCharacter) {
            handlePlayerAmmoCollision((Ammo) obj1, (ServerPlayerCharacter) obj2);
        } else if (obj1 instanceof ServerPlayerCharacter && obj2 instanceof Ammo) {
            handlePlayerAmmoCollision((Ammo) obj2, (ServerPlayerCharacter) obj1);
        }
    }



    private void handlePlayerAmmoCollision(Ammo ammo, ServerPlayerCharacter player) {
        //a player cant damage itself so we check if the ammo's owner id is the same as the player's it hit
        if (player.getId() != ammo.getOwnerId()) {
            Network.PlayerDead packet = new Network.PlayerDead();
            player.damageBy(ammo.getDAMAGE_VALUE(), ammo);
            ammo.setCollided(true);

            //if the player died from the hit
            if (player.getHealth() < 0) {
                //the player might have disconnected by the time the ammo hit something
                if(gameStage.getPlayerById(ammo.getOwnerId())!=null)
                    gameStage.getPlayerById(ammo.getOwnerId()).addKill();
                packet.id = player.getId();
                packet.respawnPos = GameServer.getInstance().generateSpawnPoint(packet.id);
                player.respawn(packet.respawnPos);
                gameStage.getPlayerById(ammo.getOwnerId()).raiseScore(1);
                GameServer.getInstance().sendToAllTCP(packet);
            }
        }
    }

}
