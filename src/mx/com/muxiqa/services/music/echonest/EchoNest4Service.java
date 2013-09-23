/**
 * 
 */
package mx.com.muxiqa.services.music.echonest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestArtist;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestAudio;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestImage;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestLink;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestNew;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestReview;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestTag;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestVideo;
import mx.com.muxiqa.services.music.echonest.entities.EchonestArtistAudio;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author gerardomartinezgil
 * 
 */
public final class EchoNest4Service {
	// core
	private static final String KEY = "ECHONEST_API_KEY";
	private static final String API_KEY = "?api_key=" + KEY;
	private static final String ECHONEST_URL = "http://developer.echonest.com/api/v4";
	private static final String PROVIDER = "&bucket=";
	private static final String _7DIGITAL_PROVIDER = "id:7digital";
	// private static final String _PLAYME_PROVIDER = "id:playme";
	// private static final String _MUSICBRAINZ_PROVIDER = "id:musicbrainz";
	private static final String XML_OUTPUT_FORMAT = "&format=xml";
	private static final String NUMBER_OF_RESULTS = "&results=";
	private static final String BEGIN_ON = "&start=";
	private static final String SORT_BY = "&sort=";
	// private static final String HOTNESS_ASC_SORT_TYPE = "hotttnesss-asc";
	private static final String SONG_HOTNESS_DES_SORT_TYPE = "song_hotttnesss-desc";
	// private static final String FAMILIARITY_ASC_SORT_TYPE =
	// "familiarity-asc";
	private static final String FAMILIARITY_DES_SORT_TYPE = "familiarity-desc";
	private static final String WEIGHT_SORT_TYPE = "weight";
	// private static final String FREQUENCY_SORT_TYPE = "frequency";
	// private static final String INCLUDE_AUDIO = "&bucket=audio";
	// private static final String INCLUDE_BIO = "&bucket=biographies";
	// private static final String INCLUDE_BLOG = "&bucket=blogs";
	// private static final String INCLUDE_FAMILIARITY = "&bucket=familiarity";
	// private static final String INCLUDE_IMAGES = "&bucket=images";
	// private static final String INCLUDE_HOTNESS = "&bucket=hotttnesss";
	// private static final String INCLUDE_NEWS = "&bucket=news";
	// private static final String INCLUDE_REVIEWS = "&bucket=reviews";
	// private static final String INCLUDE_TERMS = "&bucket=terms";
	// private static final String INCLUDE_URLS = "&bucket=urls";
	// private static final String INCLUDE_VIDEOS = "&bucket=video";
	private static final String INCLUDE_TRACKS = "&bucket=tracks";
	private static final String INCLUDE_SONG_HOTNESS = "&bucket=song_hotttnesss";
	// commands
	private static final String SEARCH_ARTIST_CMD = "/artist/search";
	private static final String SEARCH_AUDIO_CMD = "/song/search";
	private static final String GET_ARTIST_AUDIOS_CMD = "/artist/audio";
	private static final String GET_ARTISTS_TOP_HOT_CMD = "/artist/top_hottt";
	private static final String GET_ARTISTS_SIMILAR_CMD = "/artist/similar";
	private static final String GET_ARTIST_TAGS_CMD = "/artist/terms";
	private static final String GET_TAGS_TOP_CMD = "/artist/top_terms";
	private static final String GET_ARTIST_IMAGES_CMD = "/artist/images";
	private static final String GET_ARTIST_NEWS_CMD = "/artist/news";
	private static final String GET_ARTIST_REVIEWS_CMD = "/artist/reviews";
	private static final String GET_ARTIST_OFFICIAL_SITES_CMD = "/artist/url";
	private static final String GET_ARTIST_VIDEOS_CMD = "/artist/video";
	private static final String GET_PLAYLIST_STATIC_CMD = "/playlist/static";
	private static final String GET_PLAYLIST_DYNAMIC_CMD = "/playlist/dynamic";
	private static final String GET_AUDIO_PROFILE = "/song/profile";
	// parameters
	private static final String NAME_PARAM = "&name=";
	private static final String ID_PARAM = "&id=";
	private static final String TITLE_PARAM = "&title=";
	private static final String ARTIST_PARAM = "&artist=";
	private static final String ARTIST_ID_PARAM = "&artist_id=";
	private static final String COMBINED_PARAM = "&combined=";
	private static final String SONG_ID_PARAM = "&song_id=";
	private static final String SESSION_ID_PARAM = "&session_id=";
	// private static final String ADDITIONAL_PARAMETER = "&description=";
	private static final String USE_LICENSE = "&license=";
	// private static final String ECHO_SOURCE_LICENSE = "echo-source";
	// private static final String CC_BY_LICENSE = "cc-by";
	private static final String CC_BY_SA_LICENSE = "cc-by-sa";
	// private static final String PUBLIC_DOMAIN_LICENSE = "public-domain";
	private static final String USE_HIGH_RELEVANCE_RESULTS = "&high_relevance=true";
	private static final String PLAYLIST_TYPE = "&type=";
	private static final String ARTIST_PLAYLIST_TYPE = "artist";
	// private static final String ARTIST_RADIO_PLAYLIST_TYPE = "artist-radio";
	// private static final String ARTIST_DESCRIPTION_PLAYLIST_TYPE =
	// "artist-description";
	private static final String SONG_RADIO_PLAYLIST_TYPE = "song-radio";
	private static final String PICK_ARTIST_PLAYLIST_BY = "artist_pick";

