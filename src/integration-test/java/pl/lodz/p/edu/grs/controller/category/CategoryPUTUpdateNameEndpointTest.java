package pl.lodz.p.edu.grs.controller.category;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.util.CategoryUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryPUTUpdateNameEndpointTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BLANK_VALUE = "  ";

    @Before
    public void setUp() throws Exception {
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    public void shouldReturnOkStatusWhenUpdateCategoryName() throws Exception {
        //given
        String UPDATED = "UPDATED";
        CategoryDto categoryDto = new CategoryDto(UPDATED);
        Category category = categoryService.addCategory(categoryDto);

        String content = objectMapper.writeValueAsString(categoryDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/category/%d", category.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        category = categoryRepository.findOne(category.getId());


        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED));
    }

    @Test
    public void shouldReturnBadRequestWhenUpdateCategoryNameWithBlankName() throws Exception {
        //given
        CategoryDto categoryDto1 = CategoryUtil.mockCategoryDto();
        CategoryDto categoryDto = new CategoryDto(BLANK_VALUE);
        Category category = categoryService.addCategory(categoryDto1);

        String content = objectMapper.writeValueAsString(categoryDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/category/%d", category.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        category = categoryRepository.findOne(category.getId());

        assertThat(category.getName())
                .isNotEmpty();

        result.andExpect(status().isBadRequest());
    }
}
