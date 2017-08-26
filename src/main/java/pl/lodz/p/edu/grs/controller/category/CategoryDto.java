package pl.lodz.p.edu.grs.controller.category;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDto {

    @NotBlank
    String name;

}
