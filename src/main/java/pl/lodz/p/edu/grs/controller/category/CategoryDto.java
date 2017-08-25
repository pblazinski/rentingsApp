package pl.lodz.p.edu.grs.controller.category;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class CategoryDto {

    @NotBlank
    String name;

}
