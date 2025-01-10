package dev.jacobandersen.cams.game.net.response;

public record SocketResponse<T>(ResponseType type, T data, boolean isError, SocketErrorPayload error) {
    private SocketResponse(ResponseType type, T data) {
        this(type, data, false, null);
    }

    public static <T> SocketResponse<T> ok(ResponseType type, T data) {
        return new SocketResponse<>(type, data, false, null);
    }

    public static <T> SocketResponse<T> error(SocketErrorPayload error) {
        return error(null, error);
    }

    public static <T> SocketResponse<T> error(ResponseType type, SocketErrorPayload error) {
        return new SocketResponse<>(type, null, true, error);
    }
}
