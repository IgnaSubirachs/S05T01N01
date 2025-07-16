package cat.itacademy.s05.t01.n01;

import cat.itacademy.s05.t01.n01.controller.PlayerController;
import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(PlayerController.class)
@AutoConfigureWebTestClient
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PlayerService playerService;

    private PlayerDTO player;

    @BeforeEach
    void setUp() {
        player = new PlayerDTO(1L, "Ignasi", 10, 0.66);
    }

    @Test
    void getAllPlayers_ReturnsList() {
        Mockito.when(playerService.getAllPlayers())
                .thenReturn(Flux.just(player));

        webTestClient.get()
                .uri("/players")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Ignasi");
    }

    @Test
    void getPlayerById_ReturnsPlayer() {
        Mockito.when(playerService.getById(1L))
                .thenReturn(Mono.just(player));

        webTestClient.get()
                .uri("/players/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Ignasi")
                .jsonPath("$.winRatio").isEqualTo(0.66);
    }

    @Test
    void deletePlayer_ReturnsNoContent() {
        Mockito.when(playerService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/players/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void findByName_ReturnsMatchingPlayers() {
        Mockito.when(playerService.findByName("Ignasi"))
                .thenReturn(Flux.just(player));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/players")
                        .queryParam("name", "Ignasi")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Ignasi");
    }

    @Test
    void updatePlayerName_ReturnsUpdatedPlayer() {
        PlayerDTO updated = new PlayerDTO(1L, "NouNom", 10, 0.66);
        Mockito.when(playerService.updatePlayerName(1L, "NouNom"))
                .thenReturn(Mono.just(updated));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/players/{id}")
                        .queryParam("name", "NouNom")
                        .build(1L))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("NouNom");
    }
}
