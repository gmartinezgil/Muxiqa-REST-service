/**
 * 
 */
package mx.com.muxiqa.services.music.lastfm.entities;

import java.io.Serializable;

/**
 * @author gerardomartinezgil
 * 
 */
public final class LastFMArtist implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String musicBrainzId;
	private String smallPictureURL;
	private String mediumPictureURL;
	private String largePictureURL;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMusicBrainzId() {
		return musicBrainzId;
	}

	public void setMusicBrainzId(String musicBrainzId) {
		this.musicBrainzId = musicBrainzId;
	}

	public String getSmallPictureURL() {
		return smallPictureURL;
	}

	public void setSmallPictureURL(String smallPictureURL) {
		this.smallPictureURL = smallPictureURL;
	}

	public String getMediumPictureURL() {
		return mediumPictureURL;
	}

	public void setMediumPictureURL(String mediumPictureURL) {
		this.mediumPictureURL = mediumPictureURL;
	}

	public String getLargePictureURL() {
		return largePictureURL;
	}

	public void setLargePictureURL(String largePictureURL) {
		this.largePictureURL = largePictureURL;
	}
}