package mx.com.muxiqa;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.muxiqa.services.MuxiqaService;
import mx.com.muxiqa.util.Constants;

@SuppressWarnings("serial")
public class MuxiqaServlet extends HttpServlet {
	public static final Logger logger = Logger
	.getLogger(MuxiqaServlet.class.getName());
	
	// the service where we connect our services...
	private MuxiqaService service;
	// cache...
	private static final int CACHE_DURATION_IN_SECONDS = 60 * 60 * 1 /*1 HOUR*//*  * 24 * 2 //TWO DAYS*/;
	private static final long CACHE_DURATION_IN_MILISECONDS = CACHE_DURATION_IN_SECONDS * 1000;

	@Override
	public void init() throws ServletException {
		super.init();
		service = new MuxiqaService();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//response type...
		resp.setContentType("text/x-json;charset=UTF-8");
		//statistics...
		final long startTime = System.currentTimeMillis();
		// ********************CACHE********************
		makeUseOfCache(resp);
		// *********************CLIENT**********************
		// IP
		final String ipAddress = getClientIPAddress(req);
		logger.log(Level.WARNING, "CLIENT IP ADDRESS = "+ipAddress);
		// UA
		final String userAgent = getUserAgent(req);
		logger.log(Level.WARNING, "USER AGENT = "+userAgent);
		// *********************COMMANDS********************
		//requests
		final String cmdRegisterDevice = req.getParameter(Constants.REGISTER_DEVICE_CMD);//first time only, if not already registered
		final String cmdGetTheWall = req.getParameter(Constants.GET_THE_WALL_CMD);
		final String cmdSearch = req.getParameter(Constants.SEARCH_CMD);
		final String cmdSearchAudio = req.getParameter(Constants.SEARCH_AUDIO_CMD);
		final String cmdSearchArtist = req.getParameter(Constants.SEARCH_ARTIST_CMD);
		final String cmdGetArtistInfo = req.getParameter(Constants.GET_ARTIST_INFO_CMD);
		final String cmdGetAudiosFromArtist = req.getParameter(Constants.GET_AUDIOS_FROM_ARTIST_CMD);
		final String cmdGetAudioInfo = req.getParameter(Constants.GET_AUDIO_INFO_CMD);//gets also the lyrics...
		final String cmdPostToTheWall = req.getParameter(Constants.POST_TO_WALL_CMD);//can post all kind of events...
		final String cmdGetSimilarArtistsFromArtist = req.getParameter(Constants.GET_SIMILAR_ARTISTS_FROM_ARTIST_CMD);
		final String cmdGetVideosFromArtist = req.getParameter(Constants.GET_VIDEOS_FROM_ARTIST_CMD);
		final String cmdGetVideoInfo = req.getParameter(Constants.GET_VIDEO_INFO_CMD);
		final String cmdGetEventsFromArtist = req.getParameter(Constants.GET_EVENTS_FROM_ARTIST_CMD);
		final String cmdGetEventInfo = req.getParameter(Constants.GET_EVENT_INFO_CMD);//shows also the map to arrive to such destination...
		final String cmdGetNewsFromArtist = req.getParameter(Constants.GET_NEWS_FROM_ARTIST_CMD);
		final String cmdGetNewInfo = req.getParameter(Constants.GET_NEW_INFO_CMD);
		final String cmdGetImagesFromArtist = req.getParameter(Constants.GET_IMAGES_FROM_ARTIST_CMD);
		final String cmdGetImageInfo = req.getParameter(Constants.GET_IMAGE_INFO_CMD);
		final String cmdGetReviewsFromArtist = req.getParameter(Constants.GET_REVIEWS_FROM_ARTIST_CMD);
		final String cmdGetReviewInfo = req.getParameter(Constants.GET_REVIEW_INFO_CMD);
		final String cmdGetOfficialPagesFromArtist = req.getParameter(Constants.GET_OFFICIAL_PAGES_FROM_ARTIST_CMD);//in the client open the browser when looking at the detail...
		final String cmdGetBlogsTalkingAboutArtist = req.getParameter(Constants.GET_BLOGS_TALKING_ABOUT_ARTIST_CMD);
		final String cmdGetAlbumInfo = req.getParameter(Constants.GET_ALBUM_INFO_CMD);//for being used along with the bar client reader...
		final String cmdGetBiographyFromArtist = req.getParameter(Constants.GET_BIOGRAPHY_FROM_ARTIST_CMD);
		final String cmdGetVideosFromAudio = req.getParameter(Constants.GET_VIDEOS_FROM_AUDIO_CMD);
		final String cmdGetLocalEvents = req.getParameter(Constants.GET_LOCAL_EVENTS_CMD);
		final String cmdSearchEvent = req.getParameter(Constants.SEARCH_EVENT_CMD);
		final String cmdSearchVideo = req.getParameter(Constants.SEARCH_VIDEO_CMD);
		final String cmdCreateUserAccount = req.getParameter(Constants.CREATE_USER_ACCOUNT_CMD);//uses openid to create user account
		final String cmdUploadUserPicture = req.getParameter(Constants.UPLOAD_USER_PICTURE_CMD);
		final String cmdGetUserProfile = req.getParameter(Constants.GET_USER_PROFILE_CMD);
		final String cmdGetUserArtistFavorites = req.getParameter(Constants.GET_USER_ARTIST_FAVORITES_CMD);
		final String cmdGetUserAudioFavorites = req.getParameter(Constants.GET_USER_AUDIO_FAVORITES_CMD);
		final String cmdInviteFriendsToMuxiqa = req.getParameter(Constants.INVITE_FRIENDS_TO_MUXIQA_CMD);
		final String cmdSendInvitationToBeFriend = req.getParameter(Constants.SEND_INVITATION_TO_BE_FRIEND_CMD);
		final String cmdAuthorizeToBeFriend = req.getParameter(Constants.AUTHORIZE_TO_BE_FRIEND_CMD);
		final String cmdSendMessageToFriend = req.getParameter(Constants.SEND_MESSAGE_TO_FRIEND_CMD);
		final String cmdRateAndCommentOnAudio = req.getParameter(Constants.RATE_COMMENT_ON_AUDIO_CMD);
		final String cmdRecommendArtistToFriend = req.getParameter(Constants.RECOMMEND_ARTIST_TO_FRIEND_CMD);
		final String cmdRecommendAudioToFriend = req.getParameter(Constants.RECOMMEND_AUDIO_TO_FRIEND_CMD);
		final String cmdRecommendAlbumToFriend = req.getParameter(Constants.RECOMMEND_ALBUM_TO_FRIEND_CMD);
		final String cmdAuthorizeOnFacebook = req.getParameter(Constants.AUTHORIZE_ON_FACEBOOK_CMD);
		final String cmdPublishOnFacebookWall = req.getParameter(Constants.PUBLISH_ON_FACEBOOK_WALL_CMD);
		final String cmdAuthorizeOnTwitter = req.getParameter(Constants.AUTHORIZE_ON_TWITTER_CMD);
		final String cmdPublishOnTwitterStream = req.getParameter(Constants.PUBLSIH_ON_TWITTER_STREAM_CMD);
		final String cmdBuyMusicByStore = req.getParameter(Constants.BUY_MUSIC_BY_STORE_CMD);//adjustable by platform, i.e. if nokia, go to nokia music store, if apple, get to itunes
		
		//to be confirmed
		final String cmdCreatePlayListFromLibrary = req.getParameter(Constants.CREATE_PLAYLIST_FROM_LIBRARY_CMD);
		final String cmdGetUserPlaylists = req.getParameter(Constants.GET_USER_PLAYLISTS_CMD);
		final String cmdGetRouteToEvent = req.getParameter(Constants.GET_ROUTE_TO_EVENT_CMD);
		final String cmdGetRadioStations = req.getParameter(Constants.GET_RADIO_STATIONS_CMD);
		final String cmdPublishUserLocation = req.getParameter(Constants.PUBLISH_USER_LOCATION_CMD);
		final String cmdLocateFriendsNearByUser = req.getParameter(Constants.LOCATE_FRIENDS_NEAR_BY_USER_CMD);
		final String cmdLocateAudioPlayingNearByUser = req.getParameter(Constants.LOCATE_AUDIO_PLAYING_NEAR_BY_USER_CMD);
		final String cmdGetFreeMusicByTag = req.getParameter(Constants.GET_FREE_MUSIC_BY_TAG_CMD);
		final String cmdLocateStoresNearByUser = req.getParameter(Constants.LOCATE_STORES_NEAR_BY_USER_CMD);
		final String cmdRecommendAudioToUser = req.getParameter(Constants.RECOMMEND_AUDIO_TO_USER_CMD);//base on hadoop and mahout
		
		
		
		/////////////////////////////OLD LEGACY//////////////////////////////
		final String cmdGetArtist = req.getParameter("get_artist");
		final String cmdGetAudio = req.getParameter("get_audio");
		final String cmdGetAlbum = req.getParameter("get_album");
		final String cmdGetTopArtists = req.getParameter("get_top_artists");
		final String cmdGetTopArtistsFromLocale = req
				.getParameter("get_top_artists_locale");
		final String cmdGetBiography = req.getParameter("get_biography");
		final String cmdGetVideo = req.getParameter("get_video");
		final String cmdGetVideoSong = req.getParameter("get_video_song");
		final String cmdGetTopVideos = req.getParameter("get_top_videos");
		final String cmdGetTopAudios = req.getParameter("get_top_audios");
		//final String cmdSearchAudio = req.getParameter("search_audio");
		final String cmdGetEvents = req.getParameter("get_events");
		final String cmdGetFreeMusic = req.getParameter("get_free_music");
		final String cmdGetLyrics = req.getParameter("get_lyrics");
		final String cmdGetMapFromPosition = req.getParameter("get_map");
		final String cmdSubscribe = req.getParameter("subscribe");
		
		
		
		/////////////////////////////RESPONSES//////////////////////////////////
		Object response = null;
		//responses
		if(cmdRegisterDevice != null && cmdRegisterDevice.trim().length() > 0) {//muxiqa?rd=mob&id=1234567890&lang=es&ver=1
			final String uuid = req.getParameter(Constants.ID);//the UUID of the client, i.e. in mobile will be the IMEI, if available, if not a generated number
			final String language = req.getParameter(Constants.LANGUAGE);//the language of the device
			final String version = req.getParameter(Constants.VERSION);//the version of the muxiqa client
			response = service.registerDevice(cmdRegisterDevice, userAgent, ipAddress, uuid, language, version);
		}
		//in retrospective, it should be done with a websockets/push/cometd layer over GAE/J and the same layer in the client...instead of this pull
		else if(cmdGetTheWall != null && cmdGetTheWall.trim().length() > 0) {//muxiqa?gw=1292285548687&id=1234567890&tag=rock&artid=EN1234567890&str=30rws=10
			final long utc = Long.parseLong(cmdGetTheWall);//the last update on device in UTC, i.e. System.currentTimeMillis()
			final long id = (req.getParameter(Constants.ID) != null && req.getParameter(Constants.ID).trim().length() > 0) ? Long.parseLong(req.getParameter(Constants.START)) : 0;//the id of the device... 
			final String musicTag = req.getParameter(Constants.MUSIC_TAG);//the music tag genre preferences or all if no preferences...
			final String artistID = req.getParameter(Constants.ARTIST_ID);//the artist id if selected or not at all...
			int start = (req.getParameter(Constants.START) != null && req.getParameter(Constants.START).trim().length() > 0) ? Integer.parseInt(req.getParameter(Constants.START)) : 0;
			int rows = (req.getParameter(Constants.ROWS) != null && req.getParameter(Constants.ROWS).trim().length() > 0) ? Integer.parseInt(req.getParameter(Constants.ROWS)) : 1;
			response = service.getLatestPostsInTheWall(id, utc, musicTag, artistID, start, rows);
		}
		else if(cmdSearchArtist != null && cmdSearchArtist.trim().length() > 0) {//muxiqa?sa=U2&id=1234567890&imgsiz=large&rws=5
			final long id = (req.getParameter(Constants.ID) != null && req.getParameter(Constants.ID).trim().length() > 0) ? Long.parseLong(req.getParameter(Constants.START)) : 0;//the id of the device...
			final String imageSize = req.getParameter(Constants.IMAGE_SIZE);
			int rows = (req.getParameter(Constants.ROWS) != null && req.getParameter(Constants.ROWS).trim().length() > 0) ? Integer.parseInt(req.getParameter(Constants.ROWS)) : 1;
			response = service.searchForArtist(id, cmdGetArtist, imageSize, rows);
		}
		
		
		//////////////////////////OLD LEGACY///////////////////////////////////
		else if (cmdGetArtist != null && cmdGetArtist.trim().length() > 0) {
			final String image = (req.getParameter("image") != null && req
					.getParameter("image").trim().length() > 0) ? req
					.getParameter("image") : "";
			response = service.searchForArtist(cmdGetArtist, image);
		} else if (cmdGetAudio != null && cmdGetAudio.trim().length() > 0) {
			int start = (req.getParameter("start") != null && req.getParameter(
					"start").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("start")) : 0;
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 1;
			response = service.getAudioFromArtist(cmdGetAudio, start, rows);
		} else if (cmdGetAlbum != null && cmdGetAlbum.trim().length() > 0) {
			final String artist = (req.getParameter("artist") != null && req
					.getParameter("artist").trim().length() > 0) ? req
					.getParameter("artist") : "";
			response = service.getAlbumFromArtist(artist, cmdGetAlbum);
		} else if (cmdGetTopArtists != null
				&& cmdGetTopArtists.trim().length() > 0) {
			final String image = (req.getParameter("image") != null && req
					.getParameter("image").trim().length() > 0) ? req
					.getParameter("image") : "";
			int rows = Integer.parseInt(cmdGetTopArtists);
			response = service.getTopArtists(rows, image);
		} else if (cmdGetTopArtistsFromLocale != null
				&& cmdGetTopArtistsFromLocale.trim().length() > 0) {
			final String image = (req.getParameter("image") != null && req
					.getParameter("image").trim().length() > 0) ? req
					.getParameter("image") : "";
			response = service.getTopArtistsFromCountry(
					cmdGetTopArtistsFromLocale, image);
		} else if (cmdGetBiography != null
				&& cmdGetBiography.trim().length() > 0) {
			response = service.getBiographyFromArtist(cmdGetBiography);
		} else if (cmdGetVideo != null && cmdGetVideo.trim().length() > 0) {
			int start = (req.getParameter("start") != null && req.getParameter(
					"start").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("start")) : 0;
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 0;
			response = service.getVideosFromArtist(cmdGetVideo, start, rows);
		} else if (cmdGetVideoSong != null
				&& cmdGetVideoSong.trim().length() > 0) {
			final String song = (req.getParameter("song") != null && req
					.getParameter("song").trim().length() > 0) ? req
					.getParameter("song") : "";
			final String image = (req.getParameter("image") != null && req
					.getParameter("image").trim().length() > 0) ? req
					.getParameter("image") : "";
			final String type = (req.getParameter("type") != null && req
					.getParameter("type").trim().length() > 0) ? req
					.getParameter("type") : "";
			int start = (req.getParameter("start") != null && req.getParameter(
					"start").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("start")) : 1;
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 1;
			response = service.getYouTubeVideoSongsFromArtist(cmdGetVideoSong,
					song, image, type, start, rows);
		} else if (cmdGetTopVideos != null
				&& cmdGetTopVideos.trim().length() > 0) {
			final String cityName = (req.getParameter("cityName") != null && req
					.getParameter("cityName").trim().length() > 0) ? req
					.getParameter("cityName") : "";
			final String image = (req.getParameter("image") != null && req
					.getParameter("image").trim().length() > 0) ? req
					.getParameter("image") : "";
			final String type = (req.getParameter("type") != null && req
					.getParameter("type").trim().length() > 0) ? req
					.getParameter("type") : "";
			int start = (req.getParameter("start") != null && req.getParameter(
					"start").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("start")) : 1;
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 1;
			response = service.getTopVideos(cmdGetTopVideos, cityName, image,
					type, start, rows);
		} else if (cmdGetTopAudios != null
				&& cmdGetTopAudios.trim().length() > 0) {
			String cityName = (req.getParameter("cityName") != null && req
					.getParameter("cityName").trim().length() > 0) ? req
					.getParameter("cityName") : "";
			int start = (req.getParameter("start") != null && req.getParameter(
					"start").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("start")) : 0;
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 0;
			response = service.getTopAudiosFromLocation(cmdGetTopAudios,
					cityName, start, rows);
		} else if (cmdSearchAudio != null && cmdSearchAudio.trim().length() > 0) {
			String artist = (req.getParameter("artist") != null && req
					.getParameter("artist").trim().length() > 0) ? req
					.getParameter("artist") : "";
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 0;
			response = service.searchForAudio(cmdSearchAudio, artist, rows);
		} else if (cmdGetEvents != null && cmdGetEvents.trim().length() > 0) {
			String cityName = (req.getParameter("cityName") != null && req
					.getParameter("cityName").trim().length() > 0) ? req
					.getParameter("cityName") : "";
			response = service.getEvents(cmdGetEvents, cityName);
		} else if (cmdGetFreeMusic != null
				&& cmdGetFreeMusic.trim().length() > 0) {
			int rows = (req.getParameter("rows") != null && req.getParameter(
					"rows").trim().length() > 0) ? Integer.parseInt(req
					.getParameter("rows")) : 1;
			response = service.getFreeMusicByGenre(cmdGetFreeMusic, rows);
		} else if (cmdGetLyrics != null && cmdGetLyrics.trim().length() > 0) {
			String artist = (req.getParameter("artist") != null && req
					.getParameter("artist").trim().length() > 0) ? req
					.getParameter("artist") : "";
			response = service.getLyricFromSong(artist, cmdGetLyrics);
		} else if (cmdGetMapFromPosition != null
				&& cmdGetMapFromPosition.trim().length() > 0) {
			final StringTokenizer st = new StringTokenizer(cmdGetMapFromPosition, ",");
			String latitude = st.nextToken();
			String longitude = st.nextToken();
			response = service.getMapFromLocation(Double.parseDouble(latitude),
					Double.parseDouble(longitude));
		} else if (cmdSubscribe != null && cmdSubscribe.trim().length() > 0) {
			final String email = (req.getParameter("email") != null && req
					.getParameter("email").trim().length() > 0) ? req
					.getParameter("email") : "";
			final String phone = (req.getParameter("phone") != null && req
					.getParameter("phone").trim().length() > 0) ? req
					.getParameter("phone") : "";
			response = service.saveUserSubscription(cmdSubscribe, email, phone);
		}
		final String callback = req.getParameter("callback");
		if (callback != null && callback.trim().length() > 0) {
			resp.getWriter().println(callback + "(" + response + ");");
		} else {
			resp.getWriter().println(response);
		}
		final long stopTime = System.currentTimeMillis();
		logger.log(Level.WARNING, "process time - " + (stopTime - startTime)
				/ (double) 1000);
		resp.flushBuffer();
	}
	
