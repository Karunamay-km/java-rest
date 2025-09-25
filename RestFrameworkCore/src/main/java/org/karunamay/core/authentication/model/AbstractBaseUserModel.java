package org.karunamay.core.authentication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.karunamay.core.security.PasswordHasher;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractBaseUserModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long ID;

    @Column(length = 40, unique = true, nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private String username;

    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(updatable = false)
    private Instant lastLogin;

    protected AbstractBaseUserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    protected String hashPassword() {
        return PasswordHasher.hash(this.getPassword());
    }

    protected boolean matchPassword(String password) {
        return this.getPassword().equals(PasswordHasher.hash(password));
    }

    protected void changePassword(String password) {
        this.setPassword(PasswordHasher.hash(password));

    }
}
