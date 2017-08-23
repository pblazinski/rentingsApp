package pl.lodz.p.edu.grs.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Category addNewCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@RequestBody Category category,
                                   @PathVariable Long categoryId) {
        return categoryService.updateCategory(category, categoryId);
    }

    @GetMapping("/{name}")
    public Category getCategory(@PathVariable String name) {
        return categoryService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
