/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author jerry
 * 
 */
public final class EchoNestReview implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String summary;
	private String url;
	private String imageURL;
	private String release;
	private String dateReviewed;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getDateReviewed() {
		return dateReviewed;
	}

	public void setDateReviewed(String dateReviewed) {
		this.dateReviewed = dateReviewed;
	}
}