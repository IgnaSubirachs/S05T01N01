package cat.itacademy.s05.t01.n01.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a player in the Blackjack game")
public record PlayerDTO(

        @Schema(description = "Unique identifier of the player", example = "1")
        Long id,

        @Schema(description = "Name of the player", example = "Alice")
        String name,

        @Schema(description = "Total number of games won by the player", example = "3")
        int totalWins

) {}