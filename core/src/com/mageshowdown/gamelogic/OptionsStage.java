package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.MageShowdownClient;

public class OptionsStage extends Stage {

    public OptionsStage(Viewport viewport, Batch batch, Texture backgroundTexture) {
        super(viewport, batch);
        Table background = new Table();
        Image bgImage = new Image(backgroundTexture);
        if (backgroundTexture.equals(ClientAssetLoader.solidBlack))
            bgImage.setColor(0, 0, 0, 0.8f);
        background.add(bgImage);
        background.setFillParent(true);

        Table foreground = new Table();
        foreground.setFillParent(true);
        foreground.debug();

        TextButton backButton = new TextButton("Back", ClientAssetLoader.interfaceSkin);

        foreground.add(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (MageShowdownClient.getInstance().getScreen().equals(MenuScreen.getInstance())) {
                    MenuScreen.setStagePhase(MenuScreen.StagePhase.MAIN_MENU_STAGE);
                    Gdx.input.setInputProcessor(MenuScreen.getMainMenuStage());
                }
                if (MageShowdownClient.getInstance().getScreen().equals(GameScreen.getInstance())) {
                    GameScreen.setGameState(GameScreen.GameState.GAME_PAUSED);
                    Gdx.input.setInputProcessor(GameScreen.getEscMenuStage());
                }
            }
        });
        this.addActor(background);
        this.addActor(foreground);
    }

    @Override
    public void act() {
        super.act();
    }

    @Override
    public void draw() {
        super.draw();
    }
}
