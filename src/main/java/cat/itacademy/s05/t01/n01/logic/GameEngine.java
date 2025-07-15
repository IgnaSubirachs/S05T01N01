package cat.itacademy.s05.t01.n01.logic;

import cat.itacademy.s05.t01.n01.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class GameEngine {

    private final Random random = new Random();

    public Card drawCard() {
        Card.Suit suit = Card.Suit.values()[random.nextInt(Card.Suit.values().length)];
        Card.Rank rank = Card.Rank.values()[random.nextInt(Card.Rank.values().length)];
        return new Card(suit, rank);
    }

    public int calculateHandValue(List<Card> hand) {
        int total = 0;
        int aceCount = 0;

        for (Card card : hand) {
            int value = card.getRank().getValue();
            total += value;
            if (card.getRank() == Card.Rank.ACE) {
                aceCount++;
            }
        }
        while (aceCount > 0 && total + 10 <= 21) {
            total += 10;
            aceCount--;
        }

        return total;
    }

    public GameStatus evaluateWinner(List<Card> playerHand, List<Card> dealerHand) {
        int playerScore = calculateHandValue(playerHand);
        int dealerScore = calculateHandValue(dealerHand);

        if (playerScore > 21) return GameStatus.LOST;
        if (dealerScore > 21) return GameStatus.WON;
        if (playerScore > dealerScore) return GameStatus.WON;
        if (playerScore < dealerScore) return GameStatus.LOST;
        return GameStatus.DRAW;
    }

    public Game applyHit(Game game) {
        Card newCard = drawCard();
        game.getPlayerHand().add(newCard);

        int playerValue = calculateHandValue(game.getPlayerHand());
        if (playerValue > 21) {
            game.setStatus(GameStatus.LOST);
        }

        return game;
    }

    public List<Card> playerDealerTurn(List<Card> dealerHand) {
        while (calculateHandValue(dealerHand) < 17) {
            dealerHand.add(drawCard());
        }
        return dealerHand;
    }
}
