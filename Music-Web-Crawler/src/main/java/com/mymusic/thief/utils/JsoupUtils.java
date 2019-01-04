package com.mymusic.thief.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtils {

	private static String CHARSET_NAME = "utf8";

	public static Optional<Elements> getAllElements(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			return Optional.of(doc.getAllElements());

		} catch (IOException e) {
			return Optional.empty();
		}
	}

	public static Optional<Element> findElementById(File htmlFile, String targetElementId) {
		try {
			Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());

			return Optional.of(doc.getElementById(targetElementId));

		} catch (IOException e) {
			return Optional.empty();
		}
	}

	public static Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery) {
		try {
			Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());

			return Optional.of(doc.select(cssQuery));

		} catch (IOException e) {
			return Optional.empty();
		}
	}

	public static Optional<Element> findElementById(String url, String targetElementId) {
		try {
			Document doc = Jsoup.connect(url).get();
			return Optional.of(doc.getElementById(targetElementId));

		} catch (IOException e) {
			return Optional.empty();
		}
	}

	public static Optional<Elements> findElementById(Elements elements, String targetElementId) {
		try {
			Optional<Elements> elementsOpt = Optional.of(elements.select("#" + targetElementId));
			elementsOpt.orElseThrow(IOException::new);
			return elementsOpt;
		} catch (IOException e) {
            return Optional.empty();
		}
	}

	public static Optional<Elements> findElementsByQuery(String url, String cssQuery) {
		try {
			Document doc = Jsoup.connect(url).get();
			return Optional.of(doc.select(cssQuery));

		} catch (IOException e) {
			return Optional.empty();
		}
	}

	public static Optional<Elements> findElementsByQuery(Elements elements, String cssQuery) {
		return Optional.of(elements.select(cssQuery));
	}

	public static Optional<Elements> findElementsByQuery(Element element, String cssQuery) {
		return Optional.of(element.select(cssQuery));
	}
}
