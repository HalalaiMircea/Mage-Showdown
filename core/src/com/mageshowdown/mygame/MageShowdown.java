package com.mageshowdown.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

public class MageShowdown extends Game {
	private GameScreen gameScreen;

	@Override
	public void create () {
		GameWorld.setResolutionScale(Gdx.graphics.getWidth()/1280f);
		AssetLoader.load();

		gameScreen=new GameScreen(this);
	}

	@Override
	public void render () {
		super.render();

		Gdx.graphics.setTitle(GameWorld.getMousePos()+" ");

		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			gameScreen.start();
			this.setScreen(gameScreen);
		}

		GameWorld.world.step(Gdx.graphics.getDeltaTime(),6,2);
	}

	@Override
	public void dispose () {
		AssetLoader.dispose();
		gameScreen.dispose();
	}

}
