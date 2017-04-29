package de.gdevelop.library.app.category.resource;

import static de.gdevelop.library.app.category.CategoryForRepositoryTests.*;
import static de.gdevelop.library.app.common.utils.FileTestNameUtil.*;
import static de.gdevelop.library.app.common.utils.JsonTestUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.category.services.CategoryServices;
import de.gdevelop.library.app.common.exception.CategoryAlreadyExistsException;
import de.gdevelop.library.app.common.exception.CategoryNotFoundException;
import de.gdevelop.library.app.common.exception.FieldNotValidException;
import de.gdevelop.library.app.common.model.HttpCode;
import de.gdevelop.library.app.common.utils.ResourceDefinitions;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CategoryResourceTest {

	private static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();

	CategoryResource cut;

	@Mock
	CategoryServices categoryServices;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		cut = new CategoryResource();
		cut.setCategoryServices(this.categoryServices);
		cut.setCategoryJsonConverter(new CategoryJsonConverter());
	}

	@Test
	public void addValidCategory() {
		when(categoryServices.add(java())).thenReturn(withId(java(), 1l));

		final Response response = cut.add(
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "newCategory.json")));

		assertThat(response.getStatus(), is(HttpCode.CREATED.getCode()));
		assertJsonMatchesExpected(response.getEntity().toString(), "{\"id\": 1}");
	}

	@Test
	public void addExistsCategory() {
		when(categoryServices.add(java())).thenThrow(new CategoryAlreadyExistsException());

		final Response response = cut.add(
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "newCategory.json")));

		assertThat(response.getStatus(), is(HttpCode.VALIDATION_ERROR.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryAlreadyExists.json"));
	}

	@Test
	public void addCategoryWithNullName() {
		when(categoryServices.add(new Category())).thenThrow(new FieldNotValidException("name", "may not be null"));

		final Response response = cut.add(
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "categoryWithNullName.json")));

		assertThat(response.getStatus(), is(HttpCode.VALIDATION_ERROR.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryErrorNullName.json"));
	}

	@Test
	public void updateValidCategory() {
		final Response response = cut.update(1l,
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "category.json")));

		assertThat(response.getStatus(), is(HttpCode.OK.getCode()));
		assertJsonMatchesExpected(response.getEntity().toString(), "");

		verify(categoryServices).update(withId(java(), 1l));
	}

	@Test
	public void updateCategoryWithNameOfOtherCategory() {
		doThrow(new CategoryAlreadyExistsException()).when(categoryServices).update(withId(java(), 1l));

		final Response response = cut.update(1l,
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "category.json")));

		assertThat(response.getStatus(), is(HttpCode.VALIDATION_ERROR.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryAlreadyExists.json"));

	}

	@Test
	public void updateCategoryWithNullName() {

		doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices)
				.update(withId(new Category(), 1l));

		final Response response = cut.update(1l,
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "categoryWithNullName.json")));

		assertThat(response.getStatus(), is(HttpCode.VALIDATION_ERROR.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryErrorNullName.json"));
	}

	@Test
	public void updateCategoryNotFound() {

		doThrow(new CategoryNotFoundException()).when(categoryServices)
				.update(withId(java(), 2l));

		final Response response = cut.update(2l,
				readJsonFile(getRequestFilePath(PATH_RESOURCE, "category.json")));

		assertThat(response.getStatus(), is(HttpCode.NOT_FOUND.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryNotFound.json"));
	}

	@Test
	public void findById() {
		when(categoryServices.findById(1l)).thenReturn(withId(java(), 1l));

		final Response response = cut.findById(1l);

		assertThat(response.getStatus(), is(HttpCode.OK.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryFound.json"));

	}

	@Test
	public void findByIdNotFound() {
		doThrow(new CategoryNotFoundException()).when(categoryServices)
				.findById(2l);

		final Response response = cut.findById(2l);

		assertThat(response.getStatus(), is(HttpCode.NOT_FOUND.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryNotFound.json"));
	}

	@Test
	public void findByName() {
		when(categoryServices.findByName(java().getName())).thenReturn(withId(java(), 1l));

		final Response response = cut.findByName(java().getName());

		assertThat(response.getStatus(), is(HttpCode.OK.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryFound.json"));

	}

	@Test
	public void findByNameNotFound() {
		doThrow(new CategoryNotFoundException()).when(categoryServices)
				.findByName(networks().getName());

		final Response response = cut.findByName(networks().getName());

		assertThat(response.getStatus(), is(HttpCode.NOT_FOUND.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoryNotFound.json"));
	}

	@Test
	public void findAll() {
		when(categoryServices.findAll()).thenReturn(
				Arrays.asList(withId(java(), 1l), withId(networks(), 2l)));

		final Response response = cut.findAll();

		assertThat(response.getStatus(), is(HttpCode.OK.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoriesFound.json"));

	}

	@Test
	public void findAllNotFound() {
		when(categoryServices.findAll()).thenReturn(Arrays.asList());

		final Response response = cut.findAll();

		assertThat(response.getStatus(), is(HttpCode.OK.getCode()));
		assertJsonMatchesFileContent(response.getEntity().toString(),
				getResponseFilePath(PATH_RESOURCE, "categoriesNotFound.json"));
	}

}
