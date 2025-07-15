package cat.itacademy.s05.t01.n01.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible statuses of a Blackjack game.")
public enum GameStatus {

    @Schema(description = "The game is currently in progress.")
    PLAYING,

    @Schema(description = "The player has won the game.")
    WON,

    @Schema(description = "The player has lost the game.")
    LOST,

    @Schema(description = "The game ended in a draw between player and dealer.")
    DRAW
}