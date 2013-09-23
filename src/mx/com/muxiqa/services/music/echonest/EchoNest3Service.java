/**
 * 
 */
package mx.com.muxiqa.services.music.echonest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mx.com.muxiqa.services.core.cache.CacheService;
import mx.com.muxiqa.services.core.http.HttpService;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestArtist;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestAudio;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestBiography;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestBlog;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestImage;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestLink;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestNew;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestReview;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestVideo;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author jerry
 * 
 */
public final class EchoNest3Service {
	//private static final Logger logger = Logger.getLogger(EchoNest3Service.class.getName());
	
	private static final String API_KEY = "ECHONEST_API_KEY";
	private static final String ECHONEST_URL = "http://developer.echonest.com/api/";
	private static final String ECHONEST_ID = "music://id.echonest.com/~/AR/";
	private static final String SEARCH_ARTIST = "search_artists";
	private static final String GET_AUDIO = "get_audio";
	private static final String GET_TOP_HOT_ARTISTS = "get_top_hottt_artists";
	private static final String GET_SIMILAR = "get_similar";
	private static final String GET_IMAGES = "get_images";
	private static final String GET_NEWS = "get_news";
	private static final String GET_VIDEO = "get_video";
	private static final String GET_URLS = "get_urls";
	private static final String GET_REVIEWS = "get_reviews";
	private static final String GET_HOTNESS = "get_hotttnesss";
	private static final String GET_BLOGS = "get_blogs";
	private static final String GET_BIOGRAPHIES = "get_biographies";
	private static final String GET_PROFILE = "get_profile";
	private static final String VERSION = "version=3";

	private XPathFactory xPathFactory;
	private HttpService httpService;
	private CacheService cacheService;

