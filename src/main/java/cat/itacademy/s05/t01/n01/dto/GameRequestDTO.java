package cat.itacademy.s05.t01.n01.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating or updating a Blackjack game")
public record GameRequestDTO(

        @Schema(description = "ID of the player who is playing the game", example = "1")
        String playerId

) {}
