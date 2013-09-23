/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author jerry
 * 
 */
public final class EchoNestArtist implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String hotness;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHotness() {
		return hotness;
	}

	public void setHotness(String hotness) {
		this.hotness = hotness;
	}

}