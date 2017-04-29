package de.gdevelop.library.app.common.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class CategoryAlreadyExistsException extends RuntimeException {

	public CategoryAlreadyExistsException() {
	}

	public CategoryAlreadyExistsException(final String message) {
		super(message);
	}

	private static final long serialVersionUID = 1025718762510511673L;

}
