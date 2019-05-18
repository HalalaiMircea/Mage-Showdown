package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gameclient.ClientAssetLoader;

public class FreezeProjectile extends Ammo {

    public FreezeProjectile(Stage stage, Vector2 position, float rotation, Vector2 direction, int id, int ownerId){
        super(stage,new Vector2(3.5f*direction.x,3.5f*direction.y),position,new Vector2(46,31),ClientAssetLoader.freezeProjectileTexture,new Vector2(.75f,.75f),rotation,id,ownerId,3);
        createBody(rotation,BodyDef.BodyType.DynamicBody);
    }
}
