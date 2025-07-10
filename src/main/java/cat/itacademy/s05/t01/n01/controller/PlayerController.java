package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerNameUpdateRequest;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PutMapping
    public Mono<ResponseEntity<PlayerDTO>> updateById(@PathVariable Long id, @Valid @RequestBody PlayerNameUpdateRequest request) {

        return playerService.updatePlayerName(id, request.name())
                .map(ResponseEntity::ok);


    }
}