	public EchoNest3Service(final HttpService httpService, final CacheService cacheService) {
		this.httpService = httpService;
		this.cacheService = cacheService;
		xPathFactory = XPathFactory.newInstance();
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestArtist> getArtist(final String name)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		List<EchoNestArtist> artists = null;
		if (name != null && name.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + SEARCH_ARTIST
					+ "?api_key=" + API_KEY + "&query="
					+ URLEncoder.encode(name, "UTF-8") + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestArtist>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//artist",
						doc, XPathConstants.NODESET)).getLength();
				artists = new ArrayList<EchoNestArtist>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestArtist artist = new EchoNestArtist();
					final String id = (String) xpath.evaluate("string(//artist["
							+ i + "]/id)", doc, XPathConstants.STRING);
					artist
							.setId(id.substring(id.lastIndexOf('/') + 1, id
									.length()));
					artist.setName((String) xpath.evaluate("string(//artist[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					artists.add(artist);
				}
				cacheService.put(urlAddress, artists);
			}
		}
		return artists;
	}

	public EchoNestArtist getProfile(final String artistId)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		EchoNestArtist artist = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_PROFILE + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (EchoNestArtist) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				artist = new EchoNestArtist();
				artist.setId((String) xpath.evaluate("string(//artist/id)", doc,
						XPathConstants.STRING));
				artist.setName((String) xpath.evaluate("string(//artist/name)",
						doc, XPathConstants.STRING));
				cacheService.put(urlAddress, artist);
			}
		}
		return artist;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestAudio> getAudioFromArtist(final String artistId,
			final int start, final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestAudio> audios = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_AUDIO + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&start="
					+ start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestAudio>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				audios = new ArrayList<EchoNestAudio>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestAudio audio = new EchoNestAudio();
					audio.setRelease((String) xpath.evaluate("string(//doc[" + i
							+ "]/release)", doc, XPathConstants.STRING));
					audio.setTitle((String) xpath.evaluate("string(//doc[" + i
							+ "]/title)", doc, XPathConstants.STRING));
					audio.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					audio.setLength((String) xpath.evaluate("string(//doc[" + i
							+ "]/length)", doc, XPathConstants.STRING));
					audios.add(audio);
				}
				cacheService.put(urlAddress, audios);
			}
		}
		return audios;
	}

	/**
	 * 
	 * @param rows
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestArtist> getTopHotArtists(final int rows)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		List<EchoNestArtist> artists = null;
		final String urlAddress = ECHONEST_URL + GET_TOP_HOT_ARTISTS
				+ "?api_key=" + API_KEY + "&rows=" + rows + "&" + VERSION;
		if (cacheService.containsKey(urlAddress)) {
			return (List<EchoNestArtist>) cacheService.get(urlAddress);
		}
		final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
		if (doc != null) {
			final XPath xpath = xPathFactory.newXPath();
			final int totalResults = ((NodeList) xpath.evaluate("//artist", doc,
					XPathConstants.NODESET)).getLength();
			artists = new ArrayList<EchoNestArtist>(totalResults);
			for (int i = 1; i <= totalResults; i++) {
				EchoNestArtist artist = new EchoNestArtist();
				final String id = (String) xpath.evaluate("string(//artist[" + i
						+ "]/id)", doc, XPathConstants.STRING);
				artist.setId(id.substring(id.lastIndexOf('/') + 1, id.length()));
				artist.setName((String) xpath.evaluate("string(//artist[" + i
						+ "]/name)", doc, XPathConstants.STRING));
				artist.setHotness((String) xpath.evaluate("string(//artist[" + i
						+ "]/hotttnesss)", doc, XPathConstants.STRING));
				artists.add(artist);
			}
			cacheService.put(urlAddress, artists);
		}
		return artists;
	}

	/**
	 * 
	 * @param artistId
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestArtist> getSimilarArtistsFromArtist(
			final String artistId, int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestArtist> artists = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_SIMILAR + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&rows="
					+ rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestArtist>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final int totalResults = ((NodeList) xpath.evaluate("//artist",
						doc, XPathConstants.NODESET)).getLength();
				artists = new ArrayList<EchoNestArtist>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestArtist artist = new EchoNestArtist();
					final String id = (String) xpath.evaluate("string(//artist["
							+ i + "]/id)", doc, XPathConstants.STRING);
					artist
							.setId(id.substring(id.lastIndexOf('/') + 1, id
									.length()));
					artist.setName((String) xpath.evaluate("string(//artist[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					artists.add(artist);
				}
				cacheService.put(urlAddress, artists);
			}
		}
		return artists;
	}

	/**
	 * 
	 * @param artistId
	 * @param rows
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestImage> getImagesFromArtist(final String artistId,
			final int start, final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestImage> images = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_IMAGES + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&start="
					+ start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestImage>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				images = new ArrayList<EchoNestImage>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestImage image = new EchoNestImage();
					image.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					images.add(image);
				}
				cacheService.put(urlAddress, images);
			}
		}
		return images;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestNew> getNewsFromArtist(final String artistId,
			final int start, final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestNew> news = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_NEWS + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&start="
					+ start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestNew>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				news = new ArrayList<EchoNestNew>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestNew new_ = new EchoNestNew();
					new_.setTitle((String) xpath.evaluate("string(//doc[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					new_.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					new_.setSummary((String) xpath.evaluate("string(//doc[" + i
							+ "]/summary)", doc, XPathConstants.STRING));
					new_.setDatePosted((String) xpath.evaluate("string(//doc[" + i
							+ "]/date_posted)", doc, XPathConstants.STRING));
					news.add(new_);
				}
				cacheService.put(urlAddress, news);
			}
		}
		return news;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestVideo> getVideoFromArtist(final String artistId,
			final int start, final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestVideo> videos = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_VIDEO + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&start="
					+ start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestVideo>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				videos = new ArrayList<EchoNestVideo>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestVideo video = new EchoNestVideo();
					video.setTitle((String) xpath.evaluate("string(//doc[" + i
							+ "]/title)", doc, XPathConstants.STRING));
					video.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					video.setImagePreview((String) xpath.evaluate("string(//doc["
							+ i + "]/image_url)", doc, XPathConstants.STRING));
					videos.add(video);
				}
				cacheService.put(urlAddress, videos);
			}
		}
		return videos;
	}

	/**
	 * 
	 * @param artistId
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public EchoNestLink getOfficialPagesFromArtist(final String artistId)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		EchoNestLink link = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_URLS + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (EchoNestLink) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				link = new EchoNestLink();
				link.setMusicbrainzURL((String) xpath.evaluate("string(//mb_url)",
						doc, XPathConstants.STRING));
				link.setMyspaceURL((String) xpath.evaluate("string(//myspace_url)",
						doc, XPathConstants.STRING));
				link.setLastfmURL((String) xpath.evaluate("string(//lastfm_url)",
						doc, XPathConstants.STRING));
				link.setAolmusicURL((String) xpath.evaluate(
						"string(//aolmusic_url)", doc, XPathConstants.STRING));
				link.setAmazonURL((String) xpath.evaluate("string(//amazon_url)",
						doc, XPathConstants.STRING));
				link.setItunesURL((String) xpath.evaluate("string(//itunes_url)",
						doc, XPathConstants.STRING));
				cacheService.put(urlAddress, link);
			}
		}
		return link;
	}

	/**
	 * 
	 * @param artistId
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public String getHotnessFromArtist(final String artistId)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_HOTNESS + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (String) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				final String hotness = (String) xpath.evaluate("string(//hotttnesss)", doc,
						XPathConstants.STRING);
				cacheService.put(urlAddress, hotness);
				return hotness;
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
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestReview> getReviewsFromArtist(final String artistId,
			final int start, final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestReview> reviews = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_REVIEWS + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&start="
					+ start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestReview>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				reviews = new ArrayList<EchoNestReview>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestReview review = new EchoNestReview();
					review.setTitle((String) xpath.evaluate("string(//doc[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					review.setImageURL((String) xpath.evaluate("string(//doc[" + i
							+ "]/image_url)", doc, XPathConstants.STRING));
					review.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					review.setSummary((String) xpath.evaluate("string(//doc[" + i
							+ "]/summary)", doc, XPathConstants.STRING));
					review.setRelease((String) xpath.evaluate("string(//doc[" + i
							+ "]/release)", doc, XPathConstants.STRING));
					review.setDateReviewed((String) xpath.evaluate("string(//doc["
							+ i + "]/date_reviewed)", doc, XPathConstants.STRING));
					reviews.add(review);
				}
				cacheService.put(urlAddress, reviews);
			}
		}
		return reviews;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestBlog> getBlogsFromArtist(final String artistId,
			final int start, final int rows) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		List<EchoNestBlog> blogs = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_BLOGS + "?api_key="
					+ API_KEY + "&id=" + ECHONEST_ID + artistId + "&start="
					+ start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestBlog>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				blogs = new ArrayList<EchoNestBlog>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestBlog blog = new EchoNestBlog();
					blog.setTitle((String) xpath.evaluate("string(//doc[" + i
							+ "]/name)", doc, XPathConstants.STRING));
					blog.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					blog.setSummary((String) xpath.evaluate("string(//doc[" + i
							+ "]/summary)", doc, XPathConstants.STRING));
					blog.setDatePosted((String) xpath.evaluate("string(//doc[" + i
							+ "]/date_posted)", doc, XPathConstants.STRING));
					blogs.add(blog);
				}
				cacheService.put(urlAddress, blogs);
			}
		}
		return blogs;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	public List<EchoNestBiography> getBiographyFromArtist(
			final String artistId, final int start, final int rows)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		List<EchoNestBiography> biographies = null;
		if (artistId != null && artistId.trim().length() > 0) {
			final String urlAddress = ECHONEST_URL + GET_BIOGRAPHIES
					+ "?api_key=" + API_KEY + "&id=" + ECHONEST_ID + artistId
					+ "&start=" + start + "&rows=" + rows + "&" + VERSION;
			if (cacheService.containsKey(urlAddress)) {
				return (List<EchoNestBiography>) cacheService.get(urlAddress);
			}
			final Document doc = httpService.getContentAsDocumentFrom(urlAddress);
			if (doc != null) {
				final XPath xpath = xPathFactory.newXPath();
				/*
				 * int resultsFound =
				 * Integer.parseInt((String)xpath.evaluate("string(//results/@found)"
				 * , doc, XPathConstants.STRING)); System.out.println(resultsFound);
				 */
				final int totalResults = ((NodeList) xpath.evaluate("//doc", doc,
						XPathConstants.NODESET)).getLength();
				biographies = new ArrayList<EchoNestBiography>(totalResults);
				for (int i = 1; i <= totalResults; i++) {
					EchoNestBiography bio = new EchoNestBiography();
					bio.setSummary((String) xpath.evaluate("string(//doc[" + i
							+ "]/text)", doc, XPathConstants.STRING));
					bio.setUrl((String) xpath.evaluate("string(//doc[" + i
							+ "]/url)", doc, XPathConstants.STRING));
					biographies.add(bio);
				}
				cacheService.put(urlAddress, biographies);
			}
		}
		return biographies;
	}

}// END OF FILE