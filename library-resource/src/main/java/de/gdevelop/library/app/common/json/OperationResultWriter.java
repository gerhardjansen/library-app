package de.gdevelop.library.app.common.json;

import de.gdevelop.library.app.common.model.OperationResult;

import com.google.gson.JsonObject;

public class OperationResultWriter {

	private OperationResultWriter() {
	};

	public static String toJson(final OperationResult operationResult) {
		return JsonWriter.toJsonString(getJsonObject(operationResult));
	}

	private static Object getJsonObject(final OperationResult operationResult) {
		if (operationResult.isSuccess()) {
			return getJsonSucess(operationResult);
		}
		return getJsonError(operationResult);
	}

	private static Object getJsonSucess(final OperationResult operationResult) {
		return operationResult.getEntity();
	}

	private static Object getJsonError(final OperationResult operationResult) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty(OperationResult.ERROR_IDENTIFICATION, operationResult.getErrorIdentification());
		jsonObject.addProperty(OperationResult.ERROR_DESCRITPION, operationResult.getErrorDescription());

		return jsonObject;
	}

}
