package pl.lodz.p.edu.grs.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import pl.lodz.p.edu.grs.exceptions.UserRoleException;
import pl.lodz.p.edu.grs.model.game.Game;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = UserConstants.MAX_SIZE_FIRST_NAME)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = UserConstants.MAX_SIZE_LAST_NAME)
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    public boolean active;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Game> borrowed;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(final String email, final String firstName, final String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void updateNames(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void updateEmail(final String email) {
        this.email = email;
    }

    public void grant(final Role role) {
        if (this.roles.contains(role)) {
            throw new UserRoleException("Cannot add same role for user");
        }
        this.roles.add(role);
    }

    public void revoke(final Role role) {
        if (!this.roles.contains(role)) {
            throw new UserRoleException(String.format("User don't have role %s", role));
        }
        this.roles.remove(role);
    }

    public void updateActive(final boolean active) {
        this.active = active;
    }
}