package pl.lodz.p.edu.grs.controller.search;

import org.hamcrest.CoreMatchers;
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
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SearchGETSearchGamesEndpointTest {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private List<Category> categories;

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        categories = StubHelper.stubCategories();
        StubHelper.stubGames();
    }

    @Test
    public void shouldReturnEmptyPageWhenNotFoundCategory() throws Exception {
        //given

        MockHttpServletRequestBuilder requestBuilder = get(String
                .format("/api/search/games?title=&categoryId=%d", Long.MAX_VALUE));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }

    @Test
    public void shouldReturnEmptyPageWhenNotMatchingPrice() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = get(String
                .format("/api/search/games?title=&minPrice=%s&maxPrice=%s",
                        "160.0",
                        "200.0"));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }

    @Test
    public void shouldReturnEmptyPageWhenNotMatchingTitle() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = get(String
                .format("/api/search/games?title=%s", "not matching title"));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }

    @Test
    public void shouldReturnPageWithMatchingGameForCorrectPrice() throws Exception {
        //given
        String minPrice = "81.0";
        String maxPrice = "120.0";

        MockHttpServletRequestBuilder requestBuilder = get(String.format("/api/search/games?title=&minPrice=%s&maxPrice=%s",
                minPrice, maxPrice));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].price").exists())
                .andExpect(jsonPath("$.content[0].price").value(is(greaterThanOrEqualTo(Double.valueOf(minPrice)))))
                .andExpect(jsonPath("$.content[0].price").value(is(lessThanOrEqualTo(Double.valueOf(maxPrice)))))
                .andExpect(jsonPath("$.content[0].available").exists())
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }

    @Test
    public void shouldReturnPageWithMatchingGameForCorrectTitle() throws Exception {
        //given
        String title = "King";

        MockHttpServletRequestBuilder requestBuilder = get(String.format("/api/search/games?title=%s", title));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].title").value(is(CoreMatchers.containsString(title))))
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].price").exists())
                .andExpect(jsonPath("$.content[0].available").exists())
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }

    @Test
    public void shouldReturnPageWithMatchingGameForCorrectCategory() throws Exception {
        //given
        Category category = categories.get(1);
        MockHttpServletRequestBuilder requestBuilder = get(String.format("/api/search/games?title=&categoryId=%d", category.getId()));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].price").exists())
                .andExpect(jsonPath("$.content[0].available").exists())
                .andExpect(jsonPath("$.content[0].category.id").exists())
                .andExpect(jsonPath("$.content[0].category.id").value(category.getId()))
                .andExpect(jsonPath("$.content[0].category.name").exists())
                .andExpect(jsonPath("$.content[0].category.name").value(category.getName()))
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }

    @Test
    public void shouldReturnPageWithMatchingGameForCorrectCategoryTitleAndPrice() throws Exception {
        //given
        Category category = categories.get(1);
        String title = "King";
        String minPrice = "81.0";
        String maxPrice = "120.0";
        MockHttpServletRequestBuilder requestBuilder = get(
                String.format("/api/search/games?title=%s&categoryId=%d&minPrice=%s&maxPrice=%s", title, category.getId(),
                        minPrice, maxPrice)
        );

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].title").value(is(CoreMatchers.containsString(title))))
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].price").exists())
                .andExpect(jsonPath("$.content[0].price").value(is(greaterThanOrEqualTo(Double.valueOf(minPrice)))))
                .andExpect(jsonPath("$.content[0].price").value(is(lessThanOrEqualTo(Double.valueOf(maxPrice)))))
                .andExpect(jsonPath("$.content[0].available").exists())
                .andExpect(jsonPath("$.content[0].category.id").exists())
                .andExpect(jsonPath("$.content[0].category.id").value(category.getId()))
                .andExpect(jsonPath("$.content[0].category.name").exists())
                .andExpect(jsonPath("$.content[0].category.name").value(category.getName()))
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.numberOfElements").exists());
    }
}
