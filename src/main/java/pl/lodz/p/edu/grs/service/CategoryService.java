package pl.lodz.p.edu.grs.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.model.Category;

import java.util.List;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);

    List<Category> findAll();

    Category addCategory(CategoryDto categoryDto);

    Category findOne(Long id);

    Category findByName(String name);

    Category updateCategory(String name, Long id);

    void remove(Long id);
}
