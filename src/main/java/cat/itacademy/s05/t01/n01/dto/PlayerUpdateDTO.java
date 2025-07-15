package cat.itacademy.s05.t01.n01.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for updating a player's name")
public record PlayerUpdateDTO(

        @Schema(description = "New name of the player", example = "Bob")
        String newName
) {}