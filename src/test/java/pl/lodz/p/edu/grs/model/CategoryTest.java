package pl.lodz.p.edu.grs.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class CategoryTest {

    private static final String NAME = "CATEGORY NAME";

    private Category category;

    @Before
    public void setUp() throws Exception {
        category = new Category();
    }

    @Test
    public void shouldSetCategoryName() throws Exception {
        //given

        //when
        category.updateName(NAME);

        //then
        assertThat(category.getName())
                .isNotBlank()
                .isEqualTo(NAME);
    }

    @Test
    public void shouldReplaceCategoryName() throws Exception {
        //given
        final String name = "category";
        category.updateName(name);

        //when
        category.updateName(NAME);

        //then
        assertThat(category.getName())
                .isNotBlank()
                .isNotSameAs(name)
                .isEqualTo(NAME);
    }
}