package org.karunamay.core.api.authentication;

import java.time.Instant;
import java.time.LocalDateTime;

public record UserResponseDTO(Long ID,
                              String username,
                              String email,
                              Instant lastLogin,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private Instant lastLogin;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withLastLogin(Instant lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserResponseDTO build() {
            return new UserResponseDTO(id, username, email, lastLogin, createdAt, updatedAt);
        }
    }
}


