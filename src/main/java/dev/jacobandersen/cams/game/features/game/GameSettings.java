package dev.jacobandersen.cams.game.features.game;

public class GameSettings {
    private int maxPlayers;
    private int maxObservers;
    private int maxScore;
    private int roundIntermissionTimer;
    private int gameWinIntermissionTimer;
    private int playingTimer;
    private int judgingTimer;
    private boolean allowPlayersToJoinMidGame;

    public GameSettings() {
        maxPlayers = 10;
        maxObservers = 10;
        maxScore = 7;
        roundIntermissionTimer = 8;
        gameWinIntermissionTimer = 10;
        playingTimer = 120;
        judgingTimer = 90;
        allowPlayersToJoinMidGame = false;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxObservers() {
        return maxObservers;
    }

    public void setMaxObservers(int maxObservers) {
        this.maxObservers = maxObservers;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getRoundIntermissionTimer() {
        return roundIntermissionTimer;
    }

    public void setRoundIntermissionTimer(int roundIntermissionTimer) {
        this.roundIntermissionTimer = roundIntermissionTimer;
    }

    public int getGameWinIntermissionTimer() {
        return gameWinIntermissionTimer;
    }

    public void setGameWinIntermissionTimer(int gameWinIntermissionTimer) {
        this.gameWinIntermissionTimer = gameWinIntermissionTimer;
    }

    public int getPlayingTimer() {
        return playingTimer;
    }

    public void setPlayingTimer(int playingTimer) {
        this.playingTimer = playingTimer;
    }

    public int getJudgingTimer() {
        return judgingTimer;
    }

    public void setJudgingTimer(int judgingTimer) {
        this.judgingTimer = judgingTimer;
    }

    public boolean isAllowPlayersToJoinMidGame() {
        return allowPlayersToJoinMidGame;
    }

    public void setAllowPlayersToJoinMidGame(boolean allowPlayersToJoinMidGame) {
        this.allowPlayersToJoinMidGame = allowPlayersToJoinMidGame;
    }
}
