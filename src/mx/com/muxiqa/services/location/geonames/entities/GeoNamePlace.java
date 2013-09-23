/**
 * 
 */
package mx.com.muxiqa.services.location.geonames.entities;

import java.io.Serializable;

/**
 * @author gerardomartinezgil
 * 
 */
public final class GeoNamePlace implements Serializable{
	private static final long serialVersionUID = 1L;
	private String countryName;
	private String cityName;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}