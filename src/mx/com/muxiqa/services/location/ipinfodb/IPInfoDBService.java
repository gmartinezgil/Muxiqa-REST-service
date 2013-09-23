/**
 * 
 */
package mx.com.muxiqa.services.location.ipinfodb;

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
public final class IPInfoDBService {
	private static final String KEY = "IPO_INFO_DB_API_KEY";
	private static final String URL = "http://api.ipinfodb.com/v2/";
	private static final String GET_CITY = "ip_query.php?key=";
	private static final String GET_COUNTRY = "ip_query_country.php?key=";
	private static final String IP = "&ip=";

	private CoreService core;
	private XPathFactory xPathFactory;

	/**
	 * 
	 */
	public IPInfoDBService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param ipAddress
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getCityLocationFromIPAddress(
			final String ipAddress) throws XPathExpressionException {
		if (ipAddress != null && ipAddress.trim().length() > 0) {
			final String urlAddress = URL + GET_CITY + KEY + IP + ipAddress;
			if (core.cacheContains(urlAddress)) {
				return (Location) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//Response", doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					final String status = (String) xpath.evaluate(
							"string(//Response/Status)", doc,
							XPathConstants.STRING);
					if (status != null && status.trim().length() > 0
							&& status.equals("OK")) {
						Location location = new Location();
						location.setLatitude(Float.parseFloat((String) xpath.evaluate(
								"string(//Response/Latitude)", doc,
								XPathConstants.STRING)));
						location.setLongitude(Float.parseFloat((String) xpath.evaluate(
								"string(//Response/Longitude)", doc,
								XPathConstants.STRING)));
						location.setCityCode((String) xpath.evaluate(
								"string(//Response/City)", doc,
								XPathConstants.STRING));
						location.setCityName((String) xpath.evaluate(
								"string(//Response/RegionName)", doc,
								XPathConstants.STRING));
						location.setCountryName((String) xpath.evaluate(
								"string(//Response/CountryName)", doc,
								XPathConstants.STRING));
						location.setCountryCode((String) xpath.evaluate(
								"string(//Response/CountryCode)", doc,
								XPathConstants.STRING));
						core.addToCache(urlAddress, location);
						return location;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param ipAddress
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getCountryLocationFromIPAddress(
			final String ipAddress) throws XPathExpressionException {
		if (ipAddress != null && ipAddress.trim().length() > 0) {
			final String urlAddress = URL + GET_COUNTRY + KEY + IP + ipAddress;
			if (core.cacheContains(urlAddress)) {
				return (Location) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//Response", doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					final String status = (String) xpath.evaluate(
							"string(//Response/Status)", doc,
							XPathConstants.STRING);
					if (status != null && status.trim().length() > 0
							&& status.equals("OK")) {
						Location location = new Location();
						location.setCountryName((String) xpath.evaluate(
								"string(//Response/CountryName)", doc,
								XPathConstants.STRING));
						location.setCountryCode((String) xpath.evaluate(
								"string(//Response/CountryCode)", doc,
								XPathConstants.STRING));
						core.addToCache(urlAddress, location);
						return location;
					}
				}
			}
		}
		return null;
	}

}//END OF FILE