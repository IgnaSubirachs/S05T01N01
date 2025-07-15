package cat.itacademy.s05.t01.n01.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for performing an action in a Blackjack game")
public record PlayRequestDTO(

        @Schema(description = "Action type: START, HIT or STAND", example = "HIT")
        String action,

        @Schema(description = "Bet amount (if applicable)", example = "10.5")
        Double bet
) {}