package cat.itacademy.s05.t01.n01.mapper;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerRequestDTO;
import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player toEntity(PlayerRequestDTO dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Player name must not be blank");
        }

        return Player.builder()
                .name(dto.name())
                .totalWins(0)
                .build();
    }


    public PlayerDTO toDTO(Player player ,double winRatio) {
        return new PlayerDTO(
                player.getId(),
                player.getName(),
                player.getTotalWins(),
                winRatio
        );
    }

    public PlayerDTO toDTO(Player player) {
        return new PlayerDTO(
                player.getId(),
                player.getName(),
                player.getTotalWins(),
                0.0
        );
    }
}
