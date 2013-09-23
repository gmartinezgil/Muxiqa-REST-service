/**
 * 
 */
package mx.com.muxiqa.services.music.jamendo.entities;

import java.io.Serializable;

/**
 * @author gerardomartinezgil
 * 
 */
public final class JamendoMusic implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String url;
	private String albumName;
	private String albumId;
	private String albumImageURL;
	private String artistId;
	private String artistName;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumImageURL() {
		return albumImageURL;
	}

	public void setAlbumImageURL(String albumImageURL) {
		this.albumImageURL = albumImageURL;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

}