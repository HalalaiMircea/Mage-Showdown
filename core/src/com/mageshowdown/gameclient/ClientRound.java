package com.mageshowdown.gameclient;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mageshowdown.gamelogic.GameScreen;
import com.mageshowdown.gamelogic.Round;

public class ClientRound extends Round {

    private static final ClientRound INSTANCE = new ClientRound();

    private ClientRound() {
        super();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public static ClientRound getInstance() {
        return INSTANCE;
    }
}
