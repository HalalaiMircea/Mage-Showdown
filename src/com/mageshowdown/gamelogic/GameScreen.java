package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mageshowdown.gameclient.ClientGameStage;
import com.mageshowdown.gameclient.MageShowdownClient;

public class GameScreen implements Screen {

    enum GameState {
        GAME_READY,
        GAME_RUNNING,
        GAME_PAUSED
    }

    private final MageShowdownClient game;
    private ClientGameStage gameStage;
    private GameState gameState;

    public GameScreen(final MageShowdownClient game) {
        super();
        this.game = game;
        gameStage = new ClientGameStage();
        gameState = GameState.GAME_READY;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
//            case GAME_READY:
//                gameReadyInput();
//                break;
            case GAME_RUNNING:
                gameStage.act();
                gameRunningInput();
                gameStage.draw();
                break;
            case GAME_PAUSED:
                gameStage.act();
                gamePausedInput();
                gameStage.draw();
                break;
        }

    }

    public void start() {
        gameStage.start();
    }

//    private void gameReadyInput() {
//        if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
//            gameState = GameState.GAME_RUNNING;
//    }

    private void gameRunningInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_RUNNING) {
            gameState = GameState.GAME_PAUSED;
            gameStage.setDrawOverlay(true);
        }
    }

    private void gamePausedInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState == GameState.GAME_PAUSED) {
            gameState = GameState.GAME_RUNNING;
            gameStage.setDrawOverlay(false);
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q))
            Gdx.app.exit();
    }

    public ClientGameStage getGameStage() {
        return gameStage;
    }

    //setter to access gameState member variable from MenuScreen class
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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
