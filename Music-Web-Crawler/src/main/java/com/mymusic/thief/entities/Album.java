package com.mymusic.thief.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "album")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Album {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false, unique=true)
	private int id;
	
	
	@ManyToOne()
	private Artist artist;
	
	@Column(name="title")
	private String albumTitle;
	
	@Column(name="photo_url")
	private String albumPhotoUrl;
	
	@Column(name="genres")
	private String albumGenres;
	
	@Column(name="upload_date")
	private Date uploadDate;
	
	@Column(name="release_year")
	private String releaseYear;
	
	@Column(name="url")
	private String albumUrl;
	
	@Column(name="type")
	private String albumType;
	
	@Column(name="songs_total")
	private Integer songsAmount;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "album")
	private List<Song> songs;
	
	public Album() {
		super();
	}

	public Album(String albumTitle, String albumPhotoUrl, String albumGenres, Date uploadDate, String releaseYear,
			String albumUrl, String albumType, Integer songsAmount, List<Song> songs) {
		super();
		this.albumTitle = albumTitle;
		this.albumPhotoUrl = albumPhotoUrl;
		this.albumGenres = albumGenres;
		this.uploadDate = uploadDate;
		this.releaseYear = releaseYear;
		this.albumUrl = albumUrl;
		this.albumType = albumType;
		this.songsAmount = songsAmount;
		this.songs = songs;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public String getAlbumPhotoUrl() {
		return albumPhotoUrl;
	}

	public void setAlbumPhotoUrl(String albumPhotoUrl) {
		this.albumPhotoUrl = albumPhotoUrl;
	}

	public String getAlbumGenres() {
		return albumGenres;
	}

	public void setAlbumGenres(String albumGenres) {
		this.albumGenres = albumGenres;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getAlbumUrl() {
		return albumUrl;
	}

	public void setAlbumUrl(String albumUrl) {
		this.albumUrl = albumUrl;
	}
	

	public Integer getSongsAmount() {
		return songsAmount;
	}

	public void setSongsAmount(Integer songsAmount) {
		this.songsAmount = songsAmount;
	}

	public String getAlbumType() {
		return albumType;
	}

	public void setAlbumType(String albumType) {
		this.albumType = albumType;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	

	@Override
	public String toString() {
		return "Album [albumTitle=" + albumTitle + ", albumPhotoUrl=" + albumPhotoUrl + ", albumGenres="
				+ ", uploadDate=" + uploadDate + ", releaseDate=" + releaseYear
				+ ", albumUrl=" + albumUrl + ", albumType=" + albumType + ", songsAmount=" + songsAmount + "]";
	}

	
	
	

}
