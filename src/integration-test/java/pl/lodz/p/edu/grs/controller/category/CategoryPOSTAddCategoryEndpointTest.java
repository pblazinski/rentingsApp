package pl.lodz.p.edu.grs.controller.category;

import com.fasterxml.jackson.core.type.TypeReference;
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
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.io.IOException;
import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryPOSTAddCategoryEndpointTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private static final String BLANK_VALUE = "  ";

    @Before
    public void setUp() throws Exception {
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        user = StubHelper.stubUser();
    }

    @Test
    public void shouldReturnOkStatusWhenAddCategory() throws Exception {
        //given
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();

        String content = objectMapper.writeValueAsString(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/category/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);
        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        String body = result.andReturn().getResponse().getContentAsString();
        Long id = getIdFromContentBody(body);
        Category category = categoryRepository.findOne(id);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    public void shouldReturnBadRequestWhenAddCategoryWithBlankName() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto(BLANK_VALUE);

        String content = objectMapper.writeValueAsString(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/category")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isBadRequest());
    }


    private long getIdFromContentBody(final String content) throws IOException {
        TypeReference<HashMap<String, String>> typeRef
                = new TypeReference<HashMap<String, String>>() {
        };

        HashMap<String, Object> map = objectMapper.readValue(content, typeRef);
        return Long.valueOf((String) map.get("id"));
    }
}
