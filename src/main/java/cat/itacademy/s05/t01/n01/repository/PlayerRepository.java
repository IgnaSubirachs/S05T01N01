package cat.itacademy.s05.t01.n01.repository;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
    Flux<Player> findByName(String name);
}