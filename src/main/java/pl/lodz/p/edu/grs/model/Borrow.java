package pl.lodz.p.edu.grs.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class    Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private List<Game> borrowedGames;

    @Column(nullable = false)
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

    public List<Game> getBorrowedGames() {
        return borrowedGames;
    }

    public void setBorrowedGames(List<Game> borrowedGames) {
        this.borrowedGames = borrowedGames;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimeBorrowed() {
        return timeBorrowed;
    }

    public void setTimeBorrowed(LocalDateTime timeBorrowed) {
        this.timeBorrowed = timeBorrowed;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPenalties() {
        return penalties;
    }

    public void setPenalties(double penalties) {
        this.penalties = penalties;
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