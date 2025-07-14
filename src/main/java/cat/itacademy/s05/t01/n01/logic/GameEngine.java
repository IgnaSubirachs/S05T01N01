package cat.itacademy.s05.t01.n01.logic;


import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class GameEngine {
    private final Random random = new Random();

    public Card drawCard(){
        int value = random.nextInt(11)+1;
        return new Card("CARD_"+value,"SPADES",value);
    }

    public int calculateHandValue(List<Card> hand){
        return hand.stream()
                .mapToInt(Card::getValue)
                .sum();
    }

    public GameStatus evaluateWinner(List<Card>playerHand, List<Card>dealerHand){
        int playerScore = calculateHandValue(playerHand);
        int dealerScore = calculateHandValue(dealerHand);
        if(playerScore > 21) return GameStatus.LOST;
        if(dealerScore > 21) return GameStatus.WON;
        if(playerScore >dealerScore) return GameStatus.WON;
        if(playerScore < dealerScore)return GameStatus.LOST;
        return GameStatus.DRAW;
    }
}
