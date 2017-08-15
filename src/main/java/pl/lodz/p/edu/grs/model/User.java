package pl.lodz.p.edu.grs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String firstName;

//    @Column
//    private String password;

    @Column
    private String surName;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Game> borrowed;

    public User(String login, String email, boolean active) {
        this.login = login;
        this.email = email;
        this.active = active;
    }

//    public void setPassword(String password) {
//        this.password = new BCryptPasswordEncoder().encode(password);
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}
