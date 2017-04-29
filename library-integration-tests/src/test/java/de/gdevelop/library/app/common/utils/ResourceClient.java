package de.gdevelop.library.app.common.utils;

import static de.gdevelop.library.app.common.utils.JsonTestUtil.*;

import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResourceClient {
	private URL baseUrl;
	private String resourcePath;

	public ResourceClient(final URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	public ResourceClient resourcePath(final String resourcePath) {
		this.resourcePath = resourcePath;
		return this;
	}

	public Response postFile(final String filename) {
		return postContent(getRequestFromFileOrEmpty(filename));
	}

	public Response putContent(final String content) {
		return buildClient().put(Entity.entity(content, MediaType.APPLICATION_JSON));
	}

	public Response putFile(final String filename) {
		return putContent(getRequestFromFileOrEmpty(filename));
	}

	public Response postContent(final String content) {
		return buildClient().post(Entity.entity(content, MediaType.APPLICATION_JSON));
	}

	public Response get() {
		return buildClient().get();
	}

	public void delete() {
		buildClient().delete();
	}

	public Builder buildClient() {
		return ClientBuilder.newClient().target(getFullURL(resourcePath)).request();
	}

	private String getFullURL(final String resourcePath) {
		try {
			return this.baseUrl.toURI() + "api/" + resourcePath;
		} catch (final URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private String getRequestFromFileOrEmpty(final String filename) {
		if (filename == null) {
			return "";
		}
		return readJsonFile(filename);

	}

}
