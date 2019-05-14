package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.mageshowdown.gameclient.ClientAssetLoader;
import com.mageshowdown.gameclient.ClientGameStage;
import com.mageshowdown.gameclient.ClientPlayerCharacter;
import com.mageshowdown.gameclient.ClientRound;

import java.util.Map;

import static com.mageshowdown.gameclient.ClientAssetLoader.uiSkin;

public class ScoreboardStage extends Stage {

    private static ScoreboardStage ourInstance = new ScoreboardStage();

    private final int WIDTH = 800;
    private final int HEIGHT = 400;
    private ClientGameStage gameStage = GameScreen.getGameStage();
    private ClientRound round = ClientRound.getInstance();
    private Label timeLeftLabel;
    private PlayerStatsList psl;

    private class PlayerStatsList {
        Array<String> playerNames;
        Array<Integer> playerKills;
        Array<Integer> playerScore;
        List<String> nameListWidget = new List<String>(uiSkin);
        List<Integer> killsListWidget = new List<Integer>(uiSkin);
        List<Integer> scoreListWidget = new List<Integer>(uiSkin);

        private PlayerStatsList() {
            playerNames = new Array<String>();
            playerKills = new Array<Integer>();
            playerScore = new Array<Integer>();
        }

        void update() {
            playerNames.clear();
            playerKills.clear();
            playerScore.clear();
            for (Map.Entry<Integer, ClientPlayerCharacter> each : gameStage.getSortedPlayers().entrySet()) {
                playerNames.add(each.getValue().getUserName());
                playerKills.add(each.getValue().getKills());
                playerScore.add(each.getValue().getScore());
            }
            nameListWidget.setItems(playerNames);
            killsListWidget.setItems(playerKills);
            scoreListWidget.setItems(playerScore);
        }
    }

    private ScoreboardStage() {
        super(MenuScreen.getMainMenuStage().getViewport());

        psl = new PlayerStatsList();
        Table root = new Table(uiSkin);
        root.setBackground("default-window");
        root.setColor(0, 0, 0, 0.8f);
        root.setSize(WIDTH, HEIGHT);
        root.setPosition(Gdx.graphics.getWidth() / 2 - WIDTH / 2, Gdx.graphics.getHeight() / 2 - HEIGHT / 2);
        root.debug();

        Label.LabelStyle timeLeftStyle = new Label.LabelStyle(ClientAssetLoader.bigSizeFont, Color.WHITE);
        timeLeftLabel = new Label("", timeLeftStyle);

        root.top();
        root.add(timeLeftLabel).expandX().colspan(3);
        root.row();
        root.defaults().pad(1, 1, 1, 1).center().fill();
        root.add(new Label("Player Name", uiSkin));
        root.add(new Label("Kills", uiSkin));
        root.add(new Label("Score", uiSkin));
        root.row();

        root.add(psl.nameListWidget);
        root.add(psl.killsListWidget);
        root.add(psl.scoreListWidget);

        this.addActor(root);
    }

    public static ScoreboardStage getInstance() {
        return ourInstance;
    }

    @Override
    public void act() {
        super.act();

        timeLeftLabel.setText("TIME LEFT " + (int) (round.ROUND_LENGTH - round.timePassed));
        psl.update();
    }
}
