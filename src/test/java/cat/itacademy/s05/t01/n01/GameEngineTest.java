package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {

    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
    }

    @Test
    void drawCard_ReturnsValidCard() {
        Card card = gameEngine.drawCard();
        assertNotNull(card);
        assertNotNull(card.getSuit());
        assertNotNull(card.getRank());

        int value = card.getRank().getValue();
        assertTrue(value >= 1 && value <= 11);
    }

    @Test
    void evaluateWinner_PlayerWins() {
        List<Card> player = List.of(
                new Card(Card.Suit.HEARTS, Card.Rank.EIGHT),
                new Card(Card.Suit.SPADES, Card.Rank.SEVEN)); // 15
        List<Card> dealer = List.of(
                new Card(Card.Suit.CLUBS, Card.Rank.SIX),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR)); // 10

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.WON, result);
    }

    @Test
    void evaluateWinner_DealerWins() {
        List<Card> player = List.of(new Card(Card.Suit.SPADES, Card.Rank.FIVE));
        List<Card> dealer = List.of(new Card(Card.Suit.HEARTS, Card.Rank.TEN));

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.LOST, result);
    }

    @Test
    void evaluateWinner_Draw() {
        List<Card> player = List.of(new Card(Card.Suit.HEARTS, Card.Rank.NINE));
        List<Card> dealer = List.of(new Card(Card.Suit.SPADES, Card.Rank.NINE));

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.DRAW, result);
    }

    @Test
    void evaluateWinner_PlayerBusts() {
        List<Card> player = List.of(
                new Card(Card.Suit.HEARTS, Card.Rank.TEN),
                new Card(Card.Suit.CLUBS, Card.Rank.EIGHT),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE)); // 23
        List<Card> dealer = List.of(
                new Card(Card.Suit.SPADES, Card.Rank.SEVEN),
                new Card(Card.Suit.HEARTS, Card.Rank.FIVE)); // 12

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.LOST, result);
    }

    @Test
    void evaluateWinner_DealerBusts() {
        List<Card> player = List.of(
                new Card(Card.Suit.HEARTS, Card.Rank.TEN),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE)); // 15
        List<Card> dealer = List.of(
                new Card(Card.Suit.DIAMONDS, Card.Rank.TEN),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO)); // 22

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.WON, result);
    }

    @Test
    void applyHit_shouldAddCardAndRemainPlaying_whenPlayerValueIsUnder21() {
        Game game = new Game();
        game.setStatus(GameStatus.PLAYING);
        game.setPlayerHand(new ArrayList<>());

        game.getPlayerHand().add(new Card(Card.Suit.HEARTS, Card.Rank.TEN));
        Game result = gameEngine.applyHit(game);

        assertEquals(GameStatus.PLAYING, result.getStatus());
        assertEquals(2, result.getPlayerHand().size());

        int value = gameEngine.calculateHandValue(result.getPlayerHand());
        assertTrue(value <= 21);
    }

    @Test
    void applyHit_shouldSetStatusToLost_whenPlayerValueExceeds21() {
        Game game = new Game();
        game.setStatus(GameStatus.PLAYING);
        game.setPlayerHand(new ArrayList<>());

        game.getPlayerHand().add(new Card(Card.Suit.HEARTS, Card.Rank.TEN));
        game.getPlayerHand().add(new Card(Card.Suit.SPADES, Card.Rank.TEN));
        game.getPlayerHand().add(new Card(Card.Suit.CLUBS, Card.Rank.TWO)); // 22

        Game result = gameEngine.applyHit(game);

        int handValue = gameEngine.calculateHandValue(result.getPlayerHand());
        assertTrue(handValue > 21);
        assertEquals(GameStatus.LOST, result.getStatus());
    }

    @Test
    void playDealerTurn_shouldDrawUntilSeventeen() {
        List<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card(Card.Suit.SPADES, Card.Rank.SIX)); // 6

        List<Card> result = gameEngine.playerDealerTurn(dealerHand);

        int total = gameEngine.calculateHandValue(result);
        assertTrue(total >= 17);
    }
}
