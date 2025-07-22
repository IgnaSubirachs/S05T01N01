package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.dto.PlayerDTO;
import cat.itacademy.s05.t01.n01.dto.PlayerRequestDTO;
import cat.itacademy.s05.t01.n01.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/players")
@Tag(name = "Player Controller", description = "Handles all operations related to players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Create a new player", description = "Creates a new player with the given name and returns it",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Player successfully created",
                            content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
            })
    @PostMapping
    public Mono<ResponseEntity<PlayerDTO>> createPlayer(@RequestBody PlayerRequestDTO dto)

    {
        System.out.println("Received PlayerRequestDTO: " + dto);

        return playerService.createPlayer(dto)
                .map(player -> ResponseEntity.status(HttpStatus.CREATED).body(player));
    }

    @Operation(summary = "Update a player's name", description = "Updates the name of an existing player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player successfully updated",
                            content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
            })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlayerDTO>> updatePlayerName(
            @PathVariable Long id,
            @RequestParam @NotBlank String name
    ) {
        return playerService.updatePlayerName(id, name)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Get all players", description = "Returns a list of all players",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of players",
                            content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
            })
    @GetMapping
    public Flux<PlayerDTO> getAllPlayers(@RequestParam(required = false) String name) {
        return (name != null) ? playerService.findByName(name) : playerService.getAllPlayers();
    }


    @Operation(summary = "Get a player by ID", description = "Fetches a specific player by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player found",
                            content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
            })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlayerDTO>> getPlayerById(@PathVariable Long id) {
        return playerService.getById(id)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Delete a player by ID", description = "Deletes a specific player",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Player successfully deleted")
            })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable Long id) {
        return playerService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
