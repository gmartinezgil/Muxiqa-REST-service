/**
 * 
 */
package mx.com.muxiqa.services.music.youtube.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gerardomartinezgil
 * 
 */
public final class YouTubeVideo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private String mediumThumbNailURL;
	private String largeThumbNailURL;
	private List<String> videos;

	public YouTubeVideo() {
		videos = new ArrayList<String>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMediumThumbNailURL() {
		return mediumThumbNailURL;
	}

	public void setMediumThumbNailURL(String mediumThumbNailURL) {
		this.mediumThumbNailURL = mediumThumbNailURL;
	}

	public String getLargeThumbNailURL() {
		return largeThumbNailURL;
	}

	public void setLargeThumbNailURL(String largeThumbNailURL) {
		this.largeThumbNailURL = largeThumbNailURL;
	}

	public List<String> getVideos() {
		return videos;
	}

	public void addVideo(final String videoURL) {
		videos.add(videoURL);
	}
}