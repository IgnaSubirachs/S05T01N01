package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.mapper.PlayerMapper;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {
    private PlayerRepository playerRepository;
    private PlayerMapper playerMapper;
    private PlayerServiceImpl playerService;
    private Player samplePlayer;

    @BeforeEach
    void setup(){
        playerRepository = mock(PlayerRepository.class);
        playerMapper = new PlayerMapper();
        playerService = new PlayerServiceImpl(playerRepository , playerMapper);
        samplePlayer = Player.builder()
                .id(1L)
                .name("Ignasi")
                .totalWins(12)
                .build();
    }

    @Test
    void testCreatePlayer_returnSavedPlayerDTO(){
        PlayerDTO inputDTO = new PlayerDTO(null, "Ignasi",0);
        Player savedPlayer = new Player(1L, "Ignasi", 0);
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(savedPlayer));

        Mono<PlayerDTO> resutl = playerService.createPlayer(inputDTO);

        StepVerifier.create(resutl)
                .expectNextMatches(dto->
                        dto.id().equals(1L) &&
                        dto.totalWins() == 0
                )
                .verifyComplete();
        verify(playerRepository, times (1)).save(any(Player.class));

    }
    @Test
    void updatePlayerName_shouldUpdateNameIfExists() {

        String newName = "NouNom";
        Player updatedPlayer = Player.builder()
                .id(1L)
                .name(newName)
                .totalWins(5)
                .build();


        when(playerRepository.findById(1L)).thenReturn(Mono.just(samplePlayer));


        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(updatedPlayer));


        Mono<PlayerDTO> resultMono = playerService.updatePlayerName(1L, newName);


        StepVerifier.create(resultMono)
                .expectNextMatches(dto ->
                        dto.id().equals(1L)
                                && dto.name().equals(newName)
                                && dto.totalWins() == 5
                )
                .verifyComplete();
    }
}
