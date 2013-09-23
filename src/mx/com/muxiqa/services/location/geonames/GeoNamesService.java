/**
 * 
 */
package mx.com.muxiqa.services.location.geonames;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.core.db.entities.Location;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author gerardomartinezgil
 * 
 */
public final class GeoNamesService {
	private static final String GEONAMES_URL = "http://ws.geonames.org/";
	private static final String GET_COUNTRYSUBDIVISION = "countrySubdivision";

	private CoreService core;
	private XPathFactory xPathFactory;

	/**
	 * 
	 */

	public GeoNamesService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getCountryCityFromLocation(final String latitude,
			final String longitude, final String languageCode)
			throws XPathExpressionException {
		if (latitude != null && latitude.trim().length() > 0
				&& longitude != null && longitude.trim().length() > 0) {
			final String urlAddress = GEONAMES_URL
					+ GET_COUNTRYSUBDIVISION
					+ "?lat="
					+ latitude.substring(0, 6)
					+ "&lng="
					+ longitude.substring(0, 6)
					+ ((languageCode != null && languageCode.trim().length() > 0) ? "&lang="
							+ languageCode
							: "");
			if (core.cacheContains(urlAddress)) {
				return (Location) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//countrySubdivision", doc, XPathConstants.NODESET))
						.getLength();
				if (totalResults == 1) {
					Location location = new Location();
					location.setCountryName((String) xpath.evaluate(
							"string(//countrySubdivision/countryName)", doc,
							XPathConstants.STRING));
					location.setCityName((String) xpath.evaluate(
							"string(//countrySubdivision/adminName1)", doc,
							XPathConstants.STRING));
					core.addToCache(urlAddress, location);
					return location;
				}
			}
		}
		return null;
	}

}// END OF FILE