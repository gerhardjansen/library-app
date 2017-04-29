package de.gdevelop.library.app.common.utils;

import java.io.InputStream;
import java.util.Scanner;

import de.gdevelop.library.app.common.json.JsonReader;

import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonObject;

@Ignore
public class JsonTestUtil {

	public static final String BASE_JSON_DIR = "json/";

	private JsonTestUtil() {
	}

	public static String readJsonFile(final String relativePath) {
		final InputStream is = JsonTestUtil.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);
		try (Scanner s = new Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

	public static void assertJsonMatchesFileContent(final String actual, final String filenNameExpected) {
		final String expected = readJsonFile(filenNameExpected);
		assertJsonMatchesExpected(actual, expected);
	}

	public static void assertJsonMatchesExpected(final String actual, final String expected) {
		try {
			JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
		} catch (final Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Long getIdFromJson(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
		return JsonReader.getLong(jsonObject, "id");
	}

}
