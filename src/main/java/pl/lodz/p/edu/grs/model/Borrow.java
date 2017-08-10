package pl.lodz.p.edu.grs.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private List<Game> borrowedGames;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime timeBorrowed;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private double totalPrice;

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