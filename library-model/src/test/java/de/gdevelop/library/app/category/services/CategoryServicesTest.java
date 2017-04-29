package de.gdevelop.library.app.category.services;

import static de.gdevelop.library.app.category.CategoryForRepositoryTests.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.category.repository.CategoryRepository;
import de.gdevelop.library.app.common.exception.CategoryAlreadyExistsException;
import de.gdevelop.library.app.common.exception.CategoryNotFoundException;
import de.gdevelop.library.app.common.exception.FieldNotValidException;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

public class CategoryServicesTest {

	private CategoryRepository categoryRepository;
	private CategoryServices cut;
	private Validator validator;

	private static String TOO_SHORT_NAME = "A";
	private static String TOO_LONG_NAME = "This name is a bit too long.";

	@Before
	public void init() {

		this.categoryRepository = mock(CategoryRepository.class);
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		this.cut = new CategoryServices();
		cut.setValidator(this.validator);
		cut.setRepository(this.categoryRepository);
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithTooShortName() {
		addCategoryWithInvalidName(TOO_SHORT_NAME);
	}

	@Test
	public void addCategoryWithTooLongName() {
		addCategoryWithInvalidName(TOO_LONG_NAME);
	}

	@Test(expected = CategoryAlreadyExistsException.class)
	public void addCategoryWithExistentName() {
		when(categoryRepository.categoryExists(java())).thenReturn(true);
		cut.update(java());
	}

	@Test
	public void addValidCategory() {
		when(categoryRepository.categoryExists(java())).thenReturn(false);
		when(categoryRepository.add(java())).thenReturn(withId(java(), 1l));

		final Category addedCategory = cut.add(java());

		assertThat(addedCategory.getName(), is(equalTo(java().getName())));
		assertThat(addedCategory.getId(), is(1l));
	}

	private void addCategoryWithInvalidName(final String name) {
		try {
			cut.add(new Category(name));
			fail("Should have thrown validation exception");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithTooShortName() {
		updateCategoryWithInvalidName(TOO_SHORT_NAME);
	}

	@Test
	public void updateCategoryWithTooLongName() {
		updateCategoryWithInvalidName(TOO_LONG_NAME);
	}

	@Test(expected = CategoryAlreadyExistsException.class)
	public void udpateCategoryWithExistentName() {
		final Category cat = withId(java(), 1l);
		when(categoryRepository.categoryExists(cat)).thenReturn(true);
		cut.update(cat);
	}

	@Test(expected = CategoryNotFoundException.class)
	public void udpateNonExistentCategory() {
		when(categoryRepository.findById(1l)).thenReturn(null);

		cut.update(withId(java(), 1l));
	}

	@Test
	public void updateValidCategory() {
		final Category cat = withId(cleanCode(), 1l);
		when(categoryRepository.categoryExists(cat)).thenReturn(false);
		when(categoryRepository.findById(1l)).thenReturn(cat);

		cut.update(cat);

		verify(categoryRepository).update(cat);

	}

	private void updateCategoryWithInvalidName(final String name) {
		try {
			cut.update(new Category(name));
			fail("Should have thrown validation exception");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	@Test
	public void findById() {
		final Category cat = withId(cleanCode(), 1l);
		when(categoryRepository.findById(1l)).thenReturn(cat);

		final Category foundCategory = cut.findById(1l);

		assertThat(foundCategory, is(notNullValue()));
		assertThat(foundCategory.getId(), is(cat.getId()));
		assertThat(foundCategory.getName(), is(cat.getName()));

	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryRepository.findById(1l)).thenReturn(null);

		cut.findById(1l);
	}

	@Test
	public void findByName() {
		final Category cat = withId(cleanCode(), 1l);
		when(categoryRepository.findByName(cleanCode().getName())).thenReturn(cat);

		final Category foundCategory = cut.findByName(cleanCode().getName());

		assertThat(foundCategory, is(notNullValue()));
		assertThat(foundCategory.getId(), is(cat.getId()));
		assertThat(foundCategory.getName(), is(cat.getName()));

	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByNameNotFound() {
		when(categoryRepository.findByName(java().getName())).thenReturn(null);

		cut.findByName(java().getName());

	}

	@Test
	public void findAllNoCategoriesWithComparator() {
		when(categoryRepository.findAll((o1, o2) -> o1.getName().compareTo(o2.getName())))
				.thenReturn(new ArrayList<>());

		final List<Category> categories = cut.findAll((o1, o2) -> o1.getName().compareTo(o2.getName()));
		assertThat(categories.isEmpty(), is(true));
	}

	@Test
	public void findAllNoCategoriesNoComparator() {
		when(categoryRepository.findAll((o1, o2) -> o1.getName().compareTo(o2.getName())))
				.thenReturn(new ArrayList<>());

		final List<Category> categories = cut.findAll();
		assertThat(categories.isEmpty(), is(true));
	}

	@Test
	public void findAllCategoriesWithComparator() {
		final Comparator<Category> comparator = (o1, o2) -> o2.getName().compareTo(o1.getName());
		final List<Category> cats = Arrays.asList(withId(java(), 1l), withId(networks(), 2l));
		cats.sort(comparator);
		when(categoryRepository.findAll(comparator))
				.thenReturn(cats);

		final List<Category> categories = cut.findAll(comparator);

		assertThat(categories.isEmpty(), is(false));
		assertThat(categories.get(0).getName(), is(equalTo(networks().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(java().getName())));
	}

	@Test
	public void findAllCategoriesNoComparator() {
		final Comparator<Category> comparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
		when(categoryRepository.findAll(comparator))
				.thenReturn(Arrays.asList(withId(java(), 1l), withId(networks(), 2l)));

		final List<Category> categories = cut.findAll(comparator);

		assertThat(categories.isEmpty(), is(false));
		assertThat(categories.size(), is(2));
		assertThat(categories.get(0).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(networks().getName())));
	}

}
