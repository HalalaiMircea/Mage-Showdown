package com.mageshowdown.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MageShowdown extends ApplicationAdapter {
	GameStage gameStage;

	@Override
	public void create () {
		GameWorld.setResolutionScale(Gdx.graphics.getWidth()/1280f);
		AssetLoader.load();

		GameWorld.world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Object obj1=contact.getFixtureA().getUserData(),
						obj2=contact.getFixtureB().getUserData();

				System.out.println("hit wall!!!");
				if(obj1 instanceof Projectile)
					((Projectile) obj1).setCollided(true);
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
		});
		ScreenViewport viewport=new ScreenViewport();

		gameStage=new GameStage(viewport);
		gameStage.create();
		Gdx.input.setInputProcessor(gameStage);
	}

	@Override
	public void render () {
		Gdx.graphics.setTitle(GameWorld.getMousePos()+" ");

		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();
		gameStage.act();


		Gdx.gl.glClearColor(255, 255, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStage.draw();

		GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
	}

	@Override
	public void dispose () {
		AssetLoader.dispose();
		gameStage.dispose();
	}

}
