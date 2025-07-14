package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.model.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface GameService {
    Mono<GameDTO> createGame(GameDTO gameDTO);
    Mono<GameDTO> getGameById(String id);
    Flux<GameDTO> getAllGames();
    Mono<GameDTO> updateGame(String id, GameDTO gameDTO);
    Mono<Void> deleteGame(String id);
    Mono<GameDTO>startGame(String gameId);
}