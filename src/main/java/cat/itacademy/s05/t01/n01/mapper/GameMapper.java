package cat.itacademy.s05.t01.n01.mapper;

import cat.itacademy.s05.t01.n01.dto.GameRequestDTO;
import cat.itacademy.s05.t01.n01.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameResponseDTO toResponseDto(Game game) {
        return new GameResponseDTO(
                game.getId(),
                game.getPlayerId(),
                game.getStatus()
        );
    }

    public Game toEntity(GameRequestDTO requestDto) {
        Game game = new Game();
        game.setPlayerId(requestDto.playerId());
        return game;
    }
}
