package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mageshowdown.gameclient.ClientGameStage;
import com.mageshowdown.gameclient.ClientPlayerCharacter;
import com.mageshowdown.gameclient.ClientRound;

import java.util.HashMap;

import static com.mageshowdown.gameclient.ClientAssetLoader.uiSkin;

public class ScoreboardStage extends Stage {
    private static ScoreboardStage ourInstance = new ScoreboardStage();

    private Table root;
    private final int WIDTH = 800;
    private final int HEIGHT = 400;
    private ClientGameStage gameStage = GameScreen.getGameStage();
    private ClientRound round = ClientRound.getInstance();
    private Label timeLeftLabel;

    private ScoreboardStage() {
        super(MenuScreen.getMainMenuStage().getViewport(), GameScreen.getGameStage().getBatch());

        root = new Table(uiSkin);
        root.setBackground("default-window");
        root.setColor(0, 0, 0, 0.8f);
        root.setSize(WIDTH, HEIGHT);
        root.setPosition(Gdx.graphics.getWidth() / 2 - WIDTH / 2, Gdx.graphics.getHeight() / 2 - HEIGHT / 2);
        root.debug();

        timeLeftLabel = new Label("", uiSkin);

        root.top();
        root.add(timeLeftLabel);
        root.row();

        root.defaults().pad(0, 0, 0, 10);
        HashMap<Integer, ClientPlayerCharacter> otherPlayers = gameStage.getOtherPlayers();
        root.add(new Label(gameStage.getPlayerCharacter().getUserName(), uiSkin));
        root.add(new Label(Integer.toString(gameStage.getPlayerCharacter().getScore()), uiSkin));
        root.row();
        for (HashMap.Entry<Integer, ClientPlayerCharacter> each : otherPlayers.entrySet()) {
            Label tempLabel = new Label(each.getValue().getUserName(), uiSkin);
            root.add(tempLabel);
            root.add(new Label(Integer.toString(each.getValue().getScore()), uiSkin));
            root.row();
        }

        this.addActor(root);
    }

    @Override
    public void act() {
        timeLeftLabel.setText(Integer.toString((int) (round.ROUND_LENGTH - round.timePassed)));

        super.act();
    }

    public static ScoreboardStage getInstance() {
        return ourInstance;
    }
}
