package pl.lodz.p.edu.grs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.edu.grs.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    Category findByName(String name);
}
