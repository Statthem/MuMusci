package com.mymusic.thief.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Artist {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="name")
	private String artistName;
	
	@Column(name="other_name")
	private String otherNames;
	
	@Column(name="country")
	private String country;
	
	@Column(name="description")
	private String description;
	
	@Column(name="photo_url")
	private String artistPhotoUrl;
	
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "artist")
	private List<Album> albums;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(
	        name = "artist_soundtrack", 
	        joinColumns = { @JoinColumn(name = "artist_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "soundtrack_id") }
	    )
	private List<SoundTrack> soundtracks;
	
	public Artist() {
		super();
	}

	public Artist(String artistName, String otherNames, String country, String description, String artistPhotoUrl) {
		super();
		this.artistName = artistName;
		this.otherNames = otherNames;
		this.country = country;
		this.description = description;
		this.artistPhotoUrl = artistPhotoUrl;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArtistPhotoUrl() {
		return artistPhotoUrl;
	}

	public void setArtistPhotoUrl(String artistPhotoUrl) {
		this.artistPhotoUrl = artistPhotoUrl;
	}
	

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public List<SoundTrack> getSoundtracks() {
		return soundtracks;
	}

	public void setSoundtracks(List<SoundTrack> soundtracks) {
		this.soundtracks = soundtracks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "Artist [artistName=" + artistName + ", otherNames=" + otherNames + ", country=" + country
				+ ", description=" + description + ", artistPhotoUrl=" + artistPhotoUrl + "]";
	}
	
	
}
