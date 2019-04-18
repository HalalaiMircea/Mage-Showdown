package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MapObjectHitbox extends GameActor {

    public MapObjectHitbox(Stage stage, Vector2 position, Vector2 size){
        super(stage,position,size);
        createBody(BodyDef.BodyType.StaticBody);
    }

    //we override the base draw method with an empty one as we dont want these hitboxes to be drawn
    @Override
    public void draw(Batch batch, float parentAlpha) {
    }
}
