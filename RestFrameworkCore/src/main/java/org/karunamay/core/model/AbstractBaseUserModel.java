package org.karunamay.core.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.karunamay.core.security.PasswordHasher;

import java.time.Instant;


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

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "fk_user_role"),
            referencedColumnName = "ID"
    )
    private RoleModel role;

    protected AbstractBaseUserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    protected String getHashPassword() {
        return PasswordHasher.hash(this.getPassword());
    }

    protected boolean matchPassword(String password) {
        return this.getPassword().equals(PasswordHasher.hash(password));
    }

    protected void changePassword(String password) {
        this.setPassword(PasswordHasher.hash(password));

    }
}
