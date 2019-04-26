package com.mageshowdown.gameclient;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class GameLauncher extends ApplicationAdapter {
    private Stage stage;

    @Override
    public void create() {
        ClientAssetLoader.load();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        Label label = new Label("TEST", ClientAssetLoader.uiSkin);
        TextButton launchButton = new TextButton("Launch game!", ClientAssetLoader.uiSkin);
        table.add(label);
        table.row();
        table.add(launchButton);

        launchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        stage.addActor(table);
    }

    @Override
    public void render() {
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        ClientAssetLoader.dispose();
    }
}
