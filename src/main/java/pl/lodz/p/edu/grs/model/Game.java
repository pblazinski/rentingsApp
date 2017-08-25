package pl.lodz.p.edu.grs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonIgnore
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

    public void updateTitleAndDescription(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public void updatePrice(final double price) {
        this.price = price;
    }

    public void updateAvailability(boolean available) {
        this.available = available;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

}
