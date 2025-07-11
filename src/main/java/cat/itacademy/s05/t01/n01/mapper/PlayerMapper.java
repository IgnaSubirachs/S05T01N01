package cat.itacademy.s05.t01.n01.mapper;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player toEntity(PlayerDTO dto) {
        return Player.builder()
                .id(null)
                .name(dto.name())
                .totalWins(dto.totalWins())
                .build();
    }

    public PlayerDTO toDTO(Player player) {
        return new PlayerDTO(
                player.getId(),
                player.getName(),
                player.getTotalWins()
        );
    }
}
