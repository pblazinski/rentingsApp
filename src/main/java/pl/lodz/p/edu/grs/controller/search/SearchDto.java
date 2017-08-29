package pl.lodz.p.edu.grs.controller.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {

    private double minPrice;
    private double maxPrice;
    private long categoryId;
    private String title;

}
