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
public final class Album implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	private String name;
	private String imageURL;
	
	@Basic
	@OneToMany(mappedBy = "fromAlbum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Audio> audios = new ArrayList<Audio>();

	@Basic
	@ManyToOne
	private Artist fromArtist;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
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

	public Artist getFromArtist() {
		return fromArtist;
	}

	public void setFromArtist(Artist fromArtist) {
		this.fromArtist = fromArtist;
	}

	public List<Audio> getAudios() {
		return audios;
	}

	public void addAudio(Audio audio) {
		audios.add(audio);
	}
	
}