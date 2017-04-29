package de.gdevelop.library.app.common.model;

public class OperationResult {

	public static final String ERROR_IDENTIFICATION = "errorIdentification";
	public static final String ERROR_DESCRITPION = "errorDescription";

	private boolean success;
	private String errorIdentification;
	private String errorDescription;
	private Object entity;

	public OperationResult(final String errorIdentification, final String errorDescription) {
		this.success = false;
		this.errorIdentification = errorIdentification;
		this.errorDescription = errorDescription;
	}

	public OperationResult(final Object entity) {
		this.success = true;
		this.entity = entity;
	}

	public static OperationResult success(final Object entity) {
		return new OperationResult(entity);
	}

	public static OperationResult success() {
		return new OperationResult(null);
	}

	public static OperationResult error(final String errorIdentification, final String errorDescription) {
		return new OperationResult(errorIdentification, errorDescription);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

	public String getErrorIdentification() {
		return errorIdentification;
	}

	public void setErrorIdentification(final String errorIdentification) {
		this.errorIdentification = errorIdentification;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(final String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(final Object entity) {
		this.entity = entity;
	}

}
