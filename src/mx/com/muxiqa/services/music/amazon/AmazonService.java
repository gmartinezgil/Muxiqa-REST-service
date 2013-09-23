/**
 * 
 */
package mx.com.muxiqa.services.music.amazon;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.music.amazon.entities.AmazonAlbum;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author gerardomartinezgil
 * 
 */
public final class AmazonService {
	// to comply with the new schema request...
	private AmazonSignedRequestHelper amazonSignedRequestHelper;

	private CoreService core;
	private XPathFactory xPathFactory;

	public AmazonService(final CoreService core)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, ParserConfigurationException {
		this.core = core;
		amazonSignedRequestHelper = new AmazonSignedRequestHelper();
		xPathFactory = XPathFactory.newInstance();
	}

	public AmazonAlbum getAlbumInformation(final String artistName,
			final String songName) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2009-03-31");
		params.put("Operation", "ItemSearch");
		params.put("SearchIndex", "Music");
		params.put("ResponseGroup",
				"ItemAttributes,Offers,Images,Reviews,Variations");
		// the proper request...
		params.put("Artist", artistName);
		// params.put("Title", album);
		params.put("Keywords", songName);
		// get it...
		final Document doc = core.getHTTPContentAsXMLDocument(amazonSignedRequestHelper
						.sign(params));
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			AmazonAlbum amazonAlbum = new AmazonAlbum();
			amazonAlbum.setDetailPage((String) xpath.evaluate(
					"string(/ItemSearchResponse/Items/Item[1]/DetailPageURL)",
					doc, XPathConstants.STRING));
			amazonAlbum.setSmallImage((String) xpath.evaluate(
					"string(/ItemSearchResponse/Items/Item[1]/SmallImage/URL)",
					doc, XPathConstants.STRING));
			amazonAlbum
					.setArtist((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/ItemAttributes/Artist)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setLabel((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/ItemAttributes/Label)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setPrice((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/ItemAttributes/ListPrice/FormattedPrice)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setReleaseDate((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/ItemAttributes/OriginalReleaseDate)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setUpc((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/ItemAttributes/UPC)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setLowestPrice((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/OfferSummary/LowestNewPrice/FormattedPrice)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setAverageRating((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/CustomerReviews/AverageRating)",
									doc, XPathConstants.STRING));
			amazonAlbum
					.setTotalReviews((String) xpath
							.evaluate(
									"string(/ItemSearchResponse/Items/Item[1]/CustomerReviews/TotalReviews)",
									doc, XPathConstants.STRING));
			return amazonAlbum;
		}
		return null;
	}

}// END OF FILE