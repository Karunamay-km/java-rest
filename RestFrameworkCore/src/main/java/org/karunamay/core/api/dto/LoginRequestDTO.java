package org.karunamay.core.api.dto;

public record LoginRequestDTO(
        String email,
        String password
) implements HttpRequestDTO {
}
