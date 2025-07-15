package cat.itacademy.s05.t01.n01.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Represents a playing card in the Blackjack game.")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Card {

    @Schema(description = "Suit of the card.", example = "HEARTS")
    private Suit suit;

    @Schema(description = "Rank of the card.", example = "KING")
    private Rank rank;

    public enum Suit {
        HEARTS,
        DIAMONDS,
        CLUBS,
        SPADES
    }

    public enum Rank {
        ACE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        QUEEN(10),
        KING(10);
        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }



}


