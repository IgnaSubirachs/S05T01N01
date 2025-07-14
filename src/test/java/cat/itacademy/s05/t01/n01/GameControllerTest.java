package cat.itacademy.s05.t01.n01;


import cat.itacademy.s05.t01.n01.controller.GameController;
import cat.itacademy.s05.t01.n01.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(GameController.class)
@Import(GameControllerTest.MockedGameServiceConfig.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private static GameService gameServiceMock;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(gameServiceMock);
    }
    @Configuration
    static class MockedGameServiceConfig {
        @Bean
        @Primary
        public GameService gameService() {
            gameServiceMock = Mockito.mock(GameService.class);
            return gameServiceMock;
        }
    }
}
