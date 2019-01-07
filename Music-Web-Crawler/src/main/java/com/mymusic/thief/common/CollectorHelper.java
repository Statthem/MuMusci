package com.mymusic.thief.common;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mymusic.thief.entities.Artist;
import com.mymusic.thief.utils.JsoupUtils;

public class CollectorHelper {
	

	
	/**
	 * @param web page url
	 * @return Elements(same as List<Element>) containing all html from provided url 
	 */
	static protected Elements getAllElements(String url) {
		Elements allElements = null;
		Optional<Elements> allElementsOpt = JsoupUtils.getAllElements(url);
		if (allElementsOpt.isPresent())
			allElements = allElementsOpt.get();
		try {
			allElementsOpt.orElseThrow(Exception::new);
		} catch (Exception e) {
			System.err.println("Exception while retrieving allElments" + "\n" + e.getStackTrace()[1]);
		}

		return allElements;
	}
	
	
	static protected int getTotalPageAmount(Elements allElements) {
		Elements aElements = null;
		String nextPageQuery = "div.pager a:not(:contains(ВПЕРЕД))"; 
		Optional<Elements> aElementsOpt = JsoupUtils.findElementsByQuery(allElements, nextPageQuery);
		
		if (aElementsOpt.isPresent())
			aElements = aElementsOpt.get();
		try {
			aElementsOpt.orElseThrow(Exception::new);
		} catch (Exception e) {
			System.err.println("Exception while geting totalPageAmount" + "\n" + e.getStackTrace()[1]);
		}
		int totalPageAmount = 1;
		for (Element e : aElements) {
			int page = Integer.valueOf(e.text());
			if (page > totalPageAmount) {
				totalPageAmount = page;
			}
		}
		return totalPageAmount;
	}
	
	
	/**
	 * 
	 * @param empty Elements(same as List<Element>) for populating with all data in table
	 * @return populated Elements
	 */
	static protected Elements collectFromTable(Elements allElements) {
		String tableQuery = ".table.table-striped.table-condensed a[href]";
		Elements aElements = JsoupUtils.findElementsByQuery(allElements, tableQuery).get();

		return aElements;
	}
	
	/**
	 * @param Elements with paging related data
	 * @return url of the next page
	 */
	static protected String getNextPageUrl(Elements allElements) {
		String nextPageQuery = "div.pager a:contains(ВПЕРЕД)";
		Elements aElements = JsoupUtils.findElementsByQuery(allElements, nextPageQuery).get();
		String nextPageUrl = aElements.get(0).attr("href");

		return nextPageUrl;
	}


}
