package com.mageshowdown.gamelogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientRound;

public class RoundEndStage extends Stage {
    private Label roundEndLabel;

    public RoundEndStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        roundEndLabel = new Label("", ClientAssetLoader.uiSkin.get("menu-label", Label.LabelStyle.class));
        Container<Label> wrapper = new Container<Label>(roundEndLabel);
        wrapper.setFillParent(true);
        this.addActor(wrapper);
    }

    @Override
    public void act() {
        super.act();
        roundEndLabel.setText("NEW ROUND STARTS IN: " + (int) ClientRound.getInstance().getTimePassedRoundFinished());
    }

    @Override
    public void draw() {
        super.draw();
    }
}
