package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.controller.GameController;
import cat.itacademy.s05.t01.n01.dto.GameRequestDTO;
import cat.itacademy.s05.t01.n01.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerRankingDTO;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import cat.itacademy.s05.t01.n01.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(GameController.class)
@AutoConfigureWebTestClient
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private GameEngine gameEngine;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(gameService);
    }

    @Test
    void createGame_ReturnCreatedGame() {
        GameRequestDTO request = new GameRequestDTO("1");
        GameResponseDTO response = new GameResponseDTO("abc123", "1", GameStatus.PLAYING);

        Mockito.when(gameService.createGame(any(GameRequestDTO.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc123")
                .jsonPath("$.playerId").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("PLAYING");
    }

    @Test
    void getGameById_ReturnGame() {
        String gameId = "abc123";
        GameResponseDTO response = new GameResponseDTO(gameId, "1", GameStatus.PLAYING);

        Mockito.when(gameService.getGameById(gameId))
                .thenReturn(Mono.just(response));
        webTestClient.get()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId)
                .jsonPath("$.playerId").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("PLAYING");
    }

    @Test
    void getAllGames_ReturnListOfGames() {
        GameResponseDTO game1 = new GameResponseDTO("id1", "1", GameStatus.PLAYING);
        GameResponseDTO game2 = new GameResponseDTO("id2", "2", GameStatus.WON);

        Mockito.when(gameService.getAllGames())
                .thenReturn(Flux.just(game1, game2));

        webTestClient.get()
                .uri("/games")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo("id1")
                .jsonPath("$[0].status").isEqualTo("PLAYING")
                .jsonPath("$[1].id").isEqualTo("id2")
                .jsonPath("$[1].status").isEqualTo("WON");
    }

    @Test
    void updateGame_ReturnsUpdatedGame() {
        String gameId = "abc123";

        GameRequestDTO request = new GameRequestDTO("1");
        GameResponseDTO updated = new GameResponseDTO(gameId, "1", GameStatus.WON);

        Mockito.when(gameService.updateGame(Mockito.eq(gameId), Mockito.any(GameRequestDTO.class)))
                .thenReturn(Mono.just(updated));

        webTestClient.put()
                .uri("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId)
                .jsonPath("$.playerId").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("WON");
    }

    @Test
    void deleteGame_ReturnNoContent() {
        String gameId = "abc123";
        Mockito.when(gameService.deleteGame(gameId))
                .thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void startGame_ReturnsGameStarted() {
        String gameId = "abc123";
        GameResponseDTO response = new GameResponseDTO(gameId, "1", GameStatus.PLAYING);

        Mockito.when(gameService.startGame(gameId))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/games/{id}/start", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(gameId)
                .jsonPath("$.playerId").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("PLAYING");
    }

    @Test
    void getRanking_ReturnsOrderedRankingWithPositionAndRatio() {
        PlayerRankingDTO player1 = new PlayerRankingDTO(1, 1L, "Alice", 5, 0.83);
        PlayerRankingDTO player2 = new PlayerRankingDTO(2, 2L, "Bob", 3, 0.60);

        Mockito.when(gameService.getRanking())
                .thenReturn(Flux.just(player1, player2));

        webTestClient.get()
                .uri("/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].position").isEqualTo(1)
                .jsonPath("$[0].playerName").isEqualTo("Alice")
                .jsonPath("$[0].winCount").isEqualTo(5)
                .jsonPath("$[0].winRatio").isEqualTo(0.83)
                .jsonPath("$[1].position").isEqualTo(2)
                .jsonPath("$[1].playerName").isEqualTo("Bob")
                .jsonPath("$[1].winCount").isEqualTo(3)
                .jsonPath("$[1].winRatio").isEqualTo(0.60);
    }

}
