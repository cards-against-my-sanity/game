package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.security.User;

import java.util.List;
import java.util.UUID;

public class Player {
    private UUID id;
    private String nickname;
    private List<String> roles;
    private PlayerState state;
    private int score;
    private boolean needsToPlay;

    public Player() {}

    public Player(User user) {
        this.id = user.id();
        this.nickname = user.nickname();
        this.roles = user.roles();
        this.state = PlayerState.PLAYER;
        this.score = 0;
        this.needsToPlay = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isNeedsToPlay() {
        return needsToPlay;
    }

    public void setNeedsToPlay(boolean needsToPlay) {
        this.needsToPlay = needsToPlay;
    }
}
