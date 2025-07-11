package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @PostMapping
    public Mono<ResponseEntity<PlayerDTO>> createPlayer(@Valid @RequestBody PlayerDTO dto) {
        return playerService.createPlayer(dto)
                .map(player -> ResponseEntity.status(HttpStatus.CREATED).body(player));
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlayerDTO>> updatePlayerName(
            @PathVariable Long id,
            @RequestParam @NotBlank String name
    ) {
        return playerService.updatePlayerName(id, name)
                .map(ResponseEntity::ok);
    }


    @GetMapping
    public Flux<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlayerDTO>> getPlayerById(@PathVariable Long id) {
        return playerService.getById(id)
                .map(ResponseEntity::ok);
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable Long id) {
        return playerService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("/search")
    public Mono<PlayerDTO>findByName(@RequestParam String name){
        return playerService.findByName(name);

    }
}
