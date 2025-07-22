package cat.itacademy.s05.t01.n01;


import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerRequestDTO;
import cat.itacademy.s05.t01.n01.mapper.PlayerMapper;
import cat.itacademy.s05.t01.n01.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    void setUp(){
        playerMapper = new PlayerMapper();
    }
    @Test
    void toEntity_shouldMapRequestDTOToEntityCorrectly() {
        PlayerRequestDTO dto = new PlayerRequestDTO("Ignasi");
        Player entity = playerMapper.toEntity(dto);
        assertNull(entity.getId());
        assertEquals("Ignasi", entity.getName());
        assertEquals(0, entity.getTotalWins());
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
