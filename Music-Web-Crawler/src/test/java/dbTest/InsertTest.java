package dbTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.junit.Test;

import enities.Album;
import enities.Artist;
import enities.Song;
import enities.SoundTrack;
import utils.HibernateUtils;

public class InsertTest {

	//@Test
	public void basicInsert_test() {
		Session session = HibernateUtils.getSession();
		Artist artist = new Artist();
		artist.setArtistName("TestArtist");

		List<Album> albums = new ArrayList<>();
		List<Song> songs = new ArrayList<>();
		List<SoundTrack> soundtracks = new ArrayList<>();
		List<Artist> artists = new ArrayList<>();
		artists.add(artist);

		Album album1 = new Album();
		Album album2 = new Album();

		SoundTrack album3 = new SoundTrack();

		album3.setAlbumTitle("testSoundtrack");
		album3.setArtist(artist);

		album3.setArtistList(artists);

		album2.setArtist(artist);
		album1.setAlbumTitle("TestAlbum1");
		album2.setAlbumTitle("TestAlbum2");
		album1.setSongs(songs);
		albums.add(album1);
		albums.add(album2);
		soundtracks.add(album3);

		Song song1 = new Song();
		song1.setTitle("TestSong1");
		song1.setAlbum(album1);
		Song song2 = new Song();
		song2.setTitle("TestSong2");
		song2.setAlbum(album1);
		songs.add(song1);
		songs.add(song2);
		Song song3 = new Song();
		song3.setTitle("TestSong3");

		artist.setAlbums(albums);

		artist.setSoundtracks(soundtracks);

		session.save(artist);
		session.close();
		
		
	}

	//@Test
	public void SoundtrackInsert_test() {
		Artist artist = new Artist();
		artist.setArtistName("TestArtist");

		List<SoundTrack> soundtracks = new ArrayList<>();

		SoundTrack soundTrack = new SoundTrack();
		soundTrack.setAlbumTitle("test_soundtrack");
		soundtracks.add(soundTrack);
		artist.setSoundtracks(soundtracks);
		
		Session session = HibernateUtils.getSession();
		session.beginTransaction();
		
		session.save(artist);
		
		
		session.getTransaction().commit();
		session.close();
	}
	
	@Test
	public void AlbumAndSoundtrackInsert_test() {
		Artist artist = new Artist();
		artist.setArtistName("TestArtist");

		List<Album> albums = new ArrayList<>();
		List<SoundTrack> soundtracks = new ArrayList<>();

		Album album = new Album();
		album.setAlbumTitle("test_album");
		album.setArtist(artist);
		SoundTrack soundTrack = new SoundTrack();
		soundTrack.setAlbumTitle("test_soundtrack");
		albums.add(album);
		soundtracks.add(soundTrack);
		artist.setAlbums(albums);
		artist.setSoundtracks(soundtracks);
		
		Session session = HibernateUtils.getSession();
		session.beginTransaction();
		
		session.save(artist);
		
		
		session.getTransaction().commit();
		session.close();
	}
	
	
	
}
