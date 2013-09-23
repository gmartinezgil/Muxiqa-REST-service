/**
 * 
 */
package mx.com.muxiqa.services.location.opencellid;

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
public final class OpenCellIdService {
	private static final String API_KEY = "OPEN_CELL_ID_API_KEY";
	private static final String OPENCELLID_URL = "http://www.opencellid.org/cell/";
	private static final String GET_CELLINFO = "get";

	private CoreService core;
	private XPathFactory xPathFactory;

	public OpenCellIdService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param mcc
	 * @param mnc
	 * @param cellid
	 * @param lac
	 * @return
	 * @throws XPathExpressionException
	 */
	public Location getCellLocation(final String mcc, final String mnc,
			final String cellid, final String lac)
			throws XPathExpressionException {
		if (mcc != null && mcc.trim().length() > 0 && mnc != null
				&& mnc.trim().length() > 0) {
			final String urlAddress = OPENCELLID_URL
					+ GET_CELLINFO
					+ "?key="
					+ API_KEY
					+ "&mcc="
					+ mcc
					+ "&mnc="
					+ mnc
					+ ((cellid != null && cellid.trim().length() > 0) ? "&cellid="
							+ cellid
							: "")
					+ ((lac != null && lac.trim().length() > 0) ? "&lac=" + lac
							: "");
			if(core.cacheContains(urlAddress)) {
				return (Location)core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//rsp[@stat='ok']", doc, XPathConstants.NODESET))
						.getLength();
				if (totalResults == 1) {
					Location location = new Location();
					location.setLatitude(Float.parseFloat((String) xpath
							.evaluate("string(//cell/@lat)", doc,
									XPathConstants.STRING)));
					location.setLongitude(Float.parseFloat((String) xpath
							.evaluate("string(//cell/@lon)", doc,
									XPathConstants.STRING)));
					core.addToCache(urlAddress, location);
					return location;
				}
			}
		}
		return null;
	}

}// END OF FILE