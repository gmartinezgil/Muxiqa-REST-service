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
public final class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	
	public static final int LISTENING_AUDIO_TRACK = 0;
	public static final int ATTENDING_ARTIST_CONCERT = 1;
	
	private int type;
	private String comment;
	
	@Basic
	@OneToMany(mappedBy = "fromEvent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Artist> artists = new ArrayList<Artist>();

	@Basic
	@ManyToOne
	private Location fromLocation;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Location getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}
	
	public List<Artist> getArtists() {
		return artists;
	}

	public void addArtist(Artist artist) {
		artists.add(artist);
	}

}