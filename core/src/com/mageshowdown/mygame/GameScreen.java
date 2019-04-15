package com.mageshowdown.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;

import java.io.InputStreamReader;

public class GameScreen implements Screen {

    enum GameState{
        GAME_READY,
        GAME_RUNNING,
        GAME_PAUSED
    }

    private Game game;
    private GameStage gameStage;
    private GameState gameState;

    public GameScreen(Game game) {
        super();
        this.game=game;
        gameStage=new GameStage();
        gameState=GameState.GAME_READY;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch(gameState){
            case GAME_READY:
                gameReadyInput();
                break;
            case GAME_RUNNING:
                gameStage.act();
                gameRunningInput();
                gameStage.draw();
                break;
            case GAME_PAUSED:
                gamePausedInput();
                gameStage.draw();
                break;
        }
    }

    public void start(){
        gameStage.start();
    }

    private void gameReadyInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
            gameState=GameState.GAME_RUNNING;
    }

    private void gameRunningInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState==GameState.GAME_RUNNING)
            gameState=GameState.GAME_PAUSED;
    }

    private void gamePausedInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState==GameState.GAME_PAUSED)
            gameState=GameState.GAME_RUNNING;
        else if(Gdx.input.isKeyPressed(Input.Keys.Q))
            Gdx.app.exit();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameStage.dispose();
    }
}
