package com.mageshowdown.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CreateBodies {

    public static Body createRectangleBody(Vector2 position, Vector2 size, Vector2 origin, BodyDef.BodyType bodyType, float density, float friction, float restitution, float rotation) {
        Body body;

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.angle = rotation * (float) Math.PI / 180;
        //box2d takes the coordinates of the world, so we have to scale the pixels
        Vector2 convPosition = GameWorld.convertPixelsToWorld(position);
        Vector2 convSize = GameWorld.convertPixelsToWorld(size);
        Vector2 convOrigin = GameWorld.convertPixelsToWorld(origin);
        //since we use vertices to create our polygon the origin of the body is the lower left corner
        bd.position.set(new Vector2(convPosition.x + convOrigin.x, convPosition.y + convOrigin.y));

        body = GameWorld.world.createBody(bd);
        Shape shape = createPolygonShape(convSize, convOrigin);
        body.createFixture(createFixtureDef(shape, density, friction, restitution));
        shape.dispose();

        return body;
    }

    private static PolygonShape createPolygonShape(Vector2 convSize, Vector2 convOrigin) {
        PolygonShape ps = new PolygonShape();
        //in order to be able to change the origin of the body without headaches we use vertices instead of setAsBox()
        Vector2[] vertices;
        /* TODO
         * rewrite this part in a better way
         */
        if (convOrigin.x == 0)
            vertices = new Vector2[]{new Vector2(0, -convSize.y / 2), new Vector2(0, convSize.y / 2), new Vector2(convSize.x, convSize.y / 2), new Vector2(convSize.x, -convSize.y / 2)};
        else
            vertices = new Vector2[]{new Vector2(-convSize.x / 2, -convSize.y / 2), new Vector2(-convSize.x / 2, convSize.y / 2), new Vector2(convSize.x / 2, convSize.y / 2), new Vector2(convSize.x / 2, -convSize.y / 2)};


        ps.set(vertices);

        return ps;
    }

    private static FixtureDef createFixtureDef(Shape shape, float density, float friction, float restitution) {
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = density;
        fd.friction = friction;
        fd.restitution = restitution;

        return fd;

    }

}
