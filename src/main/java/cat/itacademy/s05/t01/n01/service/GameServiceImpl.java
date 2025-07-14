package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.mapper.GameMapper;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    private Card drawCard() {
        String[] suits = {"♠", "♥", "♦", "♣"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        String suit = suits[(int)(Math.random() * suits.length)];
        String value = values[(int)(Math.random() * values.length)];

        return new Card(value, suit);
    }

    @Override
    public Mono<GameDTO> createGame(GameDTO dto) {
        Game game = gameMapper.toEntity(dto);
        return gameRepository.save(game)
                .map(gameMapper::toDto);
    }

    @Override
    public Mono<GameDTO> getGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found", HttpStatus.NOT_FOUND)))
                .map(gameMapper::toDto);
    }

    @Override
    public Flux<GameDTO> getAllGames() {
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

    @Override
    public Mono<GameDTO> startGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    game.setPlayerHand(List.of(drawCard(), drawCard()));
                    game.setDealerHand(List.of(drawCard()));
                    game.setStatus(GameStatus.PLAYING);
                    return gameRepository.save(game);
                })
                .map(gameMapper::toDto);
    }

}

