package de.gdevelop.library.app.common.utils;

import org.junit.Ignore;

@Ignore
public enum ResourceDefinitions {
	CATEGORY("categories");

	private String resourceName;

	private ResourceDefinitions(final String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}
}
