package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.exception.ApiException;
import cat.itacademy.s05.t01.n01.mapper.PlayerMapper;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public Mono<PlayerDTO> createPlayer(PlayerDTO dto) {
        Player player = playerMapper.toEntity(dto);
        return playerRepository
                .save(player)
                .map(playerMapper::toDTO);
    }


    @Override
    public Mono<PlayerDTO> updatePlayerName(Long id, String newName) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("Player not found with id: " + id,404)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                })
                .map(playerMapper::toDTO);
    }

    @Override
    public Flux<PlayerDTO> getAllPlayers(){
        return playerRepository.findAll()
                .map(playerMapper::toDTO);
    }
}
