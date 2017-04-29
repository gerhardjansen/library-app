package de.gdevelop.library.app.category.resource;

import static de.gdevelop.library.app.common.model.StandardsOperationResults.*;

import java.util.List;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.category.services.CategoryServices;
import de.gdevelop.library.app.common.exception.CategoryAlreadyExistsException;
import de.gdevelop.library.app.common.exception.CategoryNotFoundException;
import de.gdevelop.library.app.common.exception.FieldNotValidException;
import de.gdevelop.library.app.common.json.JsonUtil;
import de.gdevelop.library.app.common.json.OperationResultWriter;
import de.gdevelop.library.app.common.model.HttpCode;
import de.gdevelop.library.app.common.model.OperationResult;
import de.gdevelop.library.app.common.model.ResouceMessage;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

	private static final String RESOURCE_TYPE = "category";

	private Logger log = LoggerFactory.getLogger(getClass());
	private static ResouceMessage RESOURCE_MESSAGE = new ResouceMessage(RESOURCE_TYPE);

	@Inject
	CategoryServices categoryServices;
	@Inject
	CategoryJsonConverter jsonConverter;

	@POST
	public Response add(final String body) {
		log.info("Adding a new category with body {}: " + body);
		Category category = jsonConverter.fromJson(body);

		HttpCode httpCode = HttpCode.CREATED;
		OperationResult result = null;

		try {
			category = categoryServices.add(category);
			result = OperationResult.success(JsonUtil.getJsonElementWithId(category.getId()));
		} catch (final FieldNotValidException ex) {
			log.error("Validation Error while adding Category: " + category);
			httpCode = HttpCode.VALIDATION_ERROR;
			result = getOperationResultInvalidField(RESOURCE_MESSAGE, ex);
		} catch (final CategoryAlreadyExistsException e) {
			log.error("Category already exists: " + category);
			httpCode = HttpCode.VALIDATION_ERROR;
			result = getOperationResultAlreadyExists(RESOURCE_MESSAGE, "name");
		}

		log.info("Return operation result after adding category {}: ", OperationResultWriter.toJson(result));

		return Response.status(httpCode.getCode()).entity(OperationResultWriter.toJson(result)).build();
	}

	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") final long id, final String body) {
		log.debug("Updating a new category with body {}: " + body);

		final Category category = jsonConverter.fromJson(body);

		HttpCode httpCode = HttpCode.OK;
		OperationResult result = null;

		category.setId(id);

		try {
			categoryServices.update(category);
			result = OperationResult.success();
		} catch (final FieldNotValidException ex) {
			log.error("Validation Error while adding Category: " + category);
			httpCode = HttpCode.VALIDATION_ERROR;
			result = getOperationResultInvalidField(RESOURCE_MESSAGE, ex);
		} catch (final CategoryAlreadyExistsException e) {
			log.error("Category already exists: " + category);
			httpCode = HttpCode.VALIDATION_ERROR;
			result = getOperationResultAlreadyExists(RESOURCE_MESSAGE, "name");
		} catch (final CategoryNotFoundException e) {
			log.error("Category already exists: " + category);
			httpCode = HttpCode.NOT_FOUND;
			result = getOperationResultNotFound(RESOURCE_MESSAGE);
		}

		log.info("Return operation result after updating category {}: ", OperationResultWriter.toJson(result));

		return Response.status(httpCode.getCode()).entity(OperationResultWriter.toJson(result)).build();
	}

	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") final long id) {
		log.debug("Find category with id: " + id);

		HttpCode httpCode = HttpCode.OK;
		OperationResult result = null;

		try {
			final Category category = categoryServices.findById(id);
			result = OperationResult.success(jsonConverter.toJson(category));
			log.debug("Category found: ", category);
		} catch (final CategoryNotFoundException e) {
			log.error("Category not found by id: " + id);
			httpCode = HttpCode.NOT_FOUND;
			result = getOperationResultNotFound(RESOURCE_MESSAGE);
		}

		log.info("Return operation result after looking up category {}: ", OperationResultWriter.toJson(result));
		return Response.status(httpCode.getCode()).entity(OperationResultWriter.toJson(result)).build();
	}

	@GET
	@Path("/byname/{name}")
	public Response findByName(@PathParam("name") final String name) {
		log.debug("Find category with name: " + name);

		HttpCode httpCode = HttpCode.OK;
		OperationResult result = null;

		try {
			final Category category = categoryServices.findByName(name);
			result = OperationResult.success(jsonConverter.toJson(category));
			log.debug("Category found: ", category);
		} catch (final CategoryNotFoundException e) {
			log.error("Category not found by name: " + name);
			httpCode = HttpCode.NOT_FOUND;
			result = getOperationResultNotFound(RESOURCE_MESSAGE);
		}

		log.info("Return operation result after looking up category {}: ", OperationResultWriter.toJson(result));
		return Response.status(httpCode.getCode()).entity(OperationResultWriter.toJson(result)).build();
	}

	void setCategoryServices(final CategoryServices categoryServices) {
		this.categoryServices = categoryServices;
	}

	void setCategoryJsonConverter(final CategoryJsonConverter jsonConverter) {
		this.jsonConverter = jsonConverter;

	}

	@GET
	public Response findAll() {
		log.debug("Find all categories");

		final List<Category> categories = categoryServices.findAll();
		final OperationResult result = OperationResult.success(getJsonArrayWithPaging(categories));
		log.debug("Categories found: ", categories);

		log.info("Return operation result after reading categories: ", OperationResultWriter.toJson(result));
		return Response.status(HttpCode.OK.getCode()).entity(OperationResultWriter.toJson(result)).build();
	}

	private JsonElement getJsonArrayWithPaging(final List<Category> categories) {
		final JsonObject jsonObject = new JsonObject();

		final JsonObject paging = new JsonObject();

		paging.addProperty("totalRecords", categories.size());

		jsonObject.add("paging", paging);
		jsonObject.add("entries", jsonConverter.toJson(categories));

		return jsonObject;

	}

}
