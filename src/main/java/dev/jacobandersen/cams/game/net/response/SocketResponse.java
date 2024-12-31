package dev.jacobandersen.cams.game.net.response;

public record SocketResponse<T>(ResponseType type, T data, boolean isError, SocketErrorPayload error) {
    public SocketResponse(ResponseType type, T data) {
        this(type, data, false, null);
    }

    public SocketResponse(SocketErrorPayload error) {
        this(null, null, true, error);
    }
}
