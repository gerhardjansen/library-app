package de.gdevelop.library.app.common.model;

public enum HttpCode {
	OK(200), CREATED(201), VALIDATION_ERROR(422), NOT_FOUND(404);

	private int code;

	private HttpCode(final int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
