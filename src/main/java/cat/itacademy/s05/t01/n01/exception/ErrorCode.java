package cat.itacademy.s05.t01.n01.exception;

public enum ErrorCode {
    PLAYER_NOT_FOUND("Player not found"),
    GAME_NOT_FOUND("Game not found"),
    INVALID_GAME_ACTION("Invalid game action"),
    INVALID_GAME_STATE("Game is not in PLAYING state"),
    INVALID_ACTION("Invalid action"),
    DATABASE_ERROR("Database connection failed");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
