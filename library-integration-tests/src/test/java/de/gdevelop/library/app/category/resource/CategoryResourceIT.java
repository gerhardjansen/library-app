package de.gdevelop.library.app.category.resource;

import static de.gdevelop.library.app.category.CategoryForRepositoryTests.*;
import static de.gdevelop.library.app.common.utils.FileTestNameUtil.*;
import static de.gdevelop.library.app.common.utils.IntegrationTestUtils.*;
import static de.gdevelop.library.app.common.utils.JsonTestUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.common.json.JsonReader;
import de.gdevelop.library.app.common.model.HttpCode;
import de.gdevelop.library.app.common.utils.IntegrationTestUtils;
import de.gdevelop.library.app.common.utils.ResourceClient;
import de.gdevelop.library.app.common.utils.ResourceDefinitions;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RunWith(Arquillian.class)
public class CategoryResourceIT {

	@ArquillianResource
	private URL url;

	private ResourceClient resourceClient;

	private static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class)
				.addPackages(true, "de.gdevelop.library.app")
				.addAsResource("persistence-integration.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.setWebXML(new File("src/test/resources/web.xml"))
				.addAsLibraries(
						Maven.resolver().resolve(
								"com.google.code.gson:gson:2.8.0",
								"org.mockito:mockito-core:2.7.22")
								.withTransitivity()
								.asFile());
	}

	@Before
	public void setUp() {
		this.resourceClient = new ResourceClient(url);
		resourceClient.resourcePath("/DB").delete();
	}

	@Test
	@RunAsClient
	public void addValidCategoryAndFindIt() {

		final Long id = addElementFromFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, "category.json");

		final String json = findById(resourceClient, PATH_RESOURCE, id);
		final JsonObject categoryAsJson = JsonReader.readAsJsonObject(json);
		assertThat(JsonReader.getString(categoryAsJson, "name"), is(equalTo(java().getName())));
	}

	@Test
	@RunAsClient
	public void addCategoryWithNullName() {
		final Response response = resourceClient.resourcePath(PATH_RESOURCE)
				.postFile(getRequestFilePath(PATH_RESOURCE, "categoryWithNullName.json"));

		assertThat(response.getStatus(), is(HttpCode.VALIDATION_ERROR.getCode()));
		assertJsonMatchesFileContent(response.readEntity(String.class),
				getResponseFilePath(PATH_RESOURCE, "categoryErrorNullName.json"));
	}

	@Test
	@RunAsClient
	public void addExistsCategory() {

		final Long id = addElementFromFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, "category.json");

		final Response response = resourceClient.resourcePath(PATH_RESOURCE)
				.postFile(getRequestFilePath(PATH_RESOURCE, "category.json"));

		assertThat(response.getStatus(), is(HttpCode.VALIDATION_ERROR.getCode()));
		assertJsonMatchesFileContent(response.readEntity(String.class),
				getResponseFilePath(PATH_RESOURCE, "categoryAlreadyExists.json"));
	}

	@Test
	@RunAsClient
	public void updateValidCategory() {

		final Long id = addElementFromFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, "category.json");

		final Response response = resourceClient
				.resourcePath(PATH_RESOURCE + "/" + id)
				.putFile(getRequestFilePath(PATH_RESOURCE, "categoryCleanCode.json"));
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

		findCategoryAndAssertResponseWithCategory(id, cleanCode());
	}

	@Test
	@RunAsClient
	public void updateCategoryWithNameBelongingToOtherCategory() {
		final Long javaCategoryId = addElementFromFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE,
				"category.json");
		final Long id = addElementFromFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE,
				"categoryCleanCode.json");

		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + javaCategoryId).putFile(
				getRequestFilePath(PATH_RESOURCE, "categoryCleanCode.json"));
		assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
	}

	@Test
	@RunAsClient
	public void updateCategoryNotFound() {
		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/999").putFile(
				getRequestFilePath(PATH_RESOURCE, "category.json"));
		assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
	}

	@Test
	@RunAsClient
	public void findCategoryNotFound() {
		final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/999").get();
		assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
	}

	@Test
	@RunAsClient
	public void findAllCategories() {
		resourceClient.resourcePath("DB/" + PATH_RESOURCE).postContent("");

		final Response response = resourceClient.resourcePath(PATH_RESOURCE).get();
		assertThat(response.getStatus(), is(HttpCode.OK.getCode()));
		assertThatResponseContainsCategories(response, 4, architecture(), cleanCode(), java(), networks());

	}

	private void assertThatResponseContainsCategories(final Response response, final int expectedTotalRecords,
			final Category... expectedCategories) {

		final JsonObject result = JsonReader.readAsJsonObject(response.readEntity(String.class));

		final int totalRecords = result.getAsJsonObject("paging").get("totalRecords").getAsInt();
		assertThat(totalRecords, is(equalTo(expectedTotalRecords)));

		final JsonArray categoriesList = result.getAsJsonArray("entries");
		assertThat(categoriesList.size(), is(equalTo(expectedCategories.length)));

		for (int i = 0; i < expectedCategories.length; i++) {
			final Category expectedCategory = expectedCategories[i];
			assertThat(categoriesList.get(i).getAsJsonObject().get("name").getAsString(),
					is(equalTo(expectedCategory.getName())));

		}
	}

	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesFileContent(response.readEntity(String.class), getResponseFilePath(PATH_RESOURCE, fileName));
	}

	private void findCategoryAndAssertResponseWithCategory(final Long categoryIdToBeFound,
			final Category expectedCategory) {
		final String json = IntegrationTestUtils.findById(resourceClient, PATH_RESOURCE, categoryIdToBeFound);

		final JsonObject categoryAsJson = JsonReader.readAsJsonObject(json);
		assertThat(JsonReader.getString(categoryAsJson, "name"), is(equalTo(expectedCategory.getName())));
	}
}
