package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.search.SearchDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.SearchService;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceImplTest {

    private static final Long CATEGORY_ID = 1L;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private CategoryService categoryService;

    private SearchService searchService;

    @Before
    public void setUp() throws Exception {
        searchService = new SearchServiceImpl(gameRepository, categoryService);
    }

    @Test
    public void shouldReturnPageOfGamesAndInvokeMethodForNullCategoryWhenCategoryIsNull() throws Exception {
        //given
        SearchDto searchDto = SearchDto.builder()
                .minPrice(0.0)
                .maxPrice(Double.MAX_VALUE)
                .title("title")
                .build();
        Pageable pageable = mock(Pageable.class);

        Game game = new Game();
        Page<Game> games = new PageImpl<>(Collections.singletonList(game));

        when(gameRepository.searchGamesWithoutCategory(searchDto, pageable))
                .thenReturn(games);

        //when
        Page<Game> result = searchService.searchGames(searchDto, null, pageable);

        //then
        verifyZeroInteractions(categoryService);
        verify(gameRepository)
                .searchGamesWithoutCategory(searchDto, pageable);

        assertThat(result)
                .isNotNull()
                .isSameAs(games);
        assertThat(result.getContent())
                .containsExactly(game);
    }

    @Test
    public void shouldReturnPageOfGamesAndInvokeMethodForCategoryWhenCategoryIdIsGiven() throws Exception {
        //given
        SearchDto searchDto = SearchDto.builder()
                .minPrice(0.0)
                .maxPrice(Double.MAX_VALUE)
                .title("title")
                .build();
        Pageable pageable = mock(Pageable.class);
        Category category = mock(Category.class);
        Game game = new Game();
        Page<Game> games = new PageImpl<>(Collections.singletonList(game));

        when(categoryService.findOne(CATEGORY_ID))
                .thenReturn(category);
        when(gameRepository.searchGames(searchDto, category, pageable))
                .thenReturn(games);

        //when
        Page<Game> result = searchService.searchGames(searchDto, CATEGORY_ID, pageable);

        //then
        verify(categoryService)
                .findOne(CATEGORY_ID);
        verify(gameRepository)
                .searchGames(searchDto, category, pageable);

        assertThat(result)
                .isNotNull()
                .isSameAs(games);
        assertThat(result.getContent())
                .containsExactly(game);
    }
}