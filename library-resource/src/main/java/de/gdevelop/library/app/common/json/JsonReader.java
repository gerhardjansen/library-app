package de.gdevelop.library.app.common.json;

import de.gdevelop.library.app.common.exception.InvalidJsonException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonReader {

	public static JsonObject readAsJsonObject(final String json) {
		return readJsonAs(json, JsonObject.class);
	}

	public static JsonArray readAsJsonArray(final String json) {
		return readJsonAs(json, JsonArray.class);
	}

	private static <T> T readJsonAs(final String json, final Class<T> jsonClass) {
		if (json == null || json.trim().isEmpty()) {
			throw new InvalidJsonException("Json String must not be null or empty!");
		}
		try {
			return new Gson().fromJson(json, jsonClass);
		} catch (final Exception e) {
			throw new InvalidJsonException(e);
		}
	}

	public static Long getLong(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsLong();
	}

	public static Integer getInteger(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsInt();
	}

	public static String getString(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsString();
	}

	public static Double getDouble(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsDouble();
	}

	private static boolean isJsonElementNull(final JsonElement element) {
		return element == null || element.isJsonNull();
	}
}
