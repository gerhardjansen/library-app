package de.gdevelop.library.app.category.resource;

import java.util.List;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.common.json.JsonReader;

import javax.enterprise.context.ApplicationScoped;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@ApplicationScoped
public class CategoryJsonConverter {

	public Category fromJson(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

		final Category category = new Category();
		category.setName(JsonReader.getString(jsonObject, "name"));

		return category;
	}

	public JsonElement toJson(final Category category) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("id", category.getId());
		jsonObject.addProperty("name", category.getName());

		return jsonObject;
	}

	public JsonElement toJson(final List<Category> categories) {
		final JsonArray jsonArray = new JsonArray();

		for (final Category category : categories) {
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", category.getId());
			jsonObject.addProperty("name", category.getName());
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}

}
