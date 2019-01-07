package com.mymusic.thief.common;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.select.Elements;

import com.mymusic.thief.entities.Album;
import com.mymusic.thief.entities.Artist;
import com.mymusic.thief.entities.Song;
import com.mymusic.thief.entities.SoundTrack;
import com.mymusic.thief.utils.DateUtils;
import com.mymusic.thief.utils.JsoupUtils;

public class AlbumCollector {
	
	SongCollector songCollector = new SongCollector();
	
	protected List<Album> getAllAlbums(String url, Artist incompleteArtist) {

		String studioAlbumsCssQuery = "div.album-nav a:contains(Студийные альбомы)";
		String extendedPlayCssQuery = "div.album-nav a:contains(EP)";
		String splitsCssQuery = "div.album-nav a:contains(Сплиты)";
		String singlesCssQuery = "div.album-nav a:contains(Синглы)";
		String soundTracksCssQuery = "div.album-nav a:contains(Саундтреки)";
		String liveCssQuery = "div.album-nav a:contains(Live выступления)";

		Elements allArtistAlbumElements = JsoupUtils.getAllElements(url).get();
		Elements studiAlbumsElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, studioAlbumsCssQuery).get();
		Elements extendedPlayElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, extendedPlayCssQuery).get();
		Elements splitsElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, splitsCssQuery).get();
		Elements singlesElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, singlesCssQuery).get();
		Elements soundTracksElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, soundTracksCssQuery).get();
		Elements liveElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, liveCssQuery).get();

		studioAlbumsCssQuery = "div.album-list div[data-type=" + getAlbumDataType(studiAlbumsElements) + "]";
		extendedPlayCssQuery = "div.album-list div[data-type=" + getAlbumDataType(extendedPlayElements) + "]";
		splitsCssQuery = "div.album-list div[data-type=" + getAlbumDataType(splitsElements) + "]";
		singlesCssQuery = "div.album-list div[data-type=" + getAlbumDataType(singlesElements) + "]";
		soundTracksCssQuery = "div.album-list div[data-type=" + getAlbumDataType(soundTracksElements) + "]";
		liveCssQuery = "div.album-list div[data-type=" + getAlbumDataType(liveElements) + "]";
		
		studiAlbumsElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, studioAlbumsCssQuery).get();
		extendedPlayElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, extendedPlayCssQuery).get();
		splitsElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, splitsCssQuery).get();
		singlesElements = JsoupUtils.findElementsByQuery(allArtistAlbumElements, singlesCssQuery).get();
		soundTracksElements =  JsoupUtils.findElementsByQuery(allArtistAlbumElements, soundTracksCssQuery).get();
		liveElements =  JsoupUtils.findElementsByQuery(allArtistAlbumElements, liveCssQuery).get();

		return collectAlbumData(incompleteArtist,studiAlbumsElements, extendedPlayElements, splitsElements, singlesElements, soundTracksElements, liveElements);

	}
	
	private String getAlbumDataType(Elements elements) {
		return elements.size() == 1 ? elements.get(0).attr("data-type") : "0";
	}
	
	private List<SoundTrack> divideAlbumsByType(List<Album> allAlbums){
		List<SoundTrack> soundTracks = new ArrayList<>();
		ListIterator<Album> listIterator = allAlbums.listIterator();
		
		while(listIterator.hasNext()) {
			Album album = listIterator.next();
			if(album.getAlbumType().equals("Саундтрек")) {
			SoundTrack soundTrack = new SoundTrack(
					album.getAlbumTitle(),
					album.getAlbumPhotoUrl(),
					album.getAlbumGenres(),
					album.getUploadDate(),
					album.getReleaseYear(),
					album.getAlbumUrl(),
					album.getAlbumType(),
					album.getSongsAmount(),
					album.getSongs());
			
			soundTracks.add(soundTrack);
			listIterator.remove();
			}
		}
		return soundTracks;
	}
	
	/**
	 * Author - Statthem
	 * 
	 * @param Html elements containing album data
	 * @return list of all albums
	 */
	protected List<Album> collectAlbumData(Artist artist, Elements... albumsEl) {

		List<Album> allAlbums = new ArrayList<>(albumsEl.length);
		
		for (Elements albumEl : albumsEl) {

			albumEl.forEach((al) -> {
				String albumTitleCssQuery = "div.title a";
				String albumGenresCssQuery = "div.info :nth-child(2)";
				String albumReleaseDateCssQuery = "div.info :nth-child(3) a";
				String albumUploadDateCssQuery = "div.overlay ul :nth-child(2)";
				String albumPhotoUrlCssQuery = "div.vis img";
				
				Elements albumTitleEl = JsoupUtils.findElementsByQuery(al, albumTitleCssQuery).get();
				Elements albumGenresEl = JsoupUtils.findElementsByQuery(al, albumGenresCssQuery).get();
				Elements albumReleaseDateEl = JsoupUtils.findElementsByQuery(al, albumReleaseDateCssQuery).get();
				Elements albumUploadDateEl = JsoupUtils.findElementsByQuery(al, albumUploadDateCssQuery).get();
				Elements albumPhotoUrlEl = JsoupUtils.findElementsByQuery(al, albumPhotoUrlCssQuery).get();
				
				Album album = new Album();
				album.setArtist(artist);
				album.setAlbumTitle(albumTitleEl.text());
				album.setAlbumGenres(albumGenresEl.text());
				album.setAlbumPhotoUrl(albumPhotoUrlEl.attr("src"));
				album.setAlbumUrl(albumTitleEl.attr("href"));
				album.setReleaseYear(albumReleaseDateEl.text());

				String unmodifiedUploadDate = albumUploadDateEl.text();
				Date sqlDate = DateUtils.convertToSqlDate(unmodifiedUploadDate.substring(unmodifiedUploadDate.indexOf(':') + 1,
						unmodifiedUploadDate.length()));
				album.setUploadDate(sqlDate);

				List<Song> albumSongs = new ArrayList<>();
				songCollector.collectAlbumSongsData(album.getAlbumUrl(), albumSongs, album);
				album.setSongs(albumSongs);
				album.setSongsAmount(albumSongs.size());

				allAlbums.add(album);
			});
		}
		return allAlbums;
	}

}
