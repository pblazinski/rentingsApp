package pl.lodz.p.edu.grs.service.impl.it;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.util.CategoryUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceIT {

    private static final String BLANK_VALUE = "     ";

    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @After
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Before
    public void tearDown() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void shouldReturnListWithOneCategory() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);

        //when
        List<Category> categories = categoryService.findAll();

        //then
        assertThat(categories.size())
                .isEqualTo(1);

        Category result = categories.get(0);

        assertThat(result.getName())
                .isEqualTo(category.getName());
    }

    @Test
    public void shouldReturnPageWithOneCategory() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);

        //when
        Page<Category> categories = categoryService.findAll(new PageRequest(0, 10));

        //then
        assertThat(categories.getTotalElements())
                .isEqualTo(1);

        Category result = categories.getContent().get(0);

        assertThat(result.getName())
                .isEqualTo(category.getName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenAddCategoryWithBlankName() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        categoryDto.setName(BLANK_VALUE);
        //when
        categoryService.addCategory(categoryDto);
        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenAddCategoryWithNullName() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        categoryDto.setName(null);
        //when
        categoryService.addCategory(categoryDto);
        //then
    }

    @Test
    public void shouldRemoveCategory() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);
        Long id = category.getId();

        //when
        categoryService.remove(id);

        //then
        Boolean exists = categoryRepository.exists(id);
        assertThat(exists)
                .isFalse();
    }

    @Test
    public void shouldUpdateCategoryName() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);
        Long id = category.getId();
        String updated = "Updated";

        //when
        Category result = categoryService.updateCategory(updated, id);

        //then
        assertThat(result.getName())
                .isEqualTo(updated);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenUpdateCategoryNameWithBlankValue() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);
        Long id = category.getId();

        //when
        Throwable throwable = catchThrowable(() -> categoryService.updateCategory(BLANK_VALUE, id));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdateNameNotExistedCategory() {
        //given

        //when
        categoryService.updateCategory(BLANK_VALUE, 1L);
    }
}
