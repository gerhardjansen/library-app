package de.gdevelop.library.app.common.utils;

import org.junit.Ignore;

@Ignore
public class FileTestNameUtil {

	private static final String PATH_REQUEST = "/request/";
	private static final String PATH_RESPONSE = "/response/";

	private FileTestNameUtil() {
	}

	public static String getRequestFilePath(final String mainFolder, final String fileName) {
		return mainFolder + PATH_REQUEST + fileName;
	}

	public static String getResponseFilePath(final String mainFolder, final String fileName) {
		return mainFolder + PATH_RESPONSE + fileName;
	}
}
