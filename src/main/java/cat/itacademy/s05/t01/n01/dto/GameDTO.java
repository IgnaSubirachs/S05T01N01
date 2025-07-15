package cat.itacademy.s05.t01.n01.dto;

import cat.itacademy.s05.t01.n01.model.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a Blackjack game")
public record GameDTO ( @Schema(description = "Unique identifier of the game", example = "64f8a21b3e5f2a4f234e9f10")
                        String id,

                        @Schema(description = "Identifier of the player who started the game", example = "1")
                        String playerId,

                        @Schema(description = "Current status of the game", example = "PLAYING")
                        GameStatus status

) {}

