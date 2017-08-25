package pl.lodz.p.edu.grs.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.util.CategoryUtil;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CategoryFactoryTest {

    private CategoryFactory categoryFactory;

    private static final String NAME = CategoryUtil.NAME;

    @Before
    public void setUp() throws Exception {
        categoryFactory = new CategoryFactory();
    }

    @Test
    public void shouldCreateCategoryObject() throws Exception {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();

        //when
        Category result = categoryFactory.createCategory(categoryDto);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getName())
                .isEqualTo(NAME);
    }

    @Test
    public void shouldCreateTwoDifferentCategoryObjects() throws Exception {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();

        Category category = categoryFactory.createCategory(categoryDto);

        //when
        Category result = categoryFactory.createCategory(categoryDto);

        //then
        assertThat(result)
                .isNotSameAs(category);
    }
}
