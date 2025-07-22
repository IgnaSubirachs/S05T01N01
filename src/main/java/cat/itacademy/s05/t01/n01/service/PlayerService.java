package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;

import cat.itacademy.s05.t01.n01.dto.PlayerRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<PlayerDTO> updatePlayerName(Long id, String newName);
    Mono<PlayerDTO> createPlayer(PlayerRequestDTO dto);
    Flux<PlayerDTO> getAllPlayers();
    Mono<PlayerDTO>getById(Long id);
    Mono<Void>deleteById(Long id);
    Flux<PlayerDTO>findByName(String name);
}
