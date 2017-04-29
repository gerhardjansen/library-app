package de.gdevelop.library.app.common.model;

import de.gdevelop.library.app.common.exception.FieldNotValidException;

public class StandardsOperationResults {

	private StandardsOperationResults() {

	}

	public static OperationResult getOperationResultAlreadyExists(final ResouceMessage resourceMessage,
			final String fieldName) {
		return OperationResult.error(resourceMessage.getKeyAlreadyExists(),
				resourceMessage.getMessageAlreadyExists(fieldName));
	}

	public static OperationResult getOperationResultInvalidField(final ResouceMessage resourceMessage,
			final FieldNotValidException ex) {
		return OperationResult.error(resourceMessage.getKeyInvalidField(ex.getFieldName()),
				ex.getMessage());
	}

	public static OperationResult getOperationResultNotFound(final ResouceMessage resourceMessage) {
		return OperationResult.error(resourceMessage.getKeyNotFound(),
				resourceMessage.getMessageNotFound());
	}

}
