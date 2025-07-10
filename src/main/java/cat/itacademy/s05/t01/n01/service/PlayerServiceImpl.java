package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.mapper.PlayerMapper;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService{
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public Mono<PlayerDTO> updatePlayerName(Long id, String newName){
        return Mono.fromCallable(()->{
            Optional<Player> optionalPlayer = playerRepository.findById(id);
            if(optionalPlayer.isEmpty()){
                throw new RuntimeException("PLayer not found with id: "+id);
            }
            Player player = optionalPlayer.get();
            player.setName(newName);
            Player updated = playerRepository.save(player);
            return playerMapper.toDTO(updated);
        });
    }
}
