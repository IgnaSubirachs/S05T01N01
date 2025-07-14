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
    public Mono<ResponseEntity<GameDTO>>createGame(@RequestBody GameDTO dto){
        return gameService.createGame(dto)
                .map(ResponseEntity::ok);
    }
    @GetMapping
    public Mono<ResponseEntity<GameDTO>>getGameByUd(@PathVariable String id){
        return gameService.getGameById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<GameDTO>getAllGames(){
        return gameService.getAllGames();
    }

    @PutMapping
    public Mono<ResponseEntity<GameDTO>>updateGame(@PathVariable String id, @RequestBody GameDTO dto){
        return gameService.updateGame(id, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>>deleteGame(@PathVariable String id){
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

        }
