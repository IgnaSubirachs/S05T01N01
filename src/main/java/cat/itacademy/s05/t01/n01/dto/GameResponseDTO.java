        package cat.itacademy.s05.t01.n01.dto;

        import cat.itacademy.s05.t01.n01.model.Card;
        import cat.itacademy.s05.t01.n01.model.GameStatus;
        import io.swagger.v3.oas.annotations.media.Schema;

        import java.util.List;

        @Schema(description = "Response DTO representing a Blackjack game")
        public record GameResponseDTO(

                @Schema(description = "Unique identifier of the game", example = "abcd1234")
                String id,

                @Schema(description = "ID of the player who is playing the game", example = "1")
                String playerId,

                @Schema(description = "Current status of the game", example = "PLAYING")
                GameStatus status,

                @Schema(description = "List of cards in the player's hand")
                List<Card> playerHand,

                @Schema(description = "List of cards in the dealer's hand")
                List<Card> dealerHand

        ) {}
