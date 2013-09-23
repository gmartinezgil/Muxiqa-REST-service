/**
 * 
 */
package mx.com.muxiqa.services.core.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

/**
 * @author gerardomartinezgil
 * 
 */
@Entity
public final class Artist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	private String artistId;
	private String name;
	private String imageURL;

	@Basic
	@OneToMany(mappedBy = "fromArtist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Album> albums = new ArrayList<Album>();

	@Basic
	@ManyToOne
	private Event fromEvent;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Event getFromEvent() {
		return fromEvent;
	}

	public void setFromEvent(Event fromEvent) {
		this.fromEvent = fromEvent;
	}

	public List<Album> getAlbums() {
		return albums;
	}
	
	public void addAlbum(Album album) {
		albums.add(album);
	}

}