package pl.lodz.p.edu.grs.controller.borrow;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class BorrowDto {

    @NotEmpty
    private List<Long> games;
}
