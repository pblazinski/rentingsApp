package pl.lodz.p.edu.grs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.exceptions.CategoryInUseException;
import pl.lodz.p.edu.grs.exceptions.NotFoundException;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private GameRepository gameRepository;

    @Autowired
    public CategoryServiceImpl(GameRepository gameRepository,
                               CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.gameRepository = gameRepository;
    }


    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public Category findOne(Long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category updated = categoryRepository.findOne(id);
        if (updated == null) {
            throw new NotFoundException(String.format("Category with id=[%d] not found!", category.getId()));
        }
        updated.setName(category.getName());

        return categoryRepository.save(updated);
    }


    @Override
    public void deleteCategory(Long id) {
        if (!gameRepository.findByCategoryId(id).isEmpty()) {
            throw new CategoryInUseException(String.format("Category with id=[%d] in use!", id));
        }
        categoryRepository.delete(id);
    }
}
