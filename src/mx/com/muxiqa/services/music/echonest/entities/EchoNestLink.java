/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author jerry
 * 
 */
public final class EchoNestLink implements Serializable {
	private static final long serialVersionUID = 1L;
	private String musicbrainzURL;
	private String myspaceURL;
	private String lastfmURL;
	private String aolmusicURL;
	private String amazonURL;
	private String itunesURL;
	private String aolMusicURL;

	public String getMusicbrainzURL() {
		return musicbrainzURL;
	}

	public void setMusicbrainzURL(String musicbrainzURL) {
		this.musicbrainzURL = musicbrainzURL;
	}

	public String getMyspaceURL() {
		return myspaceURL;
	}

	public void setMyspaceURL(String myspaceURL) {
		this.myspaceURL = myspaceURL;
	}

	public String getLastfmURL() {
		return lastfmURL;
	}

	public void setLastfmURL(String lastfmURL) {
		this.lastfmURL = lastfmURL;
	}

	public String getAolmusicURL() {
		return aolmusicURL;
	}

	public void setAolmusicURL(String aolmusicURL) {
		this.aolmusicURL = aolmusicURL;
	}

	public String getAmazonURL() {
		return amazonURL;
	}

	public void setAmazonURL(String amazonURL) {
		this.amazonURL = amazonURL;
	}

	public String getItunesURL() {
		return itunesURL;
	}

	public void setItunesURL(String itunesURL) {
		this.itunesURL = itunesURL;
	}

	public void setAolMusicURL(String aolMusicURL) {
		this.aolMusicURL = aolMusicURL;
	}

	public String getAolMusicURL() {
		return aolMusicURL;
	}
}
