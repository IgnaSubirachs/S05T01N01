package cat.itacademy.s05.t01.n01.dto;

import cat.itacademy.s05.t01.n01.model.GameStatus;

public record GameDTO (String id, String playerId, GameStatus status) {
}
