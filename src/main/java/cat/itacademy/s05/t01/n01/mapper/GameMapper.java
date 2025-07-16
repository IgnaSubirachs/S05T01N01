package cat.itacademy.s05.t01.n01.mapper;

import cat.itacademy.s05.t01.n01.dto.GameRequestDTO;
import cat.itacademy.s05.t01.n01.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GameMapper {

    public GameResponseDTO toResponseDto(Game game) {
        return new GameResponseDTO(
                game.getId(),
                game.getPlayerId(),
                game.getStatus(),
                game.getPlayerHand() != null ? game.getPlayerHand() : List.of(),
                game.getDealerHand() != null ? game.getDealerHand() : List.of()
        );
    }

    public Game toEntity(GameRequestDTO dto) {
        Game game = new Game();
        game.setPlayerId(dto.playerId());
        game.setStatus(GameStatus.PLAYING);
        return game;
    }
}