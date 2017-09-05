package pl.lodz.p.edu.grs.controller.borrow;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDto {

    @NotEmpty
    private List<Long> games;
}
