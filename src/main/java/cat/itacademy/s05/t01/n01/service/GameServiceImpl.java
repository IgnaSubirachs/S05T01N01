package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.*;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.exception.ErrorCode;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.mapper.GameMapper;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameStatus;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final GameEngine gameEngine;
    private final PlayerRepository playerRepository;

    @Override
    public Mono<GameResponseDTO> createGame(GameRequestDTO requestDto) {
        String playerId = requestDto.playerId();

        return playerRepository.findById(Long.valueOf(playerId))
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.PLAYER_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(player -> {
                    Game game = gameMapper.toEntity(requestDto);
                    game.setStatus(GameStatus.PLAYING);

                    return gameRepository.save(game)
                            .map(gameMapper::toResponseDto)
                            .doOnSuccess(savedDto ->
                                    log.debug("Game {} created for player {}", savedDto.id(), savedDto.playerId()));
                });
    }

    @Override
    public Mono<GameResponseDTO> getGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.GAME_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .map(gameMapper::toResponseDto)
                .doOnSuccess(dto -> log.debug("Game {} retrieved", dto.id()));
    }

    @Override
    public Flux<GameResponseDTO> getAllGames() {
        return gameRepository.findAll()
                .map(gameMapper::toResponseDto)
                .doOnComplete(() -> log.debug("Retrieved all games"));
    }

    @Override
    public Mono<GameResponseDTO> updateGame(String id, GameRequestDTO requestDto) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.GAME_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(existing -> {
                    existing.setPlayerId(requestDto.playerId());
                    return gameRepository.save(existing);
                })
                .map(gameMapper::toResponseDto)
                .doOnSuccess(dto -> log.debug("Game {} updated", dto.id()));
    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.GAME_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(gameRepository::delete)
                .doOnSuccess(unused -> log.debug("Game {} deleted", id));
    }

    @Override
    public Mono<GameResponseDTO> startGame(String playerId) {
        return playerRepository.findById(Long.valueOf(playerId))
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.PLAYER_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(player -> {
                    List<Card> playerHand = Arrays.asList(gameEngine.drawCard(), gameEngine.drawCard());
                    List<Card> dealerHand = Arrays.asList(gameEngine.drawCard(), gameEngine.drawCard());

                    Game game = Game.builder()
                            .playerId(String.valueOf(player.getId()))
                            .playerHand(new ArrayList<>(playerHand))
                            .dealerHand(new ArrayList<>(dealerHand))
                            .createdAt(LocalDate.now())
                            .status(GameStatus.PLAYING)
                            .build();

                    int playerValue = gameEngine.calculateHandValue(playerHand);
                    int dealerValue = gameEngine.calculateHandValue(dealerHand);

                    if (playerValue == 21 && dealerValue == 21) {
                        game.setStatus(GameStatus.DRAW);
                    } else if (playerValue == 21) {
                        game.setStatus(GameStatus.WON);
                    } else if (dealerValue == 21) {
                        game.setStatus(GameStatus.LOST);
                    }

                    return gameRepository.save(game)
                            .map(gameMapper::toResponseDto);
                });
    }

    @Override
    public Mono<GameResponseDTO> hit(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.GAME_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() != GameStatus.PLAYING) {
                        return Mono.error(new ApiException(ErrorCode.INVALID_GAME_STATE, HttpStatus.BAD_REQUEST));
                    }
                    Game updatedGame = gameEngine.applyHit(game);
                    return gameRepository.save(updatedGame)
                            .map(gameMapper::toResponseDto);
                });
    }

    @Override
    public Mono<GameResponseDTO> stand(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.GAME_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() != GameStatus.PLAYING) {
                        return Mono.error(new ApiException(ErrorCode.INVALID_GAME_STATE, HttpStatus.BAD_REQUEST));
                    }
                    List<Card> updatedDealerHand = gameEngine.playerDealerTurn(game.getDealerHand());
                    game.setDealerHand(updatedDealerHand);

                    GameStatus result = gameEngine.evaluateWinner(game.getPlayerHand(), updatedDealerHand);
                    game.setStatus(result);

                    return gameRepository.save(game)
                            .map(gameMapper::toResponseDto);
                });
    }

    @Override
    public Mono<GameResponseDTO> play(String gameId, PlayRequestDTO playRequestDto) {
        switch (playRequestDto.action().toUpperCase(Locale.ROOT)) {
            case "START":
                return startGame(gameId);
            case "HIT":
                return hit(gameId);
            case "STAND":
                return stand(gameId);
            default:
                return Mono.error(new ApiException(ErrorCode.INVALID_ACTION, HttpStatus.BAD_REQUEST));
        }
    }

    @Override
    public Mono<PlayerUpdateDTO> updatePlayerName(Long playerId, PlayerUpdateDTO updateDto) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.PLAYER_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(player -> {
                    player.setName(updateDto.newName());
                    return playerRepository.save(player);
                })
                .map(updated -> new PlayerUpdateDTO(updated.getName()));
    }

    @Override
    public Flux<PlayerRankingDTO> getRanking() {
        return playerRepository.findAll()
                .sort(Comparator.comparing(Player::getTotalWins).reversed())
                .index()
                .concatMap(tuple -> {
                    long index = tuple.getT1();
                    Player player = tuple.getT2();

                    return gameRepository.findAll()
                            .filter(game -> game.getPlayerId().equals(player.getId().toString()))
                            .count()
                            .map(totalGames -> {
                                double ratio = totalGames > 0
                                        ? (double) player.getTotalWins() / totalGames
                                        : 0.0;

                                return new PlayerRankingDTO(
                                        (int) index + 1,
                                        player.getId(),
                                        player.getName(),
                                        player.getTotalWins(),
                                        ratio
                                );
                            });
                });
    }

    @Override
    public Mono<Long> countGamesByPlayerId(Long playerId) {
        return gameRepository.findAll()
                .filter(game -> game.getPlayerId().equals(playerId.toString()))
                .count();
    }
}