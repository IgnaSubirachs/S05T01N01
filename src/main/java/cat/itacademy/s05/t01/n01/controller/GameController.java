package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.dto.GameDTO;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameEngine gameEngine;

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

    @GetMapping("/draw")
    public Mono<Card>drawCard(){
        return Mono.just(gameEngine.drawCard());
    }

    @PutMapping("/{id}/hit")
    public Mono<ResponseEntity<GameDTO>> hit(@PathVariable String id) {
        return gameService.hit(id)
                .map(ResponseEntity::ok);
    }
    @PutMapping("/{id}/stand")
    public Mono<ResponseEntity<GameDTO>> stand(@PathVariable String id) {
        return gameService.stand(id)
                .map(ResponseEntity::ok);
    }


}
