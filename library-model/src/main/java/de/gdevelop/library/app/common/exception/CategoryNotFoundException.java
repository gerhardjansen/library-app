package de.gdevelop.library.app.common.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class CategoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 755407949940733447L;

	public CategoryNotFoundException() {
	}

	public CategoryNotFoundException(final String message) {
		super(message);
	}

}
