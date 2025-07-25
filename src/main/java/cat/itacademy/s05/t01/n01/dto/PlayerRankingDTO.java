package cat.itacademy.s05.t01.n01.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for player ranking information with position and win ratio")
public record PlayerRankingDTO(

        @Schema(description = "Ranking position of the player", example = "1")
        int position,

        @Schema(description = "Unique identifier of the player", example = "1")
        Long playerId,

        @Schema(description = "Name of the player", example = "Alice")
        String playerName,

        @Schema(description = "Number of games won by the player", example = "5")
        int winCount,

        @Schema(description = "Win ratio (wins / total games played)", example = "0.42")
        double winRatio

) {}