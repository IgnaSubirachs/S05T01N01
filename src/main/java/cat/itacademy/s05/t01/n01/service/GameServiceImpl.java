package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final GameEngine gameEngine;


    @Override
    public Mono<GameDTO> createGame(GameDTO dto) {
        Game game = gameMapper.toEntity(dto);
        return gameRepository.save(game)
                .map(gameMapper::toDto)
                .doOnSuccess(savedDto ->
                log.debug("Game {} created successfully for player {}", savedDto.id(), savedDto.playerId()));
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
    public Mono<GameDTO> startGame(String id) {
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
                            .map(gameMapper::toDto);
                });
    }
    @Override
    public Mono<GameDTO> hit(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() != GameStatus.PLAYING) {
                        return Mono.error(new ApiException("Game is not in PLAYING state " + gameId, HttpStatus.BAD_REQUEST));
                    }
                    Game updatedGame = gameEngine.applyHit(game);

                    return gameRepository.save(updatedGame)
                            .map(gameMapper::toDto);
                });
    }
    @Override
    public Mono<GameDTO>stand(String gameId){
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new ApiException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game ->{
                    if(game.getStatus()!= GameStatus.PLAYING){
                        return Mono.error(new ApiException("Game is not in PLAYING state", HttpStatus.BAD_REQUEST));
                    }
                    List<Card>updatedDealerHand = gameEngine.playerDealerTurn(game.getDealerHand());
                    game.setDealerHand(updatedDealerHand);

                    GameStatus result = gameEngine.evaluateWinner(game.getPlayerHand(),updatedDealerHand);
                            game.setStatus(result);

                    return gameRepository.save(game)
                            .map(gameMapper::toDto);
                });
    }


}






