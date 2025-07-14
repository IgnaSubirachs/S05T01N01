package cat.itacademy.s05.t01.n01.mapper;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameDTO toDto(Game game) {
        return new GameDTO(
                game.getId(),
                game.getPlayerId(),
                game.getStatus()
        );
    }

    public Game toEntity(GameDTO dto) {
        Game game = new Game();
        game.setId(dto.id());
        game.setPlayerId(dto.playerId());
        game.setStatus(dto.status());
        return game;
    }
}

