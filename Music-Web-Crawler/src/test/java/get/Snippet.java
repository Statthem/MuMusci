package get;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import  com.mymusic.thief.entities.Album ; 
import com.mymusic.thief.entities.Artist;
import com.mymusic.thief.entities.Song;
import com.mymusic.thief.entities.SoundTrack;
import com.mymusic.thief.utils.DateUtils;
import com.mymusic.thief.utils.HibernateUtils;
import com.mymusic.thief.utils.JsoupUtils;

public class Snippet {

	private final static String DOMAIN_NAME = "myzuka.club";
	private final static int AMOUNT_TESTED = 1;

	
//	@Before 
	public void setProxy() {
		System.setProperty("http.proxyHost", "62.213.87.172"); // set proxy server
		System.setProperty("http.proxyPort", "8080"); // set proxy port
		
		System.setProperty("hsttp.nonProxyHosts", "localhost|127.0.0.1");
		// HTTPS
		System.setProperty("https.proxyHost", "62.213.87.172");
		System.setProperty("https.proxyPort", "8080");
	    }
	
	@Test
	public void testRunner() throws IOException {
		String baseURL = "http://" + DOMAIN_NAME + "/Artist";
		initialConnect(baseURL);
	}

	public void initialConnect(String baseURL) throws IOException {
		Elements allElements = getAllElements(baseURL);
		int totalPageAmount = getTotalPageAmount(allElements);

		Elements allArtists = new Elements(totalPageAmount * 100);
		allArtists = collectAll(allElements, totalPageAmount, allArtists);
		List<Artist> allCollectedArtists = new ArrayList<>(1000);
		/*
		for (Element element : allArtists) {
			String nextUrl = "http://" + DOMAIN_NAME + element.attr("href");
			Artist artist = new Artist();
			collectArtist_test(nextUrl, artist);
			allCollectedArtists.add(artist);
		}
		
		System.out.println(allCollectedArtists.size());
		 */
		
		String nextUrl = "http://" + DOMAIN_NAME + allArtists.get(10).attr("href");
		Artist artist = new Artist();
		collectArtist_test(nextUrl, artist);
		System.out.println(artist.toString());
		//artist.getAlbums().forEach(album -> System.out.println("albom " + album.getAlbumTitle() + "(" + album.getAlbumType() + ")"  + " has " + album.getSongsAmount() + " songs"));
	//	HibernateUtils.addArtist(artist);

	//	threadStart(allArtists.size()/10,allArtists);
	 
	
	}

	public void collectArtist_test(String url, Artist emptyArtist) {
		Elements allArtistElements = JsoupUtils.getAllElements(url).get();
		collectArtistData(allArtistElements, emptyArtist);
		
		List<Album> allAlbums = getAllAlbums(url + "/Albums", emptyArtist);
		List<SoundTrack> allSoundTracks = divideByType(allAlbums);
		emptyArtist.setAlbums(allAlbums);
		emptyArtist.setSoundtracks(allSoundTracks);
	}
	
	
	public List<SoundTrack> divideByType(List<Album> allAlbums){
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
	 * 
	 * @param url of specific Artist on M***ka.club
	 * @param incomplete artist Object
	 */
	public void collectArtistData(Elements allArtistElements, Artist artist) {

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

	public void threadStart(int nThreads, List<Element> list) {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		List<Runnable> runnableeList = new ArrayList<>(nThreads);
		List<Artist> allCollectedArtists = new ArrayList<>(nThreads);

		for (Element element : list) {
			Runnable runnableTask = () -> {
				System.out.println("Thread " + Thread.currentThread().getId() + " started");
				String nextUrl = "http://" + DOMAIN_NAME + element.attr("href");
				Artist artist = new Artist();
				collectArtist_test(nextUrl, artist);
				allCollectedArtists.add(artist);
				HibernateUtils.addArtist(artist);
				System.out.println(allCollectedArtists.size());
				System.out.println("Thread " + Thread.currentThread().getId() + " ended");
			};

			runnableeList.add(runnableTask);
		}
		runnableeList.forEach(task -> executorService.submit(task));

		try {
			executorService.awaitTermination(1000, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public List<Album> getAllAlbums(String url, Artist incompleteArtist) {

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

	

	/**
	 * Author - Statthem
	 * 
	 * @param Html elements containing album data
	 * @return list of all albums
	 */
	public List<Album> collectAlbumData(Artist artist, Elements... albumsEl) {

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
				collectAlbumSongsData(album.getAlbumUrl(), albumSongs, album);
				album.setSongs(albumSongs);
				album.setSongsAmount(albumSongs.size());

				allAlbums.add(album);
			});
		}
		return allAlbums;
	}

	public void collectAlbumSongsData(String albumUrl, List<Song> albumSongs, Album album) {
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

	public String getAlbumDataType(Elements elements) {
		return elements.size() == 1 ? elements.get(0).attr("data-type") : "0";
	}

	public Elements collectAll(Elements allElements, int totalPageAmount, Elements allArtists) {
		allArtists = collectFromTable(allElements);

		for (int i = 1; i < totalPageAmount; i++) {
			String nextPageUrl = "http://" + DOMAIN_NAME + getNextPageUrl(allElements);
			allElements = getAllElements(nextPageUrl);
			List<Element> tempList = collectFromTable(allElements);

			for (Element e : tempList) {
				allArtists.add(e);
			}
		}

		return allArtists;
	}

	
	public int getTotalPageAmount(Elements allElements) {
		Elements aElements = null;
		String nextPageQuery = "div.pager a:not(:contains(ВПЕРЕД))"; /**
																		 * all 'a' elements that not contain text
																		 * "ВПЕРЕД"
																		 */
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

	public Elements collectFromTable(Elements allElements) {
		String tableQuery = ".table.table-striped.table-condensed a[href]";
		Elements aElements = JsoupUtils.findElementsByQuery(allElements, tableQuery).get();

		return aElements;
	}

	public String getNextPageUrl(Elements allElements) {
		String nextPageQuery = "div.pager a:contains(ВПЕРЕД)";
		Elements aElements = JsoupUtils.findElementsByQuery(allElements, nextPageQuery).get();
		String nextPageUrl = aElements.get(0).attr("href");

		return nextPageUrl;
	}

	public Elements getAllElements(String url) {
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

}
