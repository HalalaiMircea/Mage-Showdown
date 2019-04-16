package com.mageshowdown.mygame.gameclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

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
        {
            gameState=GameState.GAME_PAUSED;
            BasicRequest br=new BasicRequest();
            br.text="pressed ESCAPE";
            GameWorld.myClient.sendUDP(br);
        }
    }

    private void gamePausedInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState==GameState.GAME_PAUSED)
            gameState=GameState.GAME_RUNNING;
        else if(Gdx.input.isKeyPressed(Input.Keys.Q))
            Gdx.app.exit();
    }

    public GameStage getGameStage() {
        return gameStage;
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
