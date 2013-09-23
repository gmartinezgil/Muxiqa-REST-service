/**
 * 
 */
package mx.com.muxiqa.services.music.chartlyric;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author gerardomartinezgil
 * 
 */
public final class ChartLyricService {
	private static final String CHARTLYRIC_URL = "http://api.chartlyrics.com/apiv1.asmx";
	private static final String SEARCH_LYRIC = "/SearchLyric";
	private static final String GET_LYRIC = "/GetLyric";

	private CoreService core;
	private XPathFactory xPathFactory;

	private static final Logger log = Logger.getLogger(ChartLyricService.class
			.getName());


	public ChartLyricService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param artistName
	 * @param songName
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public String getLyricFromArtistSong(final String artistName,
			final String songName) throws IOException,
			XPathExpressionException {
		if (artistName != null && artistName.trim().length() > 0
				&& songName != null && songName.trim().length() > 0) {
			final String urlAddress = CHARTLYRIC_URL + SEARCH_LYRIC
					+ "?artist=" + URLEncoder.encode(artistName, "UTF-8")
					+ "&song=" + URLEncoder.encode(songName, "UTF-8");
			if (core.cacheContains(urlAddress)) {
				return (String) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//SearchLyricResult", doc, XPathConstants.NODESET))
						.getLength();
				if (totalResults > 0) {
					final String lyricId = (String) xpath.evaluate(
							"string(//SearchLyricResult[1]/LyricId)", doc,
							XPathConstants.STRING);
					log.warning(lyricId);
					final String lyricCheckSum = (String) xpath.evaluate(
							"string(//SearchLyricResult[1]/LyricChecksum)", doc,
							XPathConstants.STRING);
					log.warning(lyricCheckSum);
					final String lyric = getLyricFromId(lyricId, lyricCheckSum); 
					core.addToCache(urlAddress, lyric);
					return lyric;
				}
			}
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	private String getLyricFromId(final String lyricId,
			final String lyricCheckSum) throws XPathExpressionException {
		if (lyricId != null && lyricId.trim().length() > 0
				&& lyricCheckSum != null && lyricCheckSum.trim().length() > 0) {
			final String urlAddress = CHARTLYRIC_URL + GET_LYRIC + "?lyricId="
					+ lyricId + "&lyricCheckSum=" + lyricCheckSum;
			if (core.cacheContains(urlAddress)) {
				return (String) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//Lyric", doc,
						XPathConstants.NODESET)).getLength();
				if (totalResults == 1) {
					String lyric = (String) xpath.evaluate("string(//Lyric)", doc,
							XPathConstants.STRING);
					log.warning(lyric);
					core.addToCache(urlAddress, lyric);
					return lyric;
				}
			}
		}
		return null;
	}

}// END OF FILE