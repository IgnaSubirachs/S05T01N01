package cat.itacademy.s05.t01.n01.mapper;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.model.Player;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface PlayerMapper {

    PlayerDTO toDTO(Player player);
    Player toEntity(PlayerDTO dto);


}
