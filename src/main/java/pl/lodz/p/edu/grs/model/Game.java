package pl.lodz.p.edu.grs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean available;

    @Column
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    private Category category;


    public Game(String title, String description, boolean available, double price) {
        this(null, title, description, available, price);
    }

    public Game(Long id, String title, String description, boolean available, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.available = available;
        this.price = price;
    }

    @PrePersist
    public void setDate() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }

}
