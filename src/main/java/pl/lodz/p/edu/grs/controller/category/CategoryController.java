package pl.lodz.p.edu.grs.controller.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/category")
@Api(value = "api/category", description = "Endpoints for game categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ApiOperation(value = "Get all categories list")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    @ApiOperation(value = "Create category")
    @PreAuthorize("hasAuthority('MODIFY_CATEGORY')")
    public Category addCategory(@RequestBody @Valid final CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/{categoryId}")
    @ApiOperation(value = "Update category")
    @PreAuthorize("hasAuthority('MODIFY_CATEGORY')")
    public HttpEntity updateName(@PathVariable final Long categoryId,
                                 @RequestBody @Valid final CategoryDto categoryDto) {
        Category category = categoryService.updateCategory(categoryDto.getName(), categoryId);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/{name}")
    @ApiOperation(value = "Find category by name")
    public Category getCategory(@PathVariable String name) {
        return categoryService.findByName(name);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete category")
    @PreAuthorize("hasAuthority('MODIFY_CATEGORY')")
    public HttpEntity remove(@PathVariable Long id) {
        categoryService.remove(id);

        return ResponseEntity.ok().build();
    }
}
