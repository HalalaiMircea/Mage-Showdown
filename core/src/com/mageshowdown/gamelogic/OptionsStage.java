package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.GameClient;
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

        Label playerNameLabel = new Label("Player name: ", ClientAssetLoader.interfaceSkin);
        final TextField playerNameField = new TextField("", ClientAssetLoader.interfaceSkin);
        TextButton backButton = new TextButton("Back", ClientAssetLoader.interfaceSkin);

        foreground.add(playerNameLabel).padBottom(20);
        foreground.add(playerNameField).padBottom(20);
        foreground.row();
        foreground.add(backButton).colspan(2);

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
                GameClient.getInstance().setUserName(playerNameField.getText());
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
