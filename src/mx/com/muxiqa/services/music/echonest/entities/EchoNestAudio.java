/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author jerry
 * 
 */
public final class EchoNestAudio implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String release;
	private String title;
	private String url;
	private String length;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

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

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
}
