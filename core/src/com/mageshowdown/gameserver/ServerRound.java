package com.mageshowdown.gameserver;

import com.esotericsoftware.kryonet.Connection;
import com.mageshowdown.gamelogic.Round;

public class ServerRound extends Round {

    private class RoundEndManager extends Thread {
        RoundEndManager() {
            start();
        }

        @Override
        public void run() {
            super.run();
            GameServer.getInstance().registerRound();
            GameServer.getInstance().getGameStage().clearSpells();
            for (Connection x : GameServer.getInstance().getConnections()) {
                ServerPlayerCharacter pc = GameServer.getInstance().getGameStage().getPlayerById(x.getID());

                if (pc != null) {
                    pc.respawn(GameServer.getInstance().generateSpawnPoint(x.getID()));
                    pc.setScore(0);
                    pc.setKills(0);
                }
            }
        }
    }

    private static ServerRound instance = new ServerRound(25f);


    private float mapChangeInterval;
    private int mapsChanged = 0;

    public ServerRound(float mapChangeInterval) {
        super();
        this.mapChangeInterval = mapChangeInterval;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (timePassed - mapChangeInterval * mapsChanged > mapChangeInterval) {
            GameServer.getInstance().sendMapChange(GameServer.getInstance().getARandomMap());
            mapsChanged++;
        }
    }

    @Override
    public void stop() {
        super.stop();
        mapsChanged = 0;
    }

    @Override
    protected void roundHasEnded() {
        new RoundEndManager();
    }

    public static ServerRound getInstance() {
        return instance;
    }
}
