package pl.lodz.p.edu.grs.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.model.Category;

import java.util.List;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);

    List<Category> findAll();

    Category addCategory(Category category);

    Category findOne(Long id);

    Category findByName(String name);

    Category updateCategory(Category category, Long id);

    void deleteCategory(Long id);
}
