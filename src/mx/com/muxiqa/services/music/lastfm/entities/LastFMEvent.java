/**
 * 
 */
package mx.com.muxiqa.services.music.lastfm.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gerardomartinezgil
 * 
 */
public final class LastFMEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private List<String> artistNames;
	private String artistPictureURL;
	private String placeName;
	private String placeCity;
	private String placeCountry;
	private String placeStreet;
	private String placeLatitude;
	private String placeLongitude;
	private String placePictureURL;
	private String placePageURL;
	private String placePhoneNumber;
	private String eventDate;
	private String eventDescription;
	private String eventPageURL;
	private List<String> tags;

	public LastFMEvent() {
		artistNames = new ArrayList<String>();
		tags = new ArrayList<String>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtistPictureURL() {
		return artistPictureURL;
	}

	public void setArtistPictureURL(String artistPictureURL) {
		this.artistPictureURL = artistPictureURL;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceStreet() {
		return placeStreet;
	}

	public void setPlaceStreet(String placeStreet) {
		this.placeStreet = placeStreet;
	}

	public String getPlaceLatitude() {
		return placeLatitude;
	}

	public void setPlaceLatitude(String placeLatitude) {
		this.placeLatitude = placeLatitude;
	}

	public String getPlaceLongitude() {
		return placeLongitude;
	}

	public void setPlaceLongitude(String placeLongitude) {
		this.placeLongitude = placeLongitude;
	}

	public String getPlacePictureURL() {
		return placePictureURL;
	}

	public void setPlacePictureURL(String placePictureURL) {
		this.placePictureURL = placePictureURL;
	}

	public String getPlacePageURL() {
		return placePageURL;
	}

	public void setPlacePageURL(String placePageURL) {
		this.placePageURL = placePageURL;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventPageURL() {
		return eventPageURL;
	}

	public void setEventPageURL(String eventPageURL) {
		this.eventPageURL = eventPageURL;
	}

	public void addArtistName(final String artistName) {
		artistNames.add(artistName);
	}

	public List<String> getArtistNames() {
		return artistNames;
	}

	public void addTag(final String tag) {
		tags.add(tag);
	}

	public List<String> getTags() {
		return tags;
	}

	public String getPlacePhoneNumber() {
		return placePhoneNumber;
	}

	public void setPlacePhoneNumber(String placePhoneNumber) {
		this.placePhoneNumber = placePhoneNumber;
	}

	public String getPlaceCity() {
		return placeCity;
	}

	public void setPlaceCity(String placeCity) {
		this.placeCity = placeCity;
	}

	public String getPlaceCountry() {
		return placeCountry;
	}

	public void setPlaceCountry(String placeCountry) {
		this.placeCountry = placeCountry;
	}

}