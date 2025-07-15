package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.GameRequestDTO;
import cat.itacademy.s05.t01.n01.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.mapper.GameMapper;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final GameEngine gameEngine;

    @Override
    public Mono<GameResponseDTO> createGame(GameRequestDTO dto) {
        Game game = gameMapper.toEntity(dto);
        game.setStatus(GameStatus.PLAYING);
        return gameRepository.save(game)
                .map(gameMapper::toResponseDto)
                .doOnSuccess(savedDto ->
                        log.debug("Game {} created successfully for player {}", savedDto.id(), savedDto.playerId()));
    }

    @Override
    public Mono<GameResponseDTO> getGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found", HttpStatus.NOT_FOUND)))
                .map(gameMapper::toResponseDto)
                .doOnSuccess(dto -> log.debug("Game {} retrieved", dto.id()));
    }

    @Override
    public Flux<GameResponseDTO> getAllGames() {
        return gameRepository.findAll()
                .map(gameMapper::toResponseDto)
                .doOnComplete(() -> log.debug("Finished retrieving all games"));
    }

    @Override
    public Mono<GameResponseDTO> updateGame(String id, GameRequestDTO gameDTO) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + id, HttpStatus.NOT_FOUND)))
                .flatMap(existing -> {
                    existing.setPlayerId(gameDTO.playerId());
                    return gameRepository.save(existing);
                })
                .map(gameMapper::toResponseDto)
                .doOnSuccess(updated -> log.debug("Game {} updated successfully", updated.id()));
    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + id, HttpStatus.NOT_FOUND)))
                .flatMap(gameRepository::delete)
                .doOnSuccess(unused -> log.debug("Game {} deleted successfully", id));
    }

    @Override
    public Mono<GameResponseDTO> startGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + id, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    List<Card> playerHand = Arrays.asList(gameEngine.drawCard(), gameEngine.drawCard());
                    List<Card> dealerHand = Arrays.asList(gameEngine.drawCard(), gameEngine.drawCard());

                    game.setPlayerHand(new ArrayList<>(playerHand));
                    game.setDealerHand(new ArrayList<>(dealerHand));

                    int playerValue = gameEngine.calculateHandValue(playerHand);
                    int dealerValue = gameEngine.calculateHandValue(dealerHand);

                    if (playerValue == 21 && dealerValue == 21) {
                        game.setStatus(GameStatus.DRAW);
                    } else if (playerValue == 21) {
                        game.setStatus(GameStatus.WON);
                    } else if (dealerValue == 21) {
                        game.setStatus(GameStatus.LOST);
                    } else {
                        game.setStatus(GameStatus.PLAYING);
                    }

                    return gameRepository.save(game)
                            .map(gameMapper::toResponseDto);
                });
    }

    @Override
    public Mono<GameResponseDTO> hit(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() != GameStatus.PLAYING) {
                        return Mono.error(new ApiException("Game is not in PLAYING state", HttpStatus.BAD_REQUEST));
                    }
                    Game updatedGame = gameEngine.applyHit(game);
                    return gameRepository.save(updatedGame)
                            .map(gameMapper::toResponseDto);
                });
    }

    @Override
    public Mono<GameResponseDTO> stand(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() != GameStatus.PLAYING) {
                        return Mono.error(new ApiException("Game is not in PLAYING state", HttpStatus.BAD_REQUEST));
                    }
                    List<Card> updatedDealerHand = gameEngine.playerDealerTurn(game.getDealerHand());
                    game.setDealerHand(updatedDealerHand);

                    GameStatus result = gameEngine.evaluateWinner(game.getPlayerHand(), updatedDealerHand);
                    game.setStatus(result);

                    return gameRepository.save(game)
                            .map(gameMapper::toResponseDto);
                });
    }
}