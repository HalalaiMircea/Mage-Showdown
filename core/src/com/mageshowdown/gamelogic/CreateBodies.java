package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CreateBodies {

    public static Body createRectangleBody(Vector2 position, Vector2 size, BodyDef.BodyType bodyType, float density, float friction, float restitution, float rotation){
        Body body;

        BodyDef bd=new BodyDef();
        bd.type=bodyType;
        bd.angle=rotation*(float)Math.PI/180;
        //box2d takes the coordinates of the world, so we have to scale the pixels
        Vector2 convPosition=GameWorld.convertPixelsToWorld(position);
        Vector2 convSize=GameWorld.convertPixelsToWorld(size);
        //since we use vertices to create our polygon the origin of the body is the lower left corner
        bd.position.set(new Vector2(convPosition.x,convPosition.y));

        body=GameWorld.world.createBody(bd);
        Shape shape = createPolygonShape(convSize);
        body.createFixture(createFixtureDef(shape,density,friction,restitution));
        shape.dispose();

        return body;
    }

    private static PolygonShape createPolygonShape(Vector2 convSize){
        PolygonShape ps=new PolygonShape();
        //in order to be able to change the origin without headaches we use vertices instead of setAsBox()
        Vector2 vertices[]=new Vector2[]{new Vector2(0,convSize.y),new Vector2(0,0),new Vector2(convSize.x,0),new Vector2(convSize.x,convSize.y)};

        ps.set(vertices);

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
