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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO dto) {
        return playerService.createPlayer(dto);
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
    public Flux<PlayerDTO> getAllPlayers(){
        return playerService.getAllPlayers();
    }
}
