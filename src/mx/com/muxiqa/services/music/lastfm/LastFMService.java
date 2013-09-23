/**
 * 
 */
package mx.com.muxiqa.services.music.lastfm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMArtist;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMEvent;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMSong;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author gerardomartinezgil
 * 
 */
public final class LastFMService {
	// private static final Logger logger =
	// Logger.getLogger(LastFMService.class.getName());

	public static final int MAX_RESULTS_TOP_SONGS = 50;
	public static final int MAX_RESULTS_SEARCH_SONGS = 30;

	private static final String API_KEY = "LASTFM_API_KEY";
	private static final String LASTFM_URL = "http://ws.audioscrobbler.com/2.0/?method=";
	private static final String GET_GEOTOPTRACKS = "geo.gettoptracks";
	private static final String SEARCH_TRACK = "track.search";
	private static final String GET_TRACKINFO = "track.getinfo";
	private static final String GET_GEOEVENTS = "geo.getevents";
	private static final String GET_GEOTOPARTISTS = "geo.gettopartists";
	private static final String GET_ARTISTEVENTS = "artist.getevents";
	private static final String GET_ARTISTINFO = "artist.getinfo";

	private CoreService core;
	private XPathFactory xPathFactory;

	public LastFMService(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param countryName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public List<LastFMSong> getTopSongs(final String countryName,
			final String cityName) throws XPathExpressionException,
			UnsupportedEncodingException {
		List<LastFMSong> songs = null;
		if (countryName != null && countryName.trim().length() > 0) {
			final String urlAddress = LASTFM_URL
					+ GET_GEOTOPTRACKS
					+ "&country="
					+ URLEncoder.encode(countryName, "UTF-8")
					+ ((cityName != null && cityName.trim().length() > 0) ? "&location="
							+ URLEncoder.encode(cityName, "UTF-8")
							: "") + "&api_key=" + API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (List<LastFMSong>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//track",
						doc, XPathConstants.NODESET)).getLength();
				songs = new ArrayList<LastFMSong>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					LastFMSong song = new LastFMSong();
					song.setName((String) xpath.evaluate("string(//track[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					song.setArtistName((String) xpath.evaluate(
							"string(//track[" + i + "]/artist/name)", doc,
							XPathConstants.STRING));
					song.setArtistMusicBrainzId((String) xpath.evaluate(
							"string(//track[" + i + "]/artist/mbid)", doc,
							XPathConstants.STRING));
					song.setAlbumSmallImageURL((String) xpath.evaluate(
							"string(//track[" + i + "]/image[@size='small'])",
							doc, XPathConstants.STRING));
					song.setAlbumMediumImageURL((String) xpath.evaluate(
							"string(//track[" + i + "]/image[@size='medium'])",
							doc, XPathConstants.STRING));
					song.setAlbumLargeImageURL((String) xpath.evaluate(
							"string(//track[" + i + "]/image[@size='large'])",
							doc, XPathConstants.STRING));
					songs.add(song);
				}
				core.addToCache(urlAddress, songs);
			}
		}
		return songs;
	}

	/**
	 * 
	 * @param songTitle
	 * @param artistName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public List<LastFMSong> searchForSong(final String songTitle,
			final String artistName, final int rows)
			throws XPathExpressionException, UnsupportedEncodingException {
		List<LastFMSong> songs = null;
		if (songTitle != null && songTitle.trim().length() > 0) {
			final String urlAddress = LASTFM_URL
					+ SEARCH_TRACK
					+ "&track="
					+ URLEncoder.encode(songTitle, "UTF-8")
					+ ((artistName != null && artistName.trim().length() > 0) ? "&artist="
							+ URLEncoder.encode(artistName, "UTF-8")
							: "")
					+ ((rows > 0 && rows <= MAX_RESULTS_SEARCH_SONGS) ? "&limit="
							+ rows
							: "") + "&api_key=" + API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (List<LastFMSong>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//track",
						doc, XPathConstants.NODESET)).getLength();
				songs = new ArrayList<LastFMSong>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					LastFMSong song = new LastFMSong();
					song.setName((String) xpath.evaluate("string(//track[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					song.setArtistName((String) xpath.evaluate(
							"string(//track[" + i + "]/artist)", doc,
							XPathConstants.STRING));
					song.setAlbumSmallImageURL((String) xpath.evaluate(
							"string(//track[" + i + "]/image[@size='small'])",
							doc, XPathConstants.STRING));
					song.setAlbumMediumImageURL((String) xpath.evaluate(
							"string(//track[" + i + "]/image[@size='medium'])",
							doc, XPathConstants.STRING));
					song.setAlbumLargeImageURL((String) xpath.evaluate(
							"string(//track[" + i + "]/image[@size='large'])",
							doc, XPathConstants.STRING));
					songs.add(song);
				}
				core.addToCache(urlAddress, songs);
			}
		}
		return songs;
	}

	/**
	 * 
	 * @param songTitle
	 * @param artistName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	public LastFMSong getSongInfo(final String songTitle,
			final String artistName) throws XPathExpressionException,
			UnsupportedEncodingException {
		LastFMSong song = null;
		if (songTitle != null && songTitle.trim().length() > 0) {
			final String urlAddress = LASTFM_URL
					+ GET_TRACKINFO
					+ "&track="
					+ URLEncoder.encode(songTitle, "UTF-8")
					+ ((artistName != null && artistName.trim().length() > 0) ? "&artist="
							+ URLEncoder.encode(artistName, "UTF-8")
							: "") + "&api_key=" + API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (LastFMSong) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//track",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults == 1) {
					song = new LastFMSong();
					song
							.setName((String) xpath.evaluate(
									"string(//track/name)", doc,
									XPathConstants.STRING));
					song.setArtistMusicBrainzId((String) xpath.evaluate(
							"string(//track/artist/mbid)", doc,
							XPathConstants.STRING));
					song.setArtistName((String) xpath.evaluate(
							"string(//track/artist/name)", doc,
							XPathConstants.STRING));
					song.setAlbumName((String) xpath.evaluate(
							"string(//track/album/title)", doc,
							XPathConstants.STRING));
					song.setAlbumMusicBrainzId((String) xpath.evaluate(
							"string(//track/album/mbid)", doc,
							XPathConstants.STRING));
					song.setAlbumSmallImageURL((String) xpath.evaluate(
							"string(//track/album/image[@size='small'])", doc,
							XPathConstants.STRING));
					song.setAlbumMediumImageURL((String) xpath.evaluate(
							"string(//track/album/image[@size='medium'])", doc,
							XPathConstants.STRING));
					song.setAlbumLargeImageURL((String) xpath.evaluate(
							"string(//track/album/image[@size='large'])", doc,
							XPathConstants.STRING));
					final int totalTags = ((NodeList) xpath.evaluate("//tag",
							doc, XPathConstants.NODESET)).getLength();
					for (int i = 1; i <= totalTags; i++) {
						song.addTag((String) xpath.evaluate("string(//tag[" + i
								+ "]/name)", doc, XPathConstants.STRING));
					}
					core.addToCache(urlAddress, song);
				}
			}
		}
		return song;
	}

	/**
	 * 
	 * @param countryName
	 * @param cityName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public List<LastFMEvent> getEventsForLocation(final String countryName,
			final String cityName) throws XPathExpressionException,
			UnsupportedEncodingException {
		List<LastFMEvent> events = null;
		if (countryName != null && countryName.trim().length() > 0) {
			final String urlAddress = LASTFM_URL
					+ GET_GEOEVENTS
					+ "&location="
					+ ((cityName != null && cityName.trim().length() > 0) ? URLEncoder
							.encode(cityName, "UTF-8")
							+ ","
							: "") + URLEncoder.encode(countryName, "UTF-8")
					+ "&api_key=" + API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (List<LastFMEvent>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//event",
						doc, XPathConstants.NODESET)).getLength();
				events = new ArrayList<LastFMEvent>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					LastFMEvent event = new LastFMEvent();
					event.setTitle((String) xpath.evaluate("string(//event["
							+ i + "]/title)", doc, XPathConstants.STRING));
					final int totalArtistNames = ((NodeList) xpath.evaluate(
							"//event[" + i + "]/artists/artist", doc,
							XPathConstants.NODESET)).getLength();
					for (int j = 1; j <= totalArtistNames; j++) {
						event.addArtistName((String) xpath.evaluate(
								"string(//event[" + i + "]/artists/artist[" + j
										+ "])", doc, XPathConstants.STRING));
					}
					event.setPlaceName((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/name)", doc,
							XPathConstants.STRING));
					event.setPlaceCity((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/location/city)",
							doc, XPathConstants.STRING));
					event.setPlaceCountry((String) xpath
							.evaluate("string(//event[" + i
									+ "]/venue/location/country)", doc,
									XPathConstants.STRING));
					event.setPlaceStreet((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/location/street)",
							doc, XPathConstants.STRING));
					event.setPlaceLatitude((String) xpath.evaluate(
							"string(//event[" + i
									+ "]/venue/location/geo:point/geo:lat)",
							doc, XPathConstants.STRING));
					event.setPlaceLongitude((String) xpath.evaluate(
							"string(//event[" + i
									+ "]/venue/location/geo:point/geo:long)",
							doc, XPathConstants.STRING));
					event.setPlacePageURL((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/website)", doc,
							XPathConstants.STRING));
					event.setPlacePhoneNumber((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/phonenumber)",
							doc, XPathConstants.STRING));
					event.setPlacePictureURL((String) xpath.evaluate(
							"string(//event[" + i
									+ "]/venue/image[@size='medium'])", doc,
							XPathConstants.STRING));
					event.setEventDate((String) xpath.evaluate(
							"string(//event[" + i + "]/startDate)", doc,
							XPathConstants.STRING));
					event.setEventDescription((String) xpath.evaluate(
							"string(//event[" + i + "]/description)", doc,
							XPathConstants.STRING));
					event.setArtistPictureURL((String) xpath.evaluate(
							"string(//event[" + i + "]/image[@size='medium'])",
							doc, XPathConstants.STRING));
					event.setEventPageURL((String) xpath.evaluate(
							"string(//event[" + i + "]/website)", doc,
							XPathConstants.STRING));
					final int totalTags = ((NodeList) xpath.evaluate("//event["
							+ i + "]/tags/tag", doc, XPathConstants.NODESET))
							.getLength();
					for (int j = 1; j <= totalTags; j++) {
						event.addTag((String) xpath.evaluate("string(//event["
								+ i + "]/tags/tag[" + j + "])", doc,
								XPathConstants.STRING));
					}
					events.add(event);
				}
				core.addToCache(urlAddress, events);
			}
		}
		return events;
	}

	/**
	 * 
	 * @param artistName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	public List<LastFMEvent> getEventsForArtist(final String artistName)
			throws 
			XPathExpressionException, UnsupportedEncodingException {
		List<LastFMEvent> events = null;
		if (artistName != null && artistName.trim().length() > 0) {
			final String urlAddress = LASTFM_URL + GET_ARTISTEVENTS
					+ "&artist=" + URLEncoder.encode(artistName, "UTF-8")
					+ "&api_key=" + API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (List<LastFMEvent>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//event",
						doc, XPathConstants.NODESET)).getLength();
				events = new ArrayList<LastFMEvent>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					LastFMEvent event = new LastFMEvent();
					event.setTitle((String) xpath.evaluate("string(//event["
							+ i + "]/title)", doc, XPathConstants.STRING));
					final int totalArtistNames = ((NodeList) xpath.evaluate(
							"//event[" + i + "]/artists/artist", doc,
							XPathConstants.NODESET)).getLength();
					for (int j = 1; j <= totalArtistNames; j++) {
						event.addArtistName((String) xpath.evaluate(
								"string(//event[" + i + "]/artists/artist[" + j
										+ "])", doc, XPathConstants.STRING));
					}
					event.setPlaceName((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/name)", doc,
							XPathConstants.STRING));
					event.setPlaceCity((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/location/city)",
							doc, XPathConstants.STRING));
					event.setPlaceCountry((String) xpath
							.evaluate("string(//event[" + i
									+ "]/venue/location/country)", doc,
									XPathConstants.STRING));
					event.setPlaceStreet((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/location/street)",
							doc, XPathConstants.STRING));
					event.setPlaceLatitude((String) xpath.evaluate(
							"string(//event[" + i
									+ "]/venue/location/geo:point/geo:lat)",
							doc, XPathConstants.STRING));
					event.setPlaceLongitude((String) xpath.evaluate(
							"string(//event[" + i
									+ "]/venue/location/geo:point/geo:long)",
							doc, XPathConstants.STRING));
					event.setPlacePageURL((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/website)", doc,
							XPathConstants.STRING));
					event.setPlacePhoneNumber((String) xpath.evaluate(
							"string(//event[" + i + "]/venue/phonenumber)",
							doc, XPathConstants.STRING));
					event.setPlacePictureURL((String) xpath.evaluate(
							"string(//event[" + i
									+ "]/venue/image[@size='medium'])", doc,
							XPathConstants.STRING));
					event.setEventDate((String) xpath.evaluate(
							"string(//event[" + i + "]/startDate)", doc,
							XPathConstants.STRING));
					event.setEventDescription((String) xpath.evaluate(
							"string(//event[" + i + "]/description)", doc,
							XPathConstants.STRING));
					event.setArtistPictureURL((String) xpath.evaluate(
							"string(//event[" + i + "]/image[@size='medium'])",
							doc, XPathConstants.STRING));
					event.setEventPageURL((String) xpath.evaluate(
							"string(//event[" + i + "]/website)", doc,
							XPathConstants.STRING));
					final int totalTags = ((NodeList) xpath.evaluate("//event["
							+ i + "]/tags/tag", doc, XPathConstants.NODESET))
							.getLength();
					for (int j = 1; j <= totalTags; j++) {
						event.addTag((String) xpath.evaluate("string(//event["
								+ i + "]/tags/tag[" + j + "])", doc,
								XPathConstants.STRING));
					}
					events.add(event);
				}
				core.addToCache(urlAddress, events);
			}
		}
		return events;
	}

	/**
	 * 
	 * @param countryName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public List<LastFMArtist> getTopArtists(final String countryName)
			throws XPathExpressionException, UnsupportedEncodingException {
		List<LastFMArtist> artists = null;
		if (countryName != null && countryName.trim().length() > 0) {
			final String urlAddress = LASTFM_URL + GET_GEOTOPARTISTS
					+ "&country=" + URLEncoder.encode(countryName, "UTF-8")
					+ "&api_key=" + API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (List<LastFMArtist>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//artist",
						doc, XPathConstants.NODESET)).getLength();
				artists = new ArrayList<LastFMArtist>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					LastFMArtist artist = new LastFMArtist();
					artist.setName((String) xpath.evaluate("string(//artist["
							+ i + "]/name)", doc, XPathConstants.STRING));
					artist.setMusicBrainzId((String) xpath.evaluate(
							"string(//artist[" + i + "]/mbid)", doc,
							XPathConstants.STRING));
					artist.setSmallPictureURL((String) xpath.evaluate(
							"string(//artist[" + i + "]/image[@size='small'])",
							doc, XPathConstants.STRING));
					artist.setMediumPictureURL((String) xpath
							.evaluate("string(//artist[" + i
									+ "]/image[@size='medium'])", doc,
									XPathConstants.STRING));
					artist.setLargePictureURL((String) xpath.evaluate(
							"string(//artist[" + i + "]/image[@size='large'])",
							doc, XPathConstants.STRING));
					artists.add(artist);
				}
				core.addToCache(urlAddress, artists);
			}
		}
		return artists;
	}

	/**
	 * 
	 * @param artistName
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	public LastFMArtist getArtistInfo(final String artistName)
			throws XPathExpressionException, UnsupportedEncodingException {
		LastFMArtist artist = null;
		if (artistName != null && artistName.trim().length() > 0) {
			final String urlAddress = LASTFM_URL + GET_ARTISTINFO + "&artist="
					+ URLEncoder.encode(artistName, "UTF-8") + "&api_key="
					+ API_KEY;
			if (core.cacheContains(urlAddress)) {
				return (LastFMArtist) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//artist",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					artist = new LastFMArtist();
					artist.setName((String) xpath.evaluate(
							"string(/lfm/artist/name)", doc,
							XPathConstants.STRING));
					artist.setMusicBrainzId((String) xpath.evaluate(
							"string(/lfm/artist/mbid)", doc,
							XPathConstants.STRING));
					artist.setSmallPictureURL((String) xpath.evaluate(
							"string(/lfm/artist/image[@size='small'])", doc,
							XPathConstants.STRING));
					artist.setMediumPictureURL((String) xpath.evaluate(
							"string(/lfm/artist/image[@size='medium'])", doc,
							XPathConstants.STRING));
					artist.setLargePictureURL((String) xpath.evaluate(
							"string(/lfm/artist/image[@size='large'])", doc,
							XPathConstants.STRING));
					core.addToCache(urlAddress, artist);
				}
			}
		}
		return artist;
	}

}// END OF FILE