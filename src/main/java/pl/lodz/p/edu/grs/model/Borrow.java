package pl.lodz.p.edu.grs.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.edu.grs.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column(nullable = false)
    private List<Game> borrowedGames;

    @ManyToOne
    @Column(nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime timeBorrowed;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Min(0)
    @Column(nullable = false)
    private double totalPrice;

    @Min(0)
    @Column
    private double penalties;

    public Borrow(List<Game> borrowedGames, User user) {
        this.borrowedGames = borrowedGames;
        this.user = user;
    }

    @PrePersist
    public void setDate() {
        this.timeBorrowed = LocalDateTime.now();
        this.deadline = LocalDateTime.now().plusDays(7);
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "user=" + user +
                ", timeBorrowed=" + timeBorrowed +
                ", deadline=" + deadline +
                ", totalPrice=" + totalPrice +
                '}';
    }
}