	private void makeUseOfCache(HttpServletResponse resp) {
		final long now = System.currentTimeMillis();
		resp.setHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECONDS + ",must-revalidate");
		//resp.addHeader("Cache-Control", "must-revalidate");// optional?
		resp.setDateHeader("Last-Modified", now);
		resp.setDateHeader("Expires", now + CACHE_DURATION_IN_MILISECONDS);
	}
	
	private void makeNotUseOfAnyCaching(HttpServletResponse resp) {
		resp.setHeader("Pragma", "No-cache"); 
		resp.setHeader("Cache-Control", "no-cache,no-store,max-age=0"); 
		resp.setDateHeader("Expires", 1);
	}
	
	private String getClientIPAddress(HttpServletRequest req) {
		String ipAddress = req.getRemoteAddr();
		String forwardedIp = req.getHeader("X-Forwarded-For");
		if (forwardedIp != null) {
			// If it has been forwarded multiple times, there will be
			// multiple IPs listed. We only want the last one...
			int lastComma = forwardedIp.lastIndexOf(",");
			if (lastComma >= 0) {
				ipAddress = forwardedIp.substring(lastComma + 1).trim();
			} else {
				ipAddress = forwardedIp;
			}
		}
		return ipAddress;
	}
	
	private String getUserAgent(HttpServletRequest req) {
		String userAgent = req.getHeader("User-Agent");
		if (req.getHeader("X-OperaMini-Phone-UA") != null) {
			userAgent = req.getHeader("X-OperaMini-Phone-UA");
		} else if (req.getHeader("X-Original-User-Agent") != null) {
			userAgent = req.getHeader("X-Original-User-Agent");
		} else if (req.getHeader("X-Device-User-Agent") != null) {
			userAgent = req.getHeader("X-Device-User-Agent");
		}
		// TODO: this a special case in GAE/J where it appends ",gzip(gfe)" at
		// the end of the UA request header
		// ...we have to trim it...because can affect the validation for the
		// original and UA device compliance
		if (userAgent.endsWith(",gzip(gfe)")) {
			userAgent = userAgent.substring(0, userAgent.lastIndexOf(','));
		}
		return userAgent;
	}

}// END OF FILE