package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.exceptions.NotFoundException;
import pl.lodz.p.edu.grs.factory.CategoryFactory;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.service.CategoryService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryFactory categoryFactory;

    @Autowired
    public CategoryServiceImpl(final CategoryRepository categoryRepository,
                               final CategoryFactory categoryFactory) {
        this.categoryRepository = categoryRepository;
        this.categoryFactory = categoryFactory;
    }


    @Override
    public Page<Category> findAll(final Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);

        log.info("Found <{}> categories in page", categories.getTotalElements());

        return categories;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categoryList = categoryRepository.findAll();

        log.info("Found <{}> categories in list", categoryList.size());

        return categoryList;
    }

    @Override
    public Category addCategory(final CategoryDto categoryDto) {
        Category category = categoryFactory.createCategory(categoryDto);

        category = categoryRepository.save(category);

        log.info("Add category <{}> with name <{}>", category.getId(), category.getName());

        return category;
    }

    @Override
    public Category findOne(final Long id) {
        Category category = categoryRepository.findOne(id);
        if( category == null) {
            throw new NotFoundException("Category Entity Not Found");
        }

        log.info("Found category <{}> with name <{}>", category.getId(), category.getName());

        return category;
    }

    @Override
    public Category findByName(final String name) {
        Category category = categoryRepository.findByName(name);

        log.info("Found category <{}> with name <{}>", category.getId(), category.getName());

        return category;
    }

    @Override
    public Category updateCategory(final String name,
                                   final Long id) {
        Category category = categoryRepository.getOne(id);

        log.info("Update category <{}> name to <{}>", category.getId(), category.getName());

        category.updateName(name);

        return categoryRepository.save(category);
    }


    @Override
    public void remove(final Long id) {
        Category category = categoryRepository.getOne(id);

        categoryRepository.delete(id);

        log.info("Removed category with id <{}>", category.getId());
    }
}
