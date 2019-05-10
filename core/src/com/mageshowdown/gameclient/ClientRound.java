package com.mageshowdown.gameclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mageshowdown.gamelogic.Round;

public class ClientRound extends Round {

    private static final ClientRound INSTANCE = new ClientRound();

    private Sprite roundOverSprite;
    private BitmapFont font;

    private ClientRound() {
        super();
        font = ClientAssetLoader.font1;
        roundOverSprite = new Sprite(ClientAssetLoader.roundOverScreen);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, Float.toString(timePassed), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        if (finished) {
            roundOverSprite.draw(batch);
            font.draw(batch, Float.toString(timePassedRoundFinished), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
    }

    public static ClientRound getInstance() {
        return INSTANCE;
    }

}
