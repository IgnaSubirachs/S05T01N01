package cat.itacademy.s05.t01.n01.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Full internal model representing a Blackjack game stored in MongoDB.")
@Document(collection = "games")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Schema(description = "Unique identifier of the game.", example = "64b7f13d6d7e8a32f9a12c45")
    private String id;

    @Schema(description = "Identifier of the player linked to this game.", example = "1001")
    private String playerId;

    @Schema(description = "List of cards currently held by the player.")
    private List<Card> playerHand;

    @Schema(description = "List of cards currently held by the dealer.")
    private List<Card> dealerHand;

    @Schema(description = "Current status of the game.", implementation = GameStatus.class)
    private GameStatus status;

    @Schema(description = "Date when the game was created.", example = "2025-07-15")
    private LocalDate createdAt;
}
