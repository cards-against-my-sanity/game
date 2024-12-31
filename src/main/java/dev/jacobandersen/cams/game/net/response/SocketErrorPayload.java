package dev.jacobandersen.cams.game.net.response;

public record SocketErrorPayload(String title, String message) {
    public SocketErrorPayload(String message) {
        this("Error", message);
    }
}
