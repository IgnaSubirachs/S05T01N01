package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerRequestDTO;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.exception.ErrorCode;
import cat.itacademy.s05.t01.n01.mapper.PlayerMapper;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final GameService gameService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             PlayerMapper playerMapper,
                             GameService gameService) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.gameService = gameService;
    }

    @Override
    public Mono<PlayerDTO> createPlayer(PlayerRequestDTO dto) {
        Player player = Player.builder()
                .name(dto.name())
                .totalWins(0)
                .build();

        return playerRepository.save(player)
                .map(saved -> new PlayerDTO(saved.getId(), saved.getName(), saved.getTotalWins(), 0.0));
    }

    @Override
    public Mono<PlayerDTO> updatePlayerName(Long id, String newName) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.PLAYER_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                })
                .map(playerMapper::toDTO);
    }

    @Override
    public Flux<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll()
                .map(playerMapper::toDTO);
    }

    @Override
    public Mono<PlayerDTO> getById(Long id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.PLAYER_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(player -> gameService.countGamesByPlayerId(player.getId())
                        .map(totalGames -> {
                            double winRatio = totalGames > 0
                                    ? (double) player.getTotalWins() / totalGames
                                    : 0.0;
                            return playerMapper.toDTO(player, winRatio);
                        })
                );
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.PLAYER_NOT_FOUND, HttpStatus.NOT_FOUND)))
                .flatMap(playerRepository::delete);
    }

    @Override
    public Flux<PlayerDTO> findByName(String name) {
        return playerRepository.findByName(name)
                .map(playerMapper::toDTO);
    }
}
