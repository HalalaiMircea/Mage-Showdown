package com.mageshowdown.gameclient;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.kryonet.Connection;
import com.mageshowdown.gamelogic.GameScreen;
import com.mageshowdown.gamelogic.Laser;
import com.mageshowdown.gamelogic.Round;
import com.mageshowdown.gameserver.GameServer;
import com.mageshowdown.gameserver.ServerPlayerCharacter;

public class ClientRound extends Round {

    private class RoundEndManager extends Thread {
        RoundEndManager() {
            start();
        }

        @Override
        public void run() {
            super.run();
            Laser.BurningEffect.clearBurningEffects();
            GameScreen.getInstance().getGameStage().clearSpells();
        }
    }

    private static final ClientRound INSTANCE = new ClientRound();

    private ClientRound() {
        super();
    }

    @Override
    protected void roundHasEnded() {
        new RoundEndManager();
    }

    public static ClientRound getInstance() {
        return INSTANCE;
    }
}
