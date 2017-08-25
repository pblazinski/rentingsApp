package pl.lodz.p.edu.grs.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    private List<Game> gameList;

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public void updateName(final String name) {
        this.name = name;
    }

}
