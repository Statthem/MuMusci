package com.mymusic.thief.common;

import java.util.List;

import org.jsoup.select.Elements;

import com.mymusic.thief.entities.Album;
import com.mymusic.thief.entities.Song;
import com.mymusic.thief.utils.JsoupUtils;

public class SongCollector{
	
	private final static String DOMAIN_NAME = "myzuka.club";
	
	protected void collectAlbumSongsData(String albumUrl, List<Song> albumSongs, Album album) {
        String url = "http://" + DOMAIN_NAME + albumUrl;
		
		String allSongsDataCssQuery = "div.player-inline";
		String songTitleCssQuery = "div.details p a";
		String songSizeCssQuery = "div.details div.time";
		String songUrlCssQuery = "span.ico";
		String songLenghtQualityCssQuery = "div.data";
		String albumTypeQuery = "div.main-details div.tbl tbody :nth-child(5) :nth-child(2)";

		Elements allElements = JsoupUtils.getAllElements(url).get();
		Elements allSongsDataEl = JsoupUtils.findElementsByQuery(allElements, allSongsDataCssQuery).get();
		Elements songTitleDataEl = JsoupUtils.findElementsByQuery(allSongsDataEl, songTitleCssQuery).get();
		Elements songSizeDataEl = JsoupUtils.findElementsByQuery(allSongsDataEl, songSizeCssQuery).get();
		Elements songUrlDataEl = JsoupUtils.findElementsByQuery(allSongsDataEl, songUrlCssQuery).get();
		Elements songLenghtQualityEl = JsoupUtils.findElementsByQuery(allSongsDataEl, songLenghtQualityCssQuery).get();
		Elements albumTypeEl = JsoupUtils.findElementsByQuery(allElements, albumTypeQuery).get();

		for (int i = 0; i < songTitleDataEl.size(); i++) {
			Song song = new Song();
			song.setTitle(songTitleDataEl.get(i).text());
			song.setSize(songSizeDataEl.get(i).text());
			song.setUrl(songUrlDataEl.get(i).attr("data-url"));
			song.setLength(songLenghtQualityEl.get(i).text().substring(0, songLenghtQualityEl.text().indexOf('|')));
			song.setQuality(songLenghtQualityEl.get(i).text()
					.substring(songLenghtQualityEl.text().indexOf('|') + 1, songLenghtQualityEl.text().indexOf('К'))
					.trim());
			song.setAlbum(album);

			albumSongs.add(song);
		}
		album.setAlbumType(albumTypeEl.text());
	}
}
