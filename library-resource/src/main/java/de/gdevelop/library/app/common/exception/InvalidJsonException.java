package de.gdevelop.library.app.common.exception;

public class InvalidJsonException extends RuntimeException {

	private static final long serialVersionUID = 5467877427647611204L;

	public InvalidJsonException(final String message) {
		super(message);
	}

	public InvalidJsonException(final Throwable e) {
		super(e);
	}
}
