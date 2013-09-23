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
public final class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	private float latitude;
	private float longitude;
	private String countryName;
	private String countryCode;
	private String cityName;
	private String cityCode;
	private String postalCodeNumber;
	private String streetName;
	private String streetNumber;
	private String subZoneName;
	private String zoneName;
	private String summary;
	private String mapURL;
	//private String cellId;
	
	//private String geohash;
	
	
	@Basic
	@OneToMany(mappedBy = "fromLocation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Event> events = new ArrayList<Event>();

	@Basic
	@ManyToOne
	private Device fromDevice;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getPostalCodeNumber() {
		return postalCodeNumber;
	}

	public void setPostalCodeNumber(String postalCodeNumber) {
		this.postalCodeNumber = postalCodeNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getSubZoneName() {
		return subZoneName;
	}

	public void setSubZoneName(String subZoneName) {
		this.subZoneName = subZoneName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMapURL() {
		return mapURL;
	}

	public void setMapURL(String mapURL) {
		this.mapURL = mapURL;
	}

	public Device getFromDevice() {
		return fromDevice;
	}

	public void setFromDevice(Device fromDevice) {
		this.fromDevice = fromDevice;
	}
	
	public List<Event> getEvents() {
		return events;
	}

	public void addEvent(Event event) {
		events.add(event);
	}

}