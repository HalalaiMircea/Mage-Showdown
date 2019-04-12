package com.mageshowdown.mygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Platform extends GameActor {

    public Platform(Stage stage, Vector2 position, Vector2 size,Texture texture){
        super(stage,position,size,texture);
        createBody(BodyDef.BodyType.StaticBody);
    }

}
