package de.gdevelop.library.app.category.services;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.category.repository.CategoryRepository;
import de.gdevelop.library.app.common.exception.CategoryAlreadyExistsException;
import de.gdevelop.library.app.common.exception.CategoryNotFoundException;
import de.gdevelop.library.app.common.exception.FieldNotValidException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Stateless
public class CategoryServices {

	@Inject
	Validator validator;

	@Inject
	CategoryRepository repository;

	public Category add(final Category category) throws FieldNotValidException, CategoryAlreadyExistsException {

		validateCategory(category);

		return repository.add(category);
	}

	public void update(final Category category)
			throws FieldNotValidException, CategoryAlreadyExistsException, CategoryNotFoundException {

		validateCategory(category);
		if (repository.findById(category.getId()) == null)
			throw new CategoryNotFoundException("Category not found. Category:" + category);

		repository.update(category);
	}

	public Category findById(final long id) throws CategoryNotFoundException {
		final Category category = repository.findById(id);

		if (category == null)
			throw new CategoryNotFoundException("Category not found. Id: " + id);
		return category;
	}

	public Category findByName(final String name) throws CategoryNotFoundException {
		final Category category = repository.findByName(name);

		if (category == null)
			throw new CategoryNotFoundException("Category not found. Name: " + name);
		return category;
	}

	public void setValidator(final Validator validator) {
		this.validator = validator;

	}

	public void setRepository(final CategoryRepository categoryRepository) {
		this.repository = categoryRepository;
	}

	public List<Category> findAll() {
		return this.repository.findAll(
				(o1, o2) -> o1.getName().compareTo(o2.getName()));
	}

	public List<Category> findAll(final Comparator<Category> comparator) {
		return this.repository.findAll(comparator);
	}

	private void validateCategory(final Category category)
			throws FieldNotValidException, CategoryAlreadyExistsException {

		final Set<ConstraintViolation<Category>> errors = validator.validate(category);
		final Iterator<ConstraintViolation<Category>> iter = errors.iterator();
		if (iter.hasNext()) {
			final ConstraintViolation<Category> violation = iter.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}

		if (repository.categoryExists(category))
			throw new CategoryAlreadyExistsException("Category already exists: " + category);
	}

}
