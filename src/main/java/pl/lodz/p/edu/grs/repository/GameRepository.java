package pl.lodz.p.edu.grs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lodz.p.edu.grs.controller.search.SearchDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "select g from Game g where g.price >= :#{#searchDto.minPrice}" +
            " and g.price <= :#{#searchDto.maxPrice} " +
            " and g.category.id = :#{#categoryId} " +
            " and lower(g.title) like '%' || lower(:#{#searchDto.title}) || '%'",
            countQuery = "select count(g) from Game g  where g.price >= :#{#searchDto.minPrice}" +
                    " and g.price <= :#{#searchDto.maxPrice} " +
                    " and g.category.id = :#{#categoryId} " +
                    " and lower(g.title) like '%' || lower(:#{#searchDto.title}) || '%'")
    Page<Game> searchGames(@Param("searchDto") SearchDto searchDto,
                           @Param("categoryId") long categoryId,
                           Pageable pageable);

    @Query(value = "select g from Game g where g.price >= :#{#searchDto.minPrice}" +
            " and g.price <= :#{#searchDto.maxPrice} " +
            " and lower(g.title) like '%' || lower(:#{#searchDto.title}) || '%'",
            countQuery = "select count(g) from Game g where g.price >= :#{#searchDto.minPrice}" +
                    " and g.price <= :#{#searchDto.maxPrice} " +
                    " and lower(g.title) like '%' || lower(:#{#searchDto.title}) || '%'")
    Page<Game> searchGamesWithoutCategory(@Param("searchDto") SearchDto searchDto,
                                          Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM Game g where g.id != ?1 and  g.category_id = " +
                    "(SELECT g.category_id FROM Game g WHERE g.id = ?1)")
    List<Game> findAllWithoutSelectWithCategorySameAsSelectedGame(long gameId);
}
