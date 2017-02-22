package com.cisco.mediasense.utils;

import java.net.URL;
import java.net.URLDecoder;

public class WebInfGetter {

	private static final String WEBINF = "WEB-INF";

	public static String getWebInfPath() {

		String filePath = "";

		URL url = WebInfGetter.class.getResource("WebInfGetter.class");

		String className = url.getFile();

		filePath = className.substring(0,
				className.indexOf(WEBINF) + WEBINF.length());
		filePath += "/classes";
		try {
			filePath = URLDecoder.decode(filePath, "UTF-8");
		} catch (Exception e) {
			System.out.println("Error decoding filepath " + filePath);
		}
		return filePath;

	}

}
