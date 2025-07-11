package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<PlayerDTO> updatePlayerName(Long id, String newName);
    Mono<PlayerDTO>createPlayer(PlayerDTO playerDTO);
    Flux<PlayerDTO> getAllPlayers();
}
