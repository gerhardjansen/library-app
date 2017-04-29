package de.gdevelop.library.app.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class JsonUtil {

	private JsonUtil() {

	}

	public static JsonElement getJsonElementWithId(final Long id) {
		final JsonObject idJson = new JsonObject();
		idJson.addProperty("id", id);

		return idJson;
	}
}
