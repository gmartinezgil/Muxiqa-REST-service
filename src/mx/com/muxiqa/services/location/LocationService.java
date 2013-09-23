/**
 * 
 */
package mx.com.muxiqa.services.location;

import javax.xml.xpath.XPathExpressionException;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.core.db.entities.Location;
import mx.com.muxiqa.services.location.cloudmade.CloudMadeService;
import mx.com.muxiqa.services.location.geonames.GeoNamesService;
import mx.com.muxiqa.services.location.google.GoogleGeocodeService;
import mx.com.muxiqa.services.location.ipinfodb.IPInfoDBService;
import mx.com.muxiqa.services.location.opencellid.OpenCellIdService;
import mx.com.muxiqa.util.Constants;
import mx.com.muxiqa.util.Util;

/**
 * @author gerardomartinezgil
 * 
 */
public final class LocationService {

	private CloudMadeService cloudMadeService;
	private IPInfoDBService ipInfoDBService;
	private GoogleGeocodeService googleGeocodeService;
	private OpenCellIdService openCellIdService;
	private GeoNamesService geoNamesService;

	/**
	 * 
	 */
	public LocationService(final CoreService core) {
		cloudMadeService = new CloudMadeService(core);
		ipInfoDBService = new IPInfoDBService(core);
		googleGeocodeService = new GoogleGeocodeService(core);
		openCellIdService = new OpenCellIdService(core);
		geoNamesService = new GeoNamesService(core);
	}

	/**
	 * Not accurate, relative to ISP provider, not device/computer location.
	 * 
	 * @param ipAddress
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getLocationByIPAddress(final String ipAddress)
			throws XPathExpressionException {
		Location location = ipInfoDBService
				.getCityLocationFromIPAddress(ipAddress);
		if (location != null) {
			location.setMapURL(cloudMadeService.getMapURLFromLocation(location
					.getLatitude(), location.getLongitude(),
					CloudMadeService.MAXIMUM_ZOOM_LEVEL,
					CloudMadeService.MAX_SIZE_FORMAT));
			return location;
		}
		return null;
	}

	/**
	 * Not accurate, only for GSM phones, relative position to Cell Tower, not
	 * device, we have to calculate from the device the cell signal to
	 * accurately calculate the position.
	 * 
	 * @param mcc
	 * @param mnc
	 * @param cellid
	 * @param lac
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getLocationByCellTower(final String mcc, final String mnc,
			final String cellid, final String lac)
			throws XPathExpressionException {
		Location location = openCellIdService.getCellLocation(mcc, mnc, cellid,
				lac);
		if (location != null) {
			final Location locationToName = geoNamesService
					.getCountryCityFromLocation(String.valueOf(location
							.getLatitude()), String.valueOf(location
							.getLongitude()), Constants.SPANISH_LANGUAGE_CODE);// TODO:
			// needs
			// to
			// arrive
			// in
			// a
			// parameter...
			if (locationToName != null) {
				location.setCountryName(locationToName.getCountryName());
				location.setCityName(locationToName.getCityName());
			}
			location.setMapURL(cloudMadeService.getMapURLFromLocation(location
					.getLatitude(), location.getLongitude(),
					CloudMadeService.MAXIMUM_ZOOM_LEVEL,
					CloudMadeService.MAX_SIZE_FORMAT));
			return location;
		}
		return null;
	}

	/**
	 * Gets the location information by mean of it's latitude and longitude.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getLocationByLatitudeLongitude(final String latitude,
			final String longitude) throws XPathExpressionException {
		Location location = geoNamesService.getCountryCityFromLocation(
				latitude, longitude, Constants.SPANISH_LANGUAGE_CODE);// TODO:
		// it
		// must
		// came
		// as a
		// parameter...
		if (location != null) {
			location.setLatitude(Float.parseFloat(latitude));
			location.setLongitude(Float.parseFloat(longitude));
			location.setMapURL(cloudMadeService.getMapURLFromLocation(location
					.getLatitude(), location.getLongitude(),
					CloudMadeService.MAXIMUM_ZOOM_LEVEL,
					CloudMadeService.MAX_SIZE_FORMAT));
			return location;
		}
		return null;
	}

	/**
	 * Gets the latitude/longitude given the raw address information. The address
	 * includes all the location information.
	 * 
	 * @param address
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getLocationByAddress(final String address)// TODO: have to
			// split the
			// address...
			throws XPathExpressionException {
		Location location = googleGeocodeService.getLocation(Util.encodeStringOnGoogleStyle(address));
		if (location != null) {
			location.setMapURL(cloudMadeService.getMapURLFromLocation(location
					.getLatitude(), location.getLongitude(),
					CloudMadeService.MAXIMUM_ZOOM_LEVEL,
					CloudMadeService.MAX_SIZE_FORMAT));
			return location;
		}
		return null;
	}


	/**
	 * 
	 * @param streetName
	 * @param streetNumber
	 * @param colonyName
	 * @param delegationName
	 * @param cityName
	 * @param countryName
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getLocationByAddress(final String streetName,
			final String streetNumber, final String colonyName,
			final String delegationName, final String cityName,
			final String countryName) throws XPathExpressionException {
		StringBuffer sb = new StringBuffer();
		if (streetName != null && streetName.trim().length() > 0) {
			sb.append(Util.encodeStringOnGoogleStyle(streetName));
		}
		if(streetNumber != null && streetNumber.trim().length() > 0) {
			sb.append(Util.encodeStringOnGoogleStyle(streetNumber));
		}
		if(colonyName != null && colonyName.trim().length() > 0) {
			sb.append(Util.encodeStringOnGoogleStyle(colonyName));
		}
		if(delegationName != null && delegationName.trim().length() > 0) {
			sb.append(Util.encodeStringOnGoogleStyle(delegationName));
		}
		if(cityName != null && cityName.trim().length() > 0) {
			sb.append(Util.encodeStringOnGoogleStyle(cityName));
		}
		if(countryName != null && countryName.trim().length() > 0) {
			sb.append(Util.encodeStringOnGoogleStyle(countryName));
		}
		Location location = googleGeocodeService.getLocation(sb.toString());
		if (location != null) {
			location.setStreetName(streetName);
			location.setStreetNumber(streetNumber);
			location.setSubZoneName(colonyName);
			location.setZoneName(delegationName);
			location.setCityName(cityName);
			location.setCountryName(countryName);
			location.setMapURL(cloudMadeService.getMapURLFromLocation(location
					.getLatitude(), location.getLongitude(),
					CloudMadeService.MAXIMUM_ZOOM_LEVEL,
					CloudMadeService.MAX_SIZE_FORMAT));
			return location;
		}
		return null;
	}

}// END OF FILE