	public static final Logger logger = Logger.getLogger(EchoNest4Service.class
			.getName());

	private CoreService core;
	private XPathFactory xPathFactory;

	/**
	 * 
	 * @param core
	 */
	public EchoNest4Service(final CoreService core) {
		this.core = core;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param query
	 * @param start
	 * @param rows
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchonestArtistAudio> search(final String query, // main search
			// method of the
			// wall
			final int start, final int rows)
			throws UnsupportedEncodingException, XPathExpressionException {
		final String urlAddress = ECHONEST_URL
				+ SEARCH_AUDIO_CMD
				+ API_KEY
				+ XML_OUTPUT_FORMAT
				+ ((query != null && query.trim().length() > 0) ? COMBINED_PARAM
						+ URLEncoder.encode(query, "UTF-8")
						: "") + ((rows > 1) ? NUMBER_OF_RESULTS + rows : "")
				+ BEGIN_ON + start + PROVIDER + _7DIGITAL_PROVIDER
				+ INCLUDE_TRACKS + INCLUDE_SONG_HOTNESS + SORT_BY
				+ SONG_HOTNESS_DES_SORT_TYPE;
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (List<EchonestArtistAudio>) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//song", doc,
					XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchonestArtistAudio> audios = new ArrayList<EchonestArtistAudio>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchonestArtistAudio artistAudio = new EchonestArtistAudio();
					artistAudio.setAudioTitle((String) xpath.evaluate(
							"string(//song[" + i + "]/title)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistName((String) xpath.evaluate(
							"string(//song[" + i + "]/artist_name)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioAlbumImageURL((String) xpath.evaluate(
							"string(//song[" + i
									+ "]/tracks/track[1]/release_image)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioPreviewURL((String) xpath.evaluate(
							"string(//song[" + i
									+ "]/tracks/track[1]/preview_url)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioHotness((String) xpath.evaluate(
							"string(//song[" + i + "]/song_hotttnesss)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistId((String) xpath.evaluate(
							"string(//song[" + i + "]/artist_id)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioId((String) xpath.evaluate(
							"string(//song[" + i + "]/id)", doc,
							XPathConstants.STRING));
					audios.add(artistAudio);
				}
				core.addToCache(urlAddress, audios);
				return audios;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param name
	 * @param rows
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	// TODO:on the client side verify that it doesn't contain extra characters
	// the
	// input...
	@SuppressWarnings("unchecked")
	public List<EchoNestArtist> searchForArtist(final String name,// to be used
			// on the
			// search in
			// the wall
			// of the
			// artists...
			final int rows) throws UnsupportedEncodingException,
			XPathExpressionException {
		if (name != null && name.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + SEARCH_ARTIST_CMD
					+ API_KEY + XML_OUTPUT_FORMAT
					+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + PROVIDER
					+ _7DIGITAL_PROVIDER /* + INCLUDE_AUDIO + INCLUDE_IMAGES */
					+ NAME_PARAM + URLEncoder.encode(name, "UTF-8") + SORT_BY
					+ FAMILIARITY_DES_SORT_TYPE;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (List<EchoNestArtist>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//artist",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					List<EchoNestArtist> artists = new ArrayList<EchoNestArtist>(
							totalResults);
					for (int i = 1; i <= totalResults; i++) {
						EchoNestArtist artist = new EchoNestArtist();
						artist.setId((String) xpath.evaluate("string(//artist["
								+ i + "]/id)", doc, XPathConstants.STRING));
						artist.setName((String) xpath.evaluate(
								"string(//artist[" + i + "]/name)", doc,
								XPathConstants.STRING));
						artists.add(artist);
					}
					core.addToCache(urlAddress, artists);
					return artists;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param artistName
	 * @param songName
	 * @param start
	 * @param rows
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchonestArtistAudio> searchForAudio(
			// used by the option of search music of this artist audio...
			final String artistId, final String artistName,
			final String songName, final int start, final int rows)
			throws UnsupportedEncodingException, XPathExpressionException {
		final String urlAddress = ECHONEST_URL
				+ SEARCH_AUDIO_CMD
				+ API_KEY
				+ XML_OUTPUT_FORMAT
				+ ((songName != null && songName.trim().length() > 0) ? TITLE_PARAM
						+ URLEncoder.encode(songName, "UTF-8")
						: "")
				+ ((artistName != null && artistName.trim().length() > 0) ? ARTIST_PARAM
						+ URLEncoder.encode(artistName, "UTF-8")
						: "")
				+ ((artistId != null && artistId.trim().length() > 0) ? ARTIST_ID_PARAM
						+ artistId
						: "") + ((rows > 1) ? NUMBER_OF_RESULTS + rows : "")
				+ BEGIN_ON + start + PROVIDER + _7DIGITAL_PROVIDER
				+ INCLUDE_TRACKS + INCLUDE_SONG_HOTNESS + SORT_BY
				+ SONG_HOTNESS_DES_SORT_TYPE;
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (List<EchonestArtistAudio>) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//song", doc,
					XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchonestArtistAudio> audios = new ArrayList<EchonestArtistAudio>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchonestArtistAudio artistAudio = new EchonestArtistAudio();
					artistAudio.setAudioTitle((String) xpath.evaluate(
							"string(//song[" + i + "]/title)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistName((String) xpath.evaluate(
							"string(//song[" + i + "]/artist_name)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioAlbumImageURL((String) xpath.evaluate(
							"string(//song[" + i
									+ "]/tracks/track[1]/release_image)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioPreviewURL((String) xpath.evaluate(
							"string(//song[" + i
									+ "]/tracks/track[1]/preview_url)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioHotness((String) xpath.evaluate(
							"string(//song[" + i + "]/song_hotttnesss)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistId((String) xpath.evaluate(
							"string(//song[" + i + "]/artist_id)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioId((String) xpath.evaluate(
							"string(//song[" + i + "]/id)", doc,
							XPathConstants.STRING));
					audios.add(artistAudio);
				}
				core.addToCache(urlAddress, audios);
				return audios;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<EchoNestAudio> getAudiosFromArtist(final String artistId,// use
			// the
			// searchForAudio
			// instead
			// with
			// no
			// songName
			// parameter...
			final int start, final int rows) throws XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_ARTIST_AUDIOS_CMD
					+ API_KEY + ID_PARAM + artistId + XML_OUTPUT_FORMAT
					+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
					+ start;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (List<EchoNestAudio>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//audio/audio", doc, XPathConstants.NODESET))
						.getLength();
				if (totalResults > 0) {
					List<EchoNestAudio> audios = new ArrayList<EchoNestAudio>(
							totalResults);
					for (int i = 1; i <= totalResults; i++) {
						EchoNestAudio audio = new EchoNestAudio();
						audio.setTitle((String) xpath.evaluate(
								"string(//audio/audio[" + i + "]/title)", doc,
								XPathConstants.STRING));
						audio.setUrl((String) xpath.evaluate(
								"string(//audio/audio[" + i + "]/url)", doc,
								XPathConstants.STRING));
						audio.setLength((String) xpath.evaluate(
								"string(//audio/audio[" + i + "]/length)", doc,
								XPathConstants.STRING));
						audio.setRelease((String) xpath.evaluate(
								"string(//audio/audio[" + i + "]/release)",
								doc, XPathConstants.STRING));
						audio.setId((String) xpath.evaluate(
								"string(//audio/audio[" + i + "]/id)", doc,
								XPathConstants.STRING));
						audios.add(audio);
					}
					core.addToCache(urlAddress, audios);
					return audios;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestArtist> getTopHotArtists(final int start, final int rows) // for
			// being
			// use
			// in
			// the
			// wall
			// of
			// artist
			// option
			// in
			// the
			// client...
			throws XPathExpressionException {
		final String urlAddress = ECHONEST_URL + GET_ARTISTS_TOP_HOT_CMD
				+ API_KEY + XML_OUTPUT_FORMAT
				+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
				+ start + PROVIDER + _7DIGITAL_PROVIDER;
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (List<EchoNestArtist>) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//artist",
					doc, XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchoNestArtist> artists = new ArrayList<EchoNestArtist>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestArtist artist = new EchoNestArtist();
					artist.setId((String) xpath.evaluate("string(//artist[" + i
							+ "]/id)", doc, XPathConstants.STRING));
					artist.setName((String) xpath.evaluate("string(//artist["
							+ i + "]/name)", doc, XPathConstants.STRING));
					artists.add(artist);
				}
				core.addToCache(urlAddress, artists);
				return artists;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestArtist> getSimilarArtistsToThisOne(// for being use in
			// the play lists
			// view of your
			// current songs...
			final String artistId, final int start, final int rows)
			throws XPathExpressionException {
		final String urlAddress = ECHONEST_URL + GET_ARTISTS_SIMILAR_CMD
				+ API_KEY + ID_PARAM + artistId + XML_OUTPUT_FORMAT
				+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
				+ start + PROVIDER + _7DIGITAL_PROVIDER + SORT_BY
				+ FAMILIARITY_DES_SORT_TYPE;
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (List<EchoNestArtist>) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//artist",
					doc, XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchoNestArtist> artists = new ArrayList<EchoNestArtist>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestArtist artist = new EchoNestArtist();
					artist.setId((String) xpath.evaluate("string(//artist[" + i
							+ "]/id)", doc, XPathConstants.STRING));
					artist.setName((String) xpath.evaluate("string(//artist["
							+ i + "]/name)", doc, XPathConstants.STRING));
					artists.add(artist);
				}
				core.addToCache(urlAddress, artists);
				return artists;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestTag> getTagsFromArtist(final String artistId)// option
			// to
			// see the
			// tags of
			// the
			// artist
			// and
			// navigate
			// to other
			// artists
			// with this
			// tag
			throws XPathExpressionException {
		final String urlAddress = ECHONEST_URL + GET_ARTIST_TAGS_CMD + API_KEY
				+ ID_PARAM + artistId + XML_OUTPUT_FORMAT + SORT_BY
				+ WEIGHT_SORT_TYPE;
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (List<EchoNestTag>) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//terms", doc,
					XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchoNestTag> tags = new ArrayList<EchoNestTag>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestTag tag = new EchoNestTag();
					tag.setTag((String) xpath.evaluate("string(//terms[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					tag.setWeigth((String) xpath.evaluate("string(//terms[" + i
							+ "]/weight)", doc, XPathConstants.STRING));
					tag.setFrecuency((String) xpath.evaluate("string(//terms["
							+ i + "]/frequency)", doc, XPathConstants.STRING));
					tags.add(tag);
				}
				core.addToCache(urlAddress, tags);
				return tags;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestTag> getTopTags(final int rows) // for use in the cloud
			// tag view option
			throws XPathExpressionException {
		final String urlAddress = ECHONEST_URL + GET_TAGS_TOP_CMD + API_KEY
				+ XML_OUTPUT_FORMAT
				+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "");
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (List<EchoNestTag>) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//terms", doc,
					XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchoNestTag> tags = new ArrayList<EchoNestTag>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestTag tag = new EchoNestTag();
					tag.setTag((String) xpath.evaluate("string(//terms[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					tag.setFrecuency((String) xpath.evaluate("string(//terms["
							+ i + "]/frequency)", doc, XPathConstants.STRING));
					tags.add(tag);
				}
				core.addToCache(urlAddress, tags);
				return tags;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestImage> getImagesFromArtist(final String artistId,// to
			// show
			// when
			// playing
			// artist
			// (screensaver)
			final int start, final int rows) throws XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_ARTIST_IMAGES_CMD
					+ API_KEY + ID_PARAM + artistId + XML_OUTPUT_FORMAT
					+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
					+ start + USE_LICENSE + CC_BY_SA_LICENSE;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (List<EchoNestImage>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//image",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					List<EchoNestImage> images = new ArrayList<EchoNestImage>(
							totalResults);
					for (int i = 1; i <= totalResults; i++) {
						EchoNestImage image = new EchoNestImage();
						image.setUrl((String) xpath.evaluate("string(//image["
								+ i + "]/url)", doc, XPathConstants.STRING));
						image.setLicense((String) xpath.evaluate(
								"string(//image[" + i + "]/license/type)", doc,
								XPathConstants.STRING));
						images.add(image);
					}
					core.addToCache(urlAddress, images);
					return images;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestNew> getNewsFromArtist(final String artistId,// news
			// option
			// in
			// artist
			// view
			final int start, final int rows) throws XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_ARTIST_NEWS_CMD
					+ API_KEY + ID_PARAM + artistId + XML_OUTPUT_FORMAT
					+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
					+ start + USE_HIGH_RELEVANCE_RESULTS;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (List<EchoNestNew>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate(
						"//news/news", doc, XPathConstants.NODESET))
						.getLength();
				if (totalResults > 0) {
					List<EchoNestNew> news = new ArrayList<EchoNestNew>(
							totalResults);
					for (int i = 1; i <= totalResults; i++) {
						EchoNestNew new_ = new EchoNestNew();
						new_.setId((String) xpath.evaluate(
								"string(//news/news[" + i + "]/id)", doc,
								XPathConstants.STRING));
						new_.setTitle((String) xpath.evaluate(
								"string(//news/news[" + i + "]/name)", doc,
								XPathConstants.STRING));
						new_.setUrl((String) xpath.evaluate(
								"string(//news/news[" + i + "]/url)", doc,
								XPathConstants.STRING));
						new_.setSummary((String) xpath.evaluate(
								"string(//news/news[" + i + "]/summary)", doc,
								XPathConstants.STRING));
						new_.setDatePosted((String) xpath.evaluate(
								"string(//news/news[" + i + "]/date_posted)",
								doc, XPathConstants.STRING));
						news.add(new_);
					}
					core.addToCache(urlAddress, news);
					return news;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestReview> getReviewsFromArtist(final String artistId,
			final int start, final int rows) throws XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_ARTIST_REVIEWS_CMD
					+ API_KEY + ID_PARAM + artistId + XML_OUTPUT_FORMAT
					+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
					+ start;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (List<EchoNestReview>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//review",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					List<EchoNestReview> reviews = new ArrayList<EchoNestReview>(
							totalResults);
					for (int i = 1; i <= totalResults; i++) {
						EchoNestReview review = new EchoNestReview();
						review.setId((String) xpath.evaluate("string(//review["
								+ i + "]/id)", doc, XPathConstants.STRING));
						review.setTitle((String) xpath.evaluate(
								"string(//review[" + i + "]/name)", doc,
								XPathConstants.STRING));
						review.setUrl((String) xpath.evaluate(
								"string(//review[" + i + "]/url)", doc,
								XPathConstants.STRING));
						review.setSummary((String) xpath.evaluate(
								"string(//review[" + i + "]/summary)", doc,
								XPathConstants.STRING));
						review.setRelease((String) xpath.evaluate(
								"string(//review[" + i + "]/release)", doc,
								XPathConstants.STRING));
						review.setDateReviewed((String) xpath.evaluate(
								"string(//review[" + i + "]/date_found)", doc,
								XPathConstants.STRING));
						reviews.add(review);
					}
					core.addToCache(urlAddress, reviews);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestVideo> getVideosInsideWebPagesFromArtist(
			final String artistId, final int start, final int rows)
			throws XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_ARTIST_VIDEOS_CMD
					+ API_KEY + ID_PARAM + artistId + XML_OUTPUT_FORMAT
					+ ((rows > 1) ? NUMBER_OF_RESULTS + rows : "") + BEGIN_ON
					+ start;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (List<EchoNestVideo>) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//video",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					List<EchoNestVideo> videos = new ArrayList<EchoNestVideo>(
							totalResults);
					for (int i = 1; i <= totalResults; i++) {
						EchoNestVideo video = new EchoNestVideo();
						video.setId((String) xpath.evaluate("string(//video["
								+ i + "]/id)", doc, XPathConstants.STRING));
						video.setTitle((String) xpath.evaluate(
								"string(//video[" + i + "]/title)", doc,
								XPathConstants.STRING));
						video.setUrl((String) xpath.evaluate("string(//video["
								+ i + "]/url)", doc, XPathConstants.STRING));
						video.setImagePreview((String) xpath.evaluate(
								"string(//video[" + i + "]/image_url)", doc,
								XPathConstants.STRING));
						video.setSite((String) xpath.evaluate("string(//video["
								+ i + "]/site)", doc, XPathConstants.STRING));
						video.setDateFound((String) xpath.evaluate(
								"string(//video[" + i + "]/date_found)", doc,
								XPathConstants.STRING));
						videos.add(video);
					}
					core.addToCache(urlAddress, videos);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @return
	 * @throws XPathExpressionException
	 */
	public EchoNestLink getOfficialSitesFromArtist(final String artistId)
			throws XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL
					+ GET_ARTIST_OFFICIAL_SITES_CMD + API_KEY + ID_PARAM
					+ artistId + XML_OUTPUT_FORMAT;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (EchoNestLink) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//urls",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					EchoNestLink link = new EchoNestLink();
					link.setMusicbrainzURL((String) xpath.evaluate(
							"string(//mb_url)", doc, XPathConstants.STRING));
					link.setMyspaceURL((String) xpath
							.evaluate("string(//myspace_url)", doc,
									XPathConstants.STRING));
					link
							.setLastfmURL((String) xpath.evaluate(
									"string(//lastfm_url)", doc,
									XPathConstants.STRING));
					link.setAolmusicURL((String) xpath.evaluate(
							"string(//aolmusic_url)", doc,
							XPathConstants.STRING));
					link
							.setAmazonURL((String) xpath.evaluate(
									"string(//amazon_url)", doc,
									XPathConstants.STRING));
					link
							.setItunesURL((String) xpath.evaluate(
									"string(//itunes_url)", doc,
									XPathConstants.STRING));
					link.setAolmusicURL((String) xpath.evaluate(
							"string(//aolmusic_url)", doc,
							XPathConstants.STRING));
					core.addToCache(urlAddress, link);
					return link;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistIds
	 * @param artistNames
	 * @param songIds
	 * @param start
	 * @param rows
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchonestArtistAudio> getPlayListFromCollection(
			// this is only one time for generating a play list of device music
			final String[] artistIds, final String[] artistNames,
			final String[] songIds, final int start, final int rows)
			throws UnsupportedEncodingException, XPathExpressionException {
		StringBuffer url = new StringBuffer();
		url.append(ECHONEST_URL);
		url.append(GET_PLAYLIST_STATIC_CMD);
		url.append(API_KEY);
		if (artistIds != null && artistIds.length > 0) {
			for (String id : artistIds) {
				url.append(ARTIST_ID_PARAM);
				url.append(id);
			}
			url.append(PLAYLIST_TYPE);
			url.append(ARTIST_PLAYLIST_TYPE);
		} else if (artistNames != null && artistNames.length > 0) {
			for (String name : artistNames) {
				url.append(ARTIST_PARAM);
				url.append(URLEncoder.encode(name, "UTF-8"));
			}
			url.append(PLAYLIST_TYPE);
			url.append(ARTIST_PLAYLIST_TYPE);
		} else if (songIds != null && songIds.length > 0) {
			for (String id : songIds) {
				url.append(SONG_ID_PARAM);
				url.append(id);
			}
			url.append(PLAYLIST_TYPE);
			url.append(SONG_RADIO_PLAYLIST_TYPE);
		}
		url.append(PICK_ARTIST_PLAYLIST_BY);
		url.append(SONG_HOTNESS_DES_SORT_TYPE);
		url.append(XML_OUTPUT_FORMAT);
		url.append(((rows > 1) ? NUMBER_OF_RESULTS + rows : ""));
		url.append(BEGIN_ON);
		url.append(start);
		logger.warning("urlAddress = " + url.toString());
		if (core.cacheContains(url.toString())) {
			return (List<EchonestArtistAudio>) core
					.getFromCache(url.toString());
		}
		final Document doc = core.getHTTPContentAsXMLDocument(url.toString());
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//song", doc,
					XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				List<EchonestArtistAudio> audios = new ArrayList<EchonestArtistAudio>(
						totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchonestArtistAudio artistAudio = new EchonestArtistAudio();
					artistAudio.setAudioTitle((String) xpath.evaluate(
							"string(//song[" + i + "]/title)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistName((String) xpath.evaluate(
							"string(//song[" + i + "]/artist_name)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistId((String) xpath.evaluate(
							"string(//song[" + i + "]/artist_id)", doc,
							XPathConstants.STRING));
					artistAudio.setAudioId((String) xpath.evaluate(
							"string(//song[" + i + "]/id)", doc,
							XPathConstants.STRING));
					audios.add(artistAudio);
				}
				core.addToCache(url.toString(), audios);
				return audios;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param artistName
	 * @param songId
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 */
	// TODO:the session it's killed after 4 hours...so in the client take in
	// account that time...
	public EchonestArtistAudio getPlayListFromPlayingSong(
			// only by request
			// from a next song
			// suggested by...
			final String artistId, final String artistName,
			final String songId, final String sessionId)
			throws XPathExpressionException, UnsupportedEncodingException {
		final String urlAddress = ECHONEST_URL
				+ GET_PLAYLIST_DYNAMIC_CMD
				+ API_KEY
				+ ((artistId != null && artistId.trim().length() > 0) ? ARTIST_ID_PARAM
						+ artistId
						: "")
				+ ((artistName != null && artistName.trim().length() > 0) ? ARTIST_PARAM
						+ URLEncoder.encode(artistName, "UTF-8")
						: "")
				+ ((songId != null && songId.trim().length() > 0) ? SONG_ID_PARAM
						+ songId
						: "")
				+ ((sessionId != null && sessionId.trim().length() > 0) ? SESSION_ID_PARAM
						+ sessionId
						: "") + XML_OUTPUT_FORMAT;
		logger.warning("urlAddress = " + urlAddress);
		if (core.cacheContains(urlAddress)) {
			return (EchonestArtistAudio) core.getFromCache(urlAddress);
		}
		final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//song", doc,
					XPathConstants.NODESET)).getLength();
			if (totalResults > 0) {
				EchonestArtistAudio artistAudio = new EchonestArtistAudio();
				artistAudio.setAudioTitle((String) xpath.evaluate(
						"string(//song[1]/title)", doc, XPathConstants.STRING));
				artistAudio.setArtistName((String) xpath.evaluate(
						"string(//song[1]/artist_name)", doc,
						XPathConstants.STRING));
				artistAudio.setArtistId((String) xpath.evaluate(
						"string(//song[1]/artist_id)", doc,
						XPathConstants.STRING));
				artistAudio.setAudioId((String) xpath.evaluate(
						"string(//song[1]/id)", doc, XPathConstants.STRING));
				core.addToCache(urlAddress, artistAudio);
				return artistAudio;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param songId
	 * @return
	 * @throws XPathExpressionException
	 */
	public EchonestArtistAudio getSongInfo(final String songId)
			throws XPathExpressionException {
		if (songId != null && songId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_AUDIO_PROFILE
					+ API_KEY + ID_PARAM + songId + XML_OUTPUT_FORMAT
					+ INCLUDE_TRACKS + INCLUDE_SONG_HOTNESS;
			logger.warning("urlAddress = " + urlAddress);
			if (core.cacheContains(urlAddress)) {
				return (EchonestArtistAudio) core.getFromCache(urlAddress);
			}
			final Document doc = core.getHTTPContentAsXMLDocument(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//song",
						doc, XPathConstants.NODESET)).getLength();
				if (totalResults > 0) {
					EchonestArtistAudio artistAudio = new EchonestArtistAudio();
					artistAudio.setAudioTitle((String) xpath.evaluate(
							"string(//song[1]/title)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistName((String) xpath.evaluate(
							"string(//song[1]/artist_name)", doc,
							XPathConstants.STRING));
					artistAudio.setArtistId((String) xpath.evaluate(
							"string(//song[1]/artist_id)", doc,
							XPathConstants.STRING));
					artistAudio
							.setAudioId((String) xpath.evaluate(
									"string(//song[1]/id)", doc,
									XPathConstants.STRING));
					artistAudio.setAudioAlbumImageURL((String) xpath.evaluate(
							"string(//song[1]/tracks/track[1]/release_image)",
							doc, XPathConstants.STRING));
					artistAudio.setAudioPreviewURL((String) xpath.evaluate(
							"string(//song[1]/tracks/track[1]/preview_url)",
							doc, XPathConstants.STRING));
					artistAudio.setAudioHotness((String) xpath.evaluate(
							"string(//song[1]/song_hotttnesss)", doc,
							XPathConstants.STRING));
					core.addToCache(urlAddress, artistAudio);
					return artistAudio;
				}
			}
		}
		return null;
	}

}// END OF FILE