package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.security.User;

import java.util.List;
import java.util.UUID;

public class Observer {
    private UUID id;
    private String nickname;
    private List<String> roles;

    public Observer() {}

    public Observer(User user) {
        this.id = user.id();
        this.nickname = user.nickname();
        this.roles = user.roles();
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
}
