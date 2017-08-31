package pl.lodz.p.edu.grs.controller.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchDto {

    private Double minPrice;
    private Double maxPrice;
    private String title;

}
