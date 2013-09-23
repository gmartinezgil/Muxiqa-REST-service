/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author jerry
 * 
 */
public final class EchoNestVideo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String url;
	private String imagePreview;
	private String dateFound;
	private String site;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImagePreview() {
		return imagePreview;
	}

	public void setImagePreview(String imagePreview) {
		this.imagePreview = imagePreview;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateFound() {
		return dateFound;
	}

	public void setDateFound(String dateFound) {
		this.dateFound = dateFound;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
}
