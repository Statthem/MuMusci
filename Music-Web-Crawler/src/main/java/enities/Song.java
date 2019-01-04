package enities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Song {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@ManyToOne()
	private Album album;
	
	@Column(name="title")
	private String title;
	
	@Column(name="size")
	private String size;
	
	@Column(name="quality")
	private String quality;
	
	@Column(name="lenght")
	private String length;
	
	@Column(name="url")
	private String url;
	
	public Song() {}
	
	public Song(String title, String size, String quality, String length, String url) {
		super();
		this.title = title;
		this.size = size;
		this.quality = quality;
		this.length = length;
		this.url = url;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@Override
	public String toString() {
		return "Song [title=" + title + ", size=" + size + ", quality=" + quality + ", length=" + length + ", url="
				+ url + "]";
	}
	
	

}
