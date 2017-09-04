package pl.lodz.p.edu.grs.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import pl.lodz.p.edu.grs.model.Category;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private boolean available;

    @Column
    @JsonIgnore
    private LocalDateTime createdAt;

    @Min(0)
    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Valid
    @Embedded
    private RatingSummary ratingSummary = new RatingSummary();

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
    private void setDate() {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id != null ? id.equals(game.id) : game.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

    public void addRate(final Rate rate) {
        this.ratingSummary.addRate(rate);
    }
}
