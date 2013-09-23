/**
 * 
 */
package mx.com.muxiqa.services.location.google;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.core.db.entities.Location;

import org.w3c.dom.Document;

/**
 * @author gerardomartinezgil
 * 
 */
public final class GoogleGeocodeService {
	private String URL = "http://maps.google.com/maps/api/geocode/xml?address=";
	private String SENSOR = "&sensor=false";

	private CoreService core;
	private XPathFactory xPathFactory;

	/**
	 * 
	 */

	public GoogleGeocodeService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	public Location getLocation(final String address)
			throws XPathExpressionException {
		if (address != null && address.trim().length() > 0) {
			if (core.cacheContains(URL + address + SENSOR)) {
				return (Location) core.getFromCache(URL + address + SENSOR);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(URL
					+ address + SENSOR);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final String status = (String) xpath.evaluate(
						"string(/GeocodeResponse/status)", doc,
						XPathConstants.STRING);
				if (status != null && status.trim().length() > 0
						&& status.equals("OK")) {
					final String latitude = (String) xpath
							.evaluate(
									"string(/GeocodeResponse/result/geometry/location/lat)",
									doc, XPathConstants.STRING);
					final String longitude = (String) xpath
							.evaluate(
									"string(/GeocodeResponse/result/geometry/location/lng)",
									doc, XPathConstants.STRING);
					Location location = new Location();
					location.setLatitude(Float.parseFloat(latitude));
					location.setLongitude(Float.parseFloat(longitude));
					core.addToCache(URL + address, location);
					return location;
				}
			}
		}
		return null;
	}

}// END OF FILE