/**
 * 
 */
package mx.com.muxiqa.services.music.lastfm.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gerardomartinezgil
 * 
 */
public final class LastFMSong implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String artistName;
	private String artistMusicBrainzId;
	private String albumName;
	private String albumMusicBrainzId;
	private String albumSmallImageURL;
	private String albumMediumImageURL;
	private String albumLargeImageURL;
	private List<String> tags;

	public LastFMSong() {
		tags = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getArtistMusicBrainzId() {
		return artistMusicBrainzId;
	}

	public void setArtistMusicBrainzId(String artistMusicBrainzId) {
		this.artistMusicBrainzId = artistMusicBrainzId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumMusicBrainzId() {
		return albumMusicBrainzId;
	}

	public void setAlbumMusicBrainzId(String albumMusicBrainzId) {
		this.albumMusicBrainzId = albumMusicBrainzId;
	}

	public String getAlbumSmallImageURL() {
		return albumSmallImageURL;
	}

	public void setAlbumSmallImageURL(String albumSmallImageURL) {
		this.albumSmallImageURL = albumSmallImageURL;
	}

	public String getAlbumMediumImageURL() {
		return albumMediumImageURL;
	}

	public void setAlbumMediumImageURL(String albumMediumImageURL) {
		this.albumMediumImageURL = albumMediumImageURL;
	}

	public String getAlbumLargeImageURL() {
		return albumLargeImageURL;
	}

	public void setAlbumLargeImageURL(String albumLargeImageURL) {
		this.albumLargeImageURL = albumLargeImageURL;
	}

	public void addTag(final String tag) {
		tags.add(tag);
	}

	public List<String> getTags() {
		return tags;
	}

}