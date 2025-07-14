package cat.itacademy.s05.t01.n01;


import cat.itacademy.s05.t01.n01.controller.GameController;
import cat.itacademy.s05.t01.n01.dto.GameDTO;
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

    @BeforeEach
    void resetMocks() {
        Mockito.reset(gameService);
    }

    @Test
    void createGame_ReturnCreatedGame() {
        GameDTO request = new GameDTO(null, "1", GameStatus.PLAYING);
        GameDTO response = new GameDTO("abc123", "1", GameStatus.PLAYING);

        Mockito.when(gameService.createGame(any(GameDTO.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk() // ← Aquí es 200 OK porque usas ResponseEntity.ok()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc123")
                .jsonPath("$.playerId").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("PLAYING");
    }

    @Test
    void getGameById_ReturnGame() {
        String gameId = "abc123";
        GameDTO response = new GameDTO(gameId, "1", GameStatus.PLAYING);

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
        GameDTO game1 = new GameDTO("id1", "1", GameStatus.PLAYING);
        GameDTO game2 = new GameDTO("id2", "2", GameStatus.WON);

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

        GameDTO request = new GameDTO(null, "1", GameStatus.WON);
        GameDTO updated = new GameDTO(gameId, "1", GameStatus.WON);

        Mockito.when(gameService.updateGame(Mockito.eq(gameId), Mockito.any(GameDTO.class)))
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
    void deleteGame_ReturnNoContent(){
        String gameId = "abc123";
        Mockito.when(gameService.deleteGame(gameId))
                .thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }
}