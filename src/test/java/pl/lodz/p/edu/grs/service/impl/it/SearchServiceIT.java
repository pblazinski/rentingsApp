package pl.lodz.p.edu.grs.service.impl.it;

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
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.search.SearchDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.SearchService;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SearchServiceIT {

    @Autowired
    private SearchService searchService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private List<Category> categories;

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        userRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        categories = StubHelper.stubCategories();
        List<Game> games = StubHelper.stubGames();
    }

    @Test
    public void shouldReturnEmptyPageWhenNotFoundGamesForNotExistingCategory() throws Exception {
        //given
        SearchDto searchDto = SearchDto.builder()
                .title("")
                .maxPrice(Double.MAX_VALUE)
                .minPrice(0.0)
                .build();

        //when
        Page<Game> result = searchService.searchGames(searchDto, Long.MAX_VALUE, new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(0);
    }

    @Test
    public void shouldReturnEmptyPageWhenNotFoundGamesForNotMatchingTitle() throws Exception {
        //given
        SearchDto searchDto = SearchDto.builder()
                .title("asdasdasczxdasd sad asd asd asdasdsa")
                .maxPrice(Double.MAX_VALUE)
                .minPrice(0.0)
                .build();

        //when
        Page<Game> result = searchService.searchGames(searchDto, categories.get(0).getId(), new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(0);
    }

    @Test
    public void shouldReturnEmptyPageWhenNotFoundGamesForExistingCategoryButMissingPriceAndTitle() throws Exception {
        //given
        SearchDto searchDto = SearchDto.builder()
                .title("title")
                .maxPrice(Double.MAX_VALUE)
                .minPrice(Double.MAX_VALUE / 2)
                .build();

        //when
        Page<Game> result = searchService.searchGames(searchDto, categories.get(0).getId(), new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(0);
    }

    @Test
    public void shouldReturnGamesWithMatchingCategory() throws Exception {
        //given
        SearchDto searchDto = SearchDto.builder()
                .title("")
                .maxPrice(Double.MAX_VALUE)
                .minPrice(0.0)
                .build();

        Category category = categories.get(0);

        //when
        Page<Game> result = searchService.searchGames(searchDto, category.getId(), new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(1);

        Game actual = result.getContent().get(0);

        assertThat(actual.getCategory().getId())
                .isEqualTo(category.getId());
        assertThat(actual.getCategory().getName())
                .isEqualTo(category.getName());
    }

    @Test
    public void shouldReturnGamesWithTitleWhichContainKingIgnoringCase() throws Exception {
        //given
        String title = "king";
        SearchDto searchDto = SearchDto.builder()
                .title(title)
                .maxPrice(Double.MAX_VALUE)
                .minPrice(0.0)
                .build();

        //when
        Page<Game> result = searchService.searchGames(searchDto, null, new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(1);

        Game actual = result.getContent().get(0);

        assertThat(actual.getTitle().toLowerCase())
                .contains(title.toLowerCase());
    }

    @Test
    public void shouldReturnGamesWithMatchingPrice() throws Exception {
        //given
        double minPrice = 10;
        double maxPrice = 100;
        SearchDto searchDto = SearchDto.builder()
                .title("")
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .build();

        //when
        Page<Game> result = searchService.searchGames(searchDto, null, new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(1);

        Game actual = result.getContent().get(0);

        assertThat(actual.getPrice())
                .isLessThanOrEqualTo(maxPrice)
                .isGreaterThanOrEqualTo(minPrice);
    }

    @Test
    public void shouldReturnGamesWithMatchingPriceTitleAndCategory() throws Exception {
        //given
        double minPrice = 10;
        double maxPrice = 100;
        String title = "Quake";

        SearchDto searchDto = SearchDto.builder()
                .title(title)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .build();

        Category category = categories.get(0);

        //when
        Page<Game> result = searchService.searchGames(searchDto, category.getId(), new PageRequest(0, 10));

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getContent())
                .hasSize(1);

        Game actual = result.getContent().get(0);

        assertThat(actual.getTitle().toLowerCase())
                .isEqualTo(title.toLowerCase());
        assertThat(actual.getPrice())
                .isLessThanOrEqualTo(maxPrice)
                .isGreaterThanOrEqualTo(minPrice);

        assertThat(actual.getCategory().getId())
                .isEqualTo(category.getId());
        assertThat(actual.getCategory().getName())
                .isEqualTo(category.getName());
    }
}
