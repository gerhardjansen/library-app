package de.gdevelop.library.app.common.model;

public class ResouceMessage {

	private String resource;

	public ResouceMessage(final String resource) {
		this.resource = resource;
	}

	private static final String KEY_ALREADY_EXISTS = "%s.alreadyExists";
	private static final String MESSAGE_ALREADY_EXISTS = "There is already a %s for the given %s.";
	private static final String KEY_INVALID_FIELD = "%s.invalidField.%s";
	private static final String KEY_NOT_FOUND = "%s.notFound";
	private static final String MESSAGE_NOT_FOUND = "%s not found.";

	public String getKeyAlreadyExists() {
		return String.format(KEY_ALREADY_EXISTS, resource);
	}

	public String getMessageAlreadyExists(final String fieldName) {
		return String.format(MESSAGE_ALREADY_EXISTS, resource, fieldName);
	}

	public String getKeyInvalidField(final String fieldName) {
		return String.format(KEY_INVALID_FIELD, resource, fieldName);
	}

	public String getKeyNotFound() {
		return String.format(KEY_NOT_FOUND, resource);
	}

	public String getMessageNotFound() {
		return String.format(MESSAGE_NOT_FOUND, resource);
	}

}
