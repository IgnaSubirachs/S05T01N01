package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.mapper.GameMapper;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Card.Rank;
import cat.itacademy.s05.t01.n01.model.Card.Suit;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {

    private final GameMapper mapper = new GameMapper();

    @Test
    void toResponseDto_ShouldMapGameToResponseDTO() {
        Game game = new Game();
        game.setId("abc123");
        game.setPlayerId("123");
        game.setStatus(GameStatus.WON);
        game.setCreatedAt(LocalDate.now());


        List<Card> playerHand = List.of(new Card(Suit.SPADES, Rank.KING));
        List<Card> dealerHand = List.of(new Card(Suit.HEARTS, Rank.SEVEN));

        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);

        GameResponseDTO dto = mapper.toResponseDto(game);

        assertNotNull(dto);
        assertEquals("abc123", dto.id());
        assertEquals("123", dto.playerId());
        assertEquals(GameStatus.WON, dto.status());
        assertEquals(playerHand, dto.playerHand());
        assertEquals(dealerHand, dto.dealerHand());
    }
}
