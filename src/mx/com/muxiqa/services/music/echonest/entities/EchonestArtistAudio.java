/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author gerardomartinezgil
 *
 */
public final class EchonestArtistAudio implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String artistId;
	private String artistName;
	private String audioId;
	private String audioTitle;
	private String audioAlbumName;
	private String audioAlbumImageURL;
	private String audioPreviewURL;
	private String audioHotness;
	
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
	public String getAudioId() {
		return audioId;
	}
	public void setAudioId(String audioId) {
		this.audioId = audioId;
	}
	public String getAudioTitle() {
		return audioTitle;
	}
	public void setAudioTitle(String audioTitle) {
		this.audioTitle = audioTitle;
	}
	public String getAudioAlbumName() {
		return audioAlbumName;
	}
	public void setAudioAlbumName(String audioAlbumName) {
		this.audioAlbumName = audioAlbumName;
	}
	public String getAudioAlbumImageURL() {
		return audioAlbumImageURL;
	}
	public void setAudioAlbumImageURL(String audioAlbumImageURL) {
		this.audioAlbumImageURL = audioAlbumImageURL;
	}
	public String getAudioPreviewURL() {
		return audioPreviewURL;
	}
	public void setAudioPreviewURL(String audioPreviewURL) {
		this.audioPreviewURL = audioPreviewURL;
	}
	public String getAudioHotness() {
		return audioHotness;
	}
	public void setAudioHotness(String audioHotness) {
		this.audioHotness = audioHotness;
	}
	
}