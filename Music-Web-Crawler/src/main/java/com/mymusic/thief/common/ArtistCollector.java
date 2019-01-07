package com.mymusic.thief.common;

import java.util.List;

import com.mymusic.thief.entities.Artist;

public interface ArtistCollector {
	
	void collectArtist(Artist artist);

	void collectArtist(List<Artist> artistList);
	

}
