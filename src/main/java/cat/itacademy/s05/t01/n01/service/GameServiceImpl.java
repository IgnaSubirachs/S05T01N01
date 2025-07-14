package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.mapper.GameMapper;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public Mono<GameDTO> createGame(GameDTO dto) {
        Game game = gameMapper.toEntity(dto);
        return gameRepository.save(game)
                .map(gameMapper::toDto);
    }

    @Override
    public Mono<GameDTO>getGameById(String id){
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found", HttpStatus.NOT_FOUND)))
                .map(gameMapper::toDto);
    }
    @Override
    public Flux<GameDTO> getAllGames(){
        return gameRepository.findAll()
                .map(gameMapper::toDto);
    }
    @Override
    public Mono<GameDTO> updateGame(String id, GameDTO gameDTO) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ApiException("Game not found with id: " + id, HttpStatus.NOT_FOUND)
                ))
                .flatMap(existing -> {
                    existing.setPlayerId(gameDTO.playerId());
                    existing.setStatus(gameDTO.status());
                    return gameRepository.save(existing);
                })
                .map(gameMapper::toDto);
    }
    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + id, HttpStatus.NOT_FOUND)))
                .flatMap(gameRepository::delete);
    }
}
