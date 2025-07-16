package cat.itacademy.s05.t01.n01;


import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.mapper.PlayerMapper;
import cat.itacademy.s05.t01.n01.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    void setUp(){
        playerMapper = new PlayerMapper();
    }
    @Test
    void toEntity_shouldMapDTOToEntityCorrectly(){
        PlayerDTO dto = new PlayerDTO(1L,"Ignasi", 5,0.75);
        Player entity = playerMapper.toEntity(dto);

        assertEquals(1L, entity.getId());
        assertEquals("Ignasi", entity.getName());
        assertEquals(5, entity.getTotalWins());
    }

    @Test
    void toDto_shouldMapEntityToDEOCorrectly(){
        Player entity = new Player(1L,"Ignasi", 6);
        PlayerDTO dto= playerMapper.toDTO(entity);

        assertEquals(1L, dto.id());
        assertEquals("Ignasi", dto.name());
        assertEquals(6, dto.totalWins());
    }


}
