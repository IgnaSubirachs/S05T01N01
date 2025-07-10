package cat.itacademy.s05.t01.n01.dto;

import jakarta.validation.constraints.NotBlank;

public record PlayerNameUpdateRequest(

        @NotBlank(message = "Name must not be blank")
        String name

) {}
