package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public Mono<ResponseEntity<GameDTO>> createGame(@RequestBody GameDTO dto) {
        return gameService.createGame(dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameDTO>> getGameById(@PathVariable String id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<GameDTO> getAllGames() {
        return gameService.getAllGames();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<GameDTO>> updateGame(@PathVariable String id, @RequestBody GameDTO dto) {
        return gameService.updateGame(id, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
    @PostMapping("/{id}/start")
    public Mono<ResponseEntity<GameDTO>>startGame(@PathVariable String id){
        return gameService.startGame(id)
                .map(ResponseEntity::ok);
    }

}
