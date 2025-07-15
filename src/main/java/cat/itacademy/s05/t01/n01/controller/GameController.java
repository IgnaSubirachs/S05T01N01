package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.dto.GameRequestDTO;
import cat.itacademy.s05.t01.n01.dto.GameResponseDTO;
import cat.itacademy.s05.t01.n01.logic.GameEngine;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Tag(name = "Game Controller", description = "Endpoints for managing Blackjack games")
public class GameController {

    private final GameService gameService;
    private final GameEngine gameEngine;

    @Operation(summary = "Create a new Blackjack game", description = "Creates a new game for a given player.")
    @ApiResponse(responseCode = "201", description = "Game successfully created",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @PostMapping
    public Mono<ResponseEntity<GameResponseDTO>> createGame(@RequestBody GameRequestDTO dto) {
        return gameService.createGame(dto)
                .map(responseDto -> ResponseEntity.status(HttpStatus.CREATED).body(responseDto));
    }

    @Operation(summary = "Get a game by ID", description = "Fetches a game using its ID.")
    @ApiResponse(responseCode = "200", description = "Game found",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameResponseDTO>> getGameById(@PathVariable String id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Get all games", description = "Retrieves a list of all existing games.")
    @ApiResponse(responseCode = "200", description = "List of games",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @GetMapping
    public Flux<GameResponseDTO> getAllGames() {
        return gameService.getAllGames();
    }

    @Operation(summary = "Update a game", description = "Updates a game by its ID.")
    @ApiResponse(responseCode = "200", description = "Game updated",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GameResponseDTO>> updateGame(@PathVariable String id,
                                                            @RequestBody GameRequestDTO dto) {
        return gameService.updateGame(id, dto)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Delete a game", description = "Deletes a game using its ID.")
    @ApiResponse(responseCode = "204", description = "Game deleted")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Start a game", description = "Starts a new round by dealing cards.")
    @ApiResponse(responseCode = "200", description = "Game started",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @PostMapping("/{id}/start")
    public Mono<ResponseEntity<GameResponseDTO>> startGame(@PathVariable String id) {
        return gameService.startGame(id)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Draw a card", description = "Returns a random card drawn from the deck.")
    @ApiResponse(responseCode = "200", description = "Card drawn",
            content = @Content(schema = @Schema(implementation = Card.class)))
    @GetMapping("/draw")
    public Mono<Card> drawCard() {
        return Mono.just(gameEngine.drawCard());
    }

    @Operation(summary = "Hit", description = "Draws a new card for the player in the current game.")
    @ApiResponse(responseCode = "200", description = "Game after hit",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @PutMapping("/{id}/hit")
    public Mono<ResponseEntity<GameResponseDTO>> hit(@PathVariable String id) {
        return gameService.hit(id)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Stand", description = "Ends the player's turn and lets the dealer play.")
    @ApiResponse(responseCode = "200", description = "Game ended",
            content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    @PutMapping("/{id}/stand")
    public Mono<ResponseEntity<GameResponseDTO>> stand(@PathVariable String id) {
        return gameService.stand(id)
                .map(ResponseEntity::ok);
    }
}
