package com.mymusic.thief.common;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mymusic.thief.entities.Artist;
import com.mymusic.thief.utils.JsoupUtils;

public class BasicArtistCollector implements ArtistCollector {
	private final static String DOMAIN_NAME = "myzuka.club";

	@Override
	public void collectArtist(Artist artist) {

	}

	@Override
	public void collectArtist(List<Artist> artistList) {

	}
	
	public Elements collectAll(Elements allElements, int totalPageAmount, Elements allArtists) {
		allArtists = CollectorHelper.collectFromTable(allElements);

		for (int i = 1; i < totalPageAmount; i++) {
			String nextPageUrl = "http://" + DOMAIN_NAME + CollectorHelper.getNextPageUrl(allElements);
			allElements = CollectorHelper.getAllElements(nextPageUrl);
			List<Element> tempList = CollectorHelper.collectFromTable(allElements);

			for (Element e : tempList) {
				allArtists.add(e);
			}
		}
		return allArtists;
	}
	
	
	private void collectArtistData(Elements allArtistElements, Artist artist) {
      
		String artistNameCssQuery = "div.main div.content div.inner h1";
		String otherNamesCssQuery = "div.cont tbody :nth-child(2) :nth-child(2)";
		String countryCssQuery = "div.cont tbody :nth-child(3) :nth-child(2)";
		String descriptionId = "inner_desc";
		String artistPhotoCssQuery = "div.vis img.img-thumbnail";

		Elements artistNameEl = JsoupUtils.findElementsByQuery(allArtistElements , artistNameCssQuery).get();
		Elements artistOtherNameEl = JsoupUtils.findElementsByQuery(allArtistElements , otherNamesCssQuery).get();
		Elements artistCountryEl = JsoupUtils.findElementsByQuery(allArtistElements , countryCssQuery).get();
		Elements desctiptionEl = JsoupUtils.findElementById(allArtistElements, descriptionId).get();
		Elements artistPhotoUrlEl = JsoupUtils.findElementsByQuery(allArtistElements , artistPhotoCssQuery).get();

		artist.setArtistName(artistNameEl.text());
		artist.setOtherNames(artistOtherNameEl.text());
		artist.setCountry(artistCountryEl.text());
		artist.setDescription(desctiptionEl.text());
		artist.setArtistPhotoUrl(artistPhotoUrlEl.attr("src"));
	}

}
