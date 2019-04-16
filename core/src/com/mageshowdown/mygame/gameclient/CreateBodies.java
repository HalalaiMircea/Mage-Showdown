package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CreateBodies {
    public static Body createRectangleBody(Vector2 position, Vector2 size, BodyDef.BodyType bodyType, float density, float friction, float restitution){
        Body body;

        BodyDef bd=new BodyDef();
        bd.type=bodyType;
        //box2d takes the coordinates of the world, so we have to scale the pixels
        Vector2 convPosition=GameWorld.convertPixelsToWorld(position);
        Vector2 convSize=GameWorld.convertPixelsToWorld(size);
        //in box2d the position of a body is its center, so we have to offset the sprite position by half its size
        bd.position.set(new Vector2(convPosition.x+(convSize.x/2),convPosition.y+(convSize.y/2)));

        body=GameWorld.world.createBody(bd);
        Shape shape = createPolygonShape(size);
        body.createFixture(createFixtureDef(shape,density,friction,restitution));
        shape.dispose();

        return body;
    }


    private static CircleShape createCircleShape(float radius){
        CircleShape cs=new CircleShape();
        cs.setRadius(radius);

        return cs;
    }

    private static PolygonShape createPolygonShape(Vector2 size){
        PolygonShape ps=new PolygonShape();
        Vector2 convSize=GameWorld.convertPixelsToWorld(size);
        ps.setAsBox(convSize.x/2f,convSize.y/2f);

        return ps;
    }

    private static FixtureDef createFixtureDef(Shape shape, float density, float friction, float restitution){
        FixtureDef fd=new FixtureDef();
        fd.shape=shape;
        fd.density=density;
        fd.friction=friction;
        fd.restitution=restitution;

        return fd;

    }

}
