package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.factory.CategoryFactory;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.util.CategoryUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryFactory categoryFactory;

    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
        this.categoryService = new CategoryServiceImpl(categoryRepository, categoryFactory);
    }

    @Test
    public void shouldReturnPageOfUsers() throws Exception {
        //given
        Pageable pageable = new PageRequest(0, 10);
        Category category = new Category();
        List<Category> categories = Collections.singletonList(category);
        PageImpl<Category> categoriesPage = new PageImpl<>(categories);

        when(categoryRepository.findAll(pageable))
                .thenReturn(categoriesPage);

        //when
        Page<Category> result = categoryService.findAll(pageable);

        //then
        verify(categoryRepository)
                .findAll(pageable);

        assertThat(result)
                .isEqualTo(categoriesPage);
        assertThat(result.getContent())
                .containsExactly(category);
        assertThat(result.getTotalElements())
                .isEqualTo(categories.size());
    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        //given
        Category category = new Category();
        Category category2 = new Category();
        List<Category> categories = Arrays.asList(category, category2);

        when(categoryRepository.findAll())
                .thenReturn(categories);

        //when
        List<Category> result = categoryService.findAll();

        //then
        verify(categoryRepository)
                .findAll();

        assertThat(result)
                .isNotEmpty()
                .isEqualTo(categories);
        assertThat(result.size())
                .isEqualTo(2);
        assertThat(result.containsAll(categories))
                .isTrue();
    }

    @Test
    public void shouldAddCategory() {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = CategoryUtil.mockCategory();

        when(categoryFactory.createCategory(categoryDto))
                .thenReturn(category);
        when(categoryRepository.save(category))
                .thenReturn(category);

        //when
        Category result = categoryService.addCategory(categoryDto);

        //then
        verify(categoryFactory)
                .createCategory(categoryDto);
        verify(categoryRepository)
                .save(category);

        assertThat(result)
                .isNotNull();
        assertThat(result.getId())
                .isEqualTo(CategoryUtil.CATEGORY_ID);
        assertThat(result.getName())
                .isEqualTo(CategoryUtil.NAME);
    }

    @Test
    public void shouldFindOne() throws Exception {
        //given
        Category category = CategoryUtil.mockCategory();

        when(categoryRepository.findOne(category.getId()))
                .thenReturn(category);

        //when
        Category result = categoryService.findOne(category.getId());


        //then
        verify(categoryRepository)
                .findOne(category.getId());
        assertThat(result)
                .isNotNull()
                .isEqualTo(category);
    }

    @Test
    public void shouldFindCategoryByName() throws Exception {
        //given
        String categoryName = "FPS";
        Category category = CategoryUtil.mockCategory();

        when(categoryRepository.findByName(category.getName()))
                .thenReturn(category);

        //when
        Category result = categoryService.findByName(categoryName);

        //then
        verify(categoryRepository)
                .findByName(categoryName);

        assertThat(result)
                .isNotNull()
                .isEqualTo(category);
    }

    @Test
    public void shouldUpdateCategoryName() throws Exception {
        //given
        Category category = CategoryUtil.mockCategory();

        String categoryName = "Updated";

        Long categoryId = CategoryUtil.CATEGORY_ID;

        when(categoryRepository.getOne(categoryId))
                .thenReturn(category);
        when(categoryRepository.save(category))
                .thenReturn(category);

        //when
        Category result = categoryService.updateCategory(categoryName, categoryId);

        //then
        verify(categoryRepository)
                .save(category);

        assertThat(result.getId())
                .isEqualTo(category.getId());
        assertThat(result.getName())
                .isEqualTo(categoryName);
    }

    @Test
    public void shouldDeleteCategory() throws Exception {
        //given
        Long categoryId = 1L;
        Category category = CategoryUtil.mockCategory();

        when(categoryRepository.getOne(categoryId))
                .thenReturn(category);

        //when
        categoryService.remove(categoryId);

        //then
        verify(categoryRepository)
                .getOne(categoryId);
        verify(categoryRepository)
                .delete(categoryId);
    }
}
