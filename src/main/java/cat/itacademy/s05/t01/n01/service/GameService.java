package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    Mono<GameResponseDTO> createGame(GameRequestDTO gameRequestDTO);

    Mono<GameResponseDTO> getGameById(String id);

    Flux<GameResponseDTO> getAllGames();

    Mono<GameResponseDTO> updateGame(String id, GameRequestDTO gameRequestDTO);

    Mono<Void> deleteGame(String id);

    Mono<GameResponseDTO> startGame(String gameId);

    Mono<GameResponseDTO> hit(String gameId);

    Mono<GameResponseDTO> stand(String gameId);

    Mono<GameResponseDTO> play(String gameId, PlayRequestDTO playRequestDto);

    Mono<PlayerUpdateDTO> updatePlayerName(Long playerId, PlayerUpdateDTO updateDto);

    Flux<PlayerRankingDTO> getRanking();

    Mono<Long> countGamesByPlayerId(Long playerId);
}


