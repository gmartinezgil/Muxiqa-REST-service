/**
 * 
 */
package mx.com.muxiqa.services.location.cloudmade;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.core.db.entities.Location;
import mx.com.muxiqa.services.location.cloudmade.entities.CloudMadeRoute;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author gerardomartinezgil
 * 
 */
public final class CloudMadeService {
	//private static final Logger log = Logger.getLogger(CloudMadeService.class.getName());

	private static final String API_KEY = "CLOUDMADE_API_KEY";
	private static final String CLOUDMADE_TILE_URL = "http://tile.cloudmade.com/"
			+ API_KEY;
	// private static final String OPENSTREETMAP_TILE_URL =
	// "http://tile.openstreetmap.org/";
	private static final String TILE_STYLE = "/1";
	private static final String MINI_FORMAT = "/64";
	private static final String NORMAL_FORMAT = "/256";
	public static final int MAXIMUM_ZOOM_LEVEL = 18;
	public static final int MAX_SIZE_FORMAT = 0;
	public static final int MINI_SIZE_FORMAT = 1;
	private static final String CLOUDMADE_ROUTE_URL = "http://routes.cloudmade.com/"
			+ API_KEY + "/api/0.3/";
	private static final String GET_ROUTE = "shortest.gpx?units=";
	public static final String CAR_TRANSPORT = "car";
	public static final String FOOT_TRANSPORT = "foot";
	public static final String BICYCLE_TRANSPORT = "bicycle";
	public static final String KM_UNITS = "km";
	public static final String MI_UNITS = "mi";

	private CoreService core;
	private XPathFactory xPathFactory;

	// http://routes.cloudmade.com/965626de7cb35a36bad7fa5d1c82dcac/api/0.3/19.40120,-99.15706,19.39341,-99.17304/car.gpx?lang=es&units=km

	public CloudMadeService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param zoomLevel
	 * @return
	 */
	public String getMapURLFromLocation(final double latitude,
			final double longitude, final int zoomLevel, final int format) {
		if (zoomLevel > 0 && zoomLevel <= MAXIMUM_ZOOM_LEVEL) {
			return CLOUDMADE_TILE_URL + TILE_STYLE
					+ ((format == 0) ? NORMAL_FORMAT : MINI_FORMAT) + "/"
					+ getTileNumber(latitude, longitude, zoomLevel) + ".png";
		}
		return null;
	}

	/**
	 * 
	 * @param startLatitude
	 * @param startLongitude
	 * @param endLatitude
	 * @param endLongitude
	 * @param languageCode
	 * @param transportType
	 * @param unitType
	 * @return
	 * @throws XPathExpressionException
	 */
	public CloudMadeRoute getRouteFromStartEndPoints(final float startLatitude,
			final float startLongitude, final float endLatitude,
			final float endLongitude, final String languageCode,
			final String transportType, final String unitType)
			throws XPathExpressionException {
		CloudMadeRoute route = null;
		final String urlAddress = CLOUDMADE_ROUTE_URL + startLatitude + ","
				+ startLongitude + "," + endLatitude + "," + endLongitude + "/"
				+ transportType + "/" + GET_ROUTE + unitType + "&lang="
				+ languageCode;
		if (core.cacheContains(urlAddress)) {
			return (CloudMadeRoute) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalPointsRoute = ((NodeList) xpath.evaluate("//wpt",
					doc, XPathConstants.NODESET)).getLength();
			if (totalPointsRoute > 0) {
				route = new CloudMadeRoute();
				route.setDistance((String) xpath.evaluate(
						"string(/gpx/extensions/distance)", doc,
						XPathConstants.STRING));
				route.setTime((String) xpath.evaluate(
						"string(/gpx/extensions/time)", doc,
						XPathConstants.STRING));
				route.setStart((String) xpath.evaluate(
						"string(/gpx/extensions/start)", doc,
						XPathConstants.STRING));
				route.setEnd((String) xpath.evaluate(
						"string(/gpx/extensions/end)", doc,
						XPathConstants.STRING));
				for (int i = 1; i <= totalPointsRoute; i++) {
					Location location = new Location();
					location.setLatitude(Float.parseFloat((String) xpath
							.evaluate("string(//wpt[" + i + "]/@lat)", doc,
									XPathConstants.STRING)));
					location.setLongitude(Float.parseFloat((String) xpath
							.evaluate("string(//wpt[" + i + "]/@lon)", doc,
									XPathConstants.STRING)));
					route.addRoutePoint(location);
				}
				final int totalRouteDirections = ((NodeList) xpath.evaluate(
						"//rtept", doc, XPathConstants.NODESET)).getLength();
				if (totalRouteDirections > 0) {
					for (int i = 1; i <= totalRouteDirections; i++) {
						Location location = new Location();
						location.setLatitude(Float.parseFloat((String) xpath
								.evaluate("string(//rtept[" + i + "]/@lat)",
										doc, XPathConstants.STRING)));
						location.setLongitude(Float.parseFloat((String) xpath
								.evaluate("string(//rtept[" + i + "]/@lon)",
										doc, XPathConstants.STRING)));
						location.setSummary((String) xpath.evaluate(
								"string(//rtept[" + i + "]/desc)", doc,
								XPathConstants.STRING));
						route.addRouteDirection(location);
					}
				}
				core.addToCache(urlAddress, route);
				return route;
			}
		}

		return null;
	}

	// ////////////////////////////////////////////////////////////////////////
	private String getTileNumber(final double lat, final double lon,
			final int zoom) {
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math.floor((1 - Math.log(Math
				.tan(Math.toRadians(lat))
				+ 1 / Math.cos(Math.toRadians(lat)))
				/ Math.PI)
				/ 2 * (1 << zoom));
		return ("" + zoom + "/" + xtile + "/" + ytile);
	}

}// END OF FILE