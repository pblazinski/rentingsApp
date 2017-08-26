package pl.lodz.p.edu.grs.controller.game;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.lodz.p.edu.grs.repository.CategoryRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class CategoryValidator implements Validator {

    private final CategoryRepository categoryRepository;

    public CategoryValidator(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void customValidation(final Long categoryId, final Errors errors) {

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Long.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(final Object o,final Errors errors) {
        Long categoryId = (Long) o;

        if(!categoryRepository.exists(categoryId)) {
            errors.rejectValue("categoryId", "categoryDoesNotExists");
        }
    }
}
