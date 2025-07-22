package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.dto.*;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.mapper.GameMapper;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameMapper gameMapper;
    @Mock
    private GameEngine gameEngine;



    @InjectMocks
    private GameServiceImpl service;

    private final String gameId = "game123";
    private final Long playerId = 1L;
    private GameRequestDTO gameRequest;
    private GameResponseDTO gameResponse;
    private Game gameEntity;

    @BeforeEach
    void setUp() {
        gameRequest    = new GameRequestDTO(playerId.toString());
        gameEntity     = new Game();
        gameEntity.setId(gameId);
        gameEntity.setPlayerId(gameRequest.playerId());
        gameEntity.setStatus(GameStatus.PLAYING);
        gameResponse   = new GameResponseDTO(  gameId,
                gameRequest.playerId(),
                GameStatus.PLAYING,
                List.of(), // playerHand
                List.of()  // dealerHand
        );
    }

    @Test
    void createGame_shouldReturnResponseDto() {
        Player mockPlayer = Player.builder()
                .id(playerId)
                .name("Ignasi")
                .totalWins(0)
                .build();

        when(playerRepository.findById(playerId)).thenReturn(Mono.just(mockPlayer));
        when(gameMapper.toEntity(gameRequest)).thenReturn(gameEntity);
        when(gameRepository.save(gameEntity)).thenReturn(Mono.just(gameEntity));
        when(gameMapper.toResponseDto(gameEntity)).thenReturn(gameResponse);

        StepVerifier.create(service.createGame(gameRequest))
                .expectNext(gameResponse)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(playerId);
        verify(gameRepository, times(1)).save(gameEntity);
    }

    @Test
    void updatePlayerName_shouldReturnUpdatedDto() {
        Player existing = new Player();
        existing.setId(playerId);
        existing.setName("OldName");
        existing.setTotalWins(4);

        Player saved = new Player();
        saved.setId(playerId);
        saved.setName("NewName");
        saved.setTotalWins(4);

        PlayerUpdateDTO updateDto = new PlayerUpdateDTO("NewName");

        when(playerRepository.findById(playerId)).thenReturn(Mono.just(existing));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.updatePlayerName(playerId, updateDto))
                .expectNextMatches(dto ->
                        dto.newName().equals("NewName")
                )
                .verifyComplete();

        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, times(1)).save(any(Player.class));
    }



}
