package pl.lodz.p.edu.grs.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("api/category")
@Api(value = "api/category", description = "Endpoints for game categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    @ApiOperation(value = "Get all categories list")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    @ApiOperation(value = "Create category")
    public Category addNewCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update category")
    public Category updateCategory(@RequestBody Category category,
                                   @PathVariable Long categoryId) {
        return categoryService.updateCategory(category, categoryId);
    }

    @GetMapping("/{name}")
    @ApiOperation(value = "Find category by name")
    public Category getCategory(@PathVariable String name) {
        return categoryService.findByName(name);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete category")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
