package de.gdevelop.library.app.common.utils;

import static de.gdevelop.library.app.common.utils.FileTestNameUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import de.gdevelop.library.app.common.model.HttpCode;

import javax.ws.rs.core.Response;

import org.junit.Ignore;

@Ignore
public final class IntegrationTestUtils {

	private IntegrationTestUtils() {
	}

	public static Long addElementFromFileAndGetId(final ResourceClient resourceClient, final String pathResource,
			final String mainFolder, final String fileName) {
		final Response response = resourceClient
				.resourcePath(pathResource)
				.postFile(getRequestFilePath(mainFolder, fileName));

		return assertThatResponseIsCreatedAndGetId(response);
	}

	public static String findById(final ResourceClient resourceClient, final String pathResource, final Long id) {

		final Response responseGet = resourceClient.resourcePath(pathResource + "/" + id).get();
		assertThat(responseGet.getStatus(), is(HttpCode.OK.getCode()));

		return responseGet.readEntity(String.class);
	}

	public static String findByName(final ResourceClient resourceClient, final String pathResource, final String name) {

		final Response responseGet = resourceClient.resourcePath(pathResource + "/name/" + name).get();
		assertThat(responseGet.getStatus(), is(HttpCode.OK.getCode()));

		return responseGet.readEntity(String.class);
	}

	private static Long assertThatResponseIsCreatedAndGetId(final Response response) {
		assertThat(response.getStatus(), is(HttpCode.CREATED.getCode()));
		final Long id = JsonTestUtil.getIdFromJson(response.readEntity(String.class));
		assertThat(id, is(notNullValue()));

		return id;
	}
}
