package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private GameEngine gameEngine;

     @BeforeEach
    void setUp() {
         gameEngine = new GameEngine();
    }

    @Test
    void drawCard_ReturnsValidCard(){
         Card card = gameEngine.drawCard();
         assertNotNull(card);
         assertTrue(card.getValue()>=1 && card.getValue()<=11);
         assertNotNull(card.getSuit());
         assertNotNull(card.getRank());
    }
    @Test
    void evaluateWinner_PlayerWins() {
        List<Card> player = List.of(new Card("X", "X", 10), new Card("X", "X", 5));
        List<Card> dealer = List.of(new Card("X", "X", 8), new Card("X", "X", 4));

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.WON, result);
    }

    @Test
    void evaluateWinner_DealerWins(){
        List<Card> player = List.of(new Card("X", "X", 6));
        List<Card> dealer = List.of(new Card("X", "X", 10));

        GameStatus result = gameEngine.evaluateWinner(player , dealer);
        assertEquals(GameStatus.LOST, result);
    }

    @Test
    void evaluateWinner_Draw() {
        List<Card> player = List.of(new Card("X", "X", 9));
        List<Card> dealer = List.of(new Card("X", "X", 9));

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.DRAW, result);
    }

    @Test
    void evaluateWinner_PlayerBusts() {
        List<Card> player = List.of(new Card("X", "X", 10), new Card("X", "X", 8), new Card("X", "X", 5)); // 23
        List<Card> dealer = List.of(new Card("X", "X", 7), new Card("X", "X", 5)); // 12

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.LOST, result);
    }

    @Test
    void evaluateWinner_DealerBusts() {
        List<Card> player = List.of(new Card("X", "X", 10), new Card("X", "X", 5)); // 15
        List<Card> dealer = List.of(new Card("X", "X", 10), new Card("X", "X", 10), new Card("X", "X", 2)); // 22

        GameStatus result = gameEngine.evaluateWinner(player, dealer);
        assertEquals(GameStatus.WON, result);
    }
}


