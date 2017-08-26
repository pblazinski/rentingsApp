package pl.lodz.p.edu.grs.model;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

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

    @NotBlank
    @Column(nullable = false)
    private String name;


    public Category(String name) {
        this.name = name;
    }

    public void updateName(final String name) {
        this.name = name;
    }

}
