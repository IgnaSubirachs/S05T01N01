package cat.itacademy.s05.t01.n01.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request DTO for creating a new player")
public record PlayerRequestDTO(

        //@NotBlank
        @Schema(description = "Name of the player", example = "Ignasi")
        String name

) {}