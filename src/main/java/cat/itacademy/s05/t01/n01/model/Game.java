package cat.itacademy.s05.t01.n01.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
@Document (collection = "games")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String id;
    private Long playerId;
    private List<Card>playerHand;
    private List<Card> dealerHand;
    private GameStatus status;
    private LocalDate createdAt;

}
