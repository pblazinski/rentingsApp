package pl.lodz.p.edu.grs.util;


import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.model.Category;

public class CategoryUtil {

    public static final Long CATEGORY_ID = 1L;

    public static final String NAME = "FPS";


    private CategoryUtil() {
    }

    public static CategoryDto mockCategoryDto() {
        return new CategoryDto(NAME);
    }

    public static Category mockCategory() {
        return new Category(CATEGORY_ID, NAME);
    }


}
