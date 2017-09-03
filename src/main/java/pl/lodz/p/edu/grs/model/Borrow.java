package pl.lodz.p.edu.grs.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Cascade(CascadeType.PERSIST)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Game> borrowedGames = new ArrayList<>();

    @NotNull
    @ManyToOne
    private User user;

    @Column
    private LocalDateTime timeBack;

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

    public void updateTotalPrice(final double price) {
        this.totalPrice = price;
    }

    public void updatePenalties(final double penalties) {
        this.penalties = penalties;
    }

    public void updateTimeBack(final LocalDateTime timeBack) {
        this.timeBack = timeBack;
    }

    public void updateUser(final User user) {
        this.user = user;
    }

    public void updateBorrowedGames(final List<Game> borrowedGames) {
        this.borrowedGames.clear();
        this.borrowedGames.addAll(borrowedGames);
    }

    public void updateDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
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