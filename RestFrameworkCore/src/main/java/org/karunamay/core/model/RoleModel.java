package org.karunamay.core.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private String role;

    @Column(name = "is_active")
    @Setter(AccessLevel.PROTECTED)
    private Boolean isActive = false;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<UserModel> userModelSet;

    @Column(name="privilege", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Integer privilege;

    public RoleModel(String role, Boolean isActive) {
        this.role = role;
        this.isActive = isActive;
    }
}
