package pl.lodz.p.edu.grs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    public Category addNewCategory(Category category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("/{name}")
    public Category getCategory(@PathVariable String name) {
        return categoryService.findByName(name);
    }

    @PostMapping("/{id}")
    public void removeCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
    }
}
