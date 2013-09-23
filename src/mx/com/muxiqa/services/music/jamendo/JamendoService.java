/**
 * 
 */
package mx.com.muxiqa.services.music.jamendo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.music.jamendo.entities.JamendoMusic;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author gerardomartinezgil
 * 
 */
public final class JamendoService {
	private static final String JAMENDO_URL = "http://api.jamendo.com/get2/";
	private static final String GET_TOP_TRACKS = "id+name+stream+album_name+album_id+album_image+artist_id+artist_name/track/XML/track_album+album_artist/";
	//private static final String GET_TOP_ALBUMS = "id+name+stream+album_name+album_id+album_image+artist_id+artist_name/album/XML/";
	private static final String TAG = "?tag_idstr=";
	private static final String ROWS = "&n=";
	private static final String ORDER_BY = "&order=ratingday_desc";

	// http://api.jamendo.com/get2/id+name+stream+album_name+album_id+album_image+artist_id+artist_name/track/XML/track_album+album_artist/?n=10&order=ratingday_desc&tag_idstr=latin
	private CoreService core;
	private XPathFactory xPathFactory;

	public JamendoService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param genreTag
	 * @param rows
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<JamendoMusic> getTopMusicThisDayByGenre(final String genreTag,
			final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<JamendoMusic> audios = null;
		if (genreTag != null && genreTag.trim().length() > 0) {
			final String urlAddress = JAMENDO_URL + GET_TOP_TRACKS + TAG
					+ genreTag + ROWS + rows + ORDER_BY;
			if (core.cacheContains(urlAddress)) {
				return (List<JamendoMusic>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//track", doc,
						XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					audios = new ArrayList<JamendoMusic>(totalResults);
					for (int i = 1; i <= totalResults; i++) {
						JamendoMusic music = new JamendoMusic();
						music.setId((String) xpath.evaluate("string(//track[" + i
								+ "]/id)", doc, XPathConstants.STRING));
						music.setTitle((String) xpath.evaluate("string(//track["
								+ i + "]/name)", doc, XPathConstants.STRING));
						music.setUrl((String) xpath.evaluate("string(//track[" + i
								+ "]/stream)", doc, XPathConstants.STRING));
						music.setAlbumName((String) xpath.evaluate(
								"string(//track[" + i + "]/album_name)", doc,
								XPathConstants.STRING));
						music.setAlbumId((String) xpath.evaluate("string(//track["
								+ i + "]/album_id)", doc, XPathConstants.STRING));
						music.setAlbumImageURL((String) xpath.evaluate(
								"string(//track[" + i + "]/album_image)", doc,
								XPathConstants.STRING));
						music.setArtistId((String) xpath.evaluate("string(//track["
								+ i + "]/artist_id)", doc, XPathConstants.STRING));
						music.setArtistName((String) xpath.evaluate(
								"string(//track[" + i + "]/artist_name)", doc,
								XPathConstants.STRING));
						audios.add(music);
					}
					core.addToCache(urlAddress, audios);
				}
			}
		}
		return audios;
	}

}// END OF FILE