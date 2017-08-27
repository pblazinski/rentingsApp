package pl.lodz.p.edu.grs.controller.category;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.StubHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryDELETERemoveEndpointTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() throws Exception {
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        user = StubHelper.stubUser();
    }


    @Test
    public void shouldReturnOkWhenRemoveCategory() throws Exception {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);

        MockHttpServletRequestBuilder requestBuilder = delete(String.format("/api/category/%d", category.getId()))
                .with(user(new AppUser(user)));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        category = categoryRepository.findOne(category.getId());

        assertThat(category)
                .isNull();

        result.andExpect(status().isOk());
    }
}
