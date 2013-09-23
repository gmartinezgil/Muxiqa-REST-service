/**
 * 
 */
package mx.com.muxiqa.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mx.com.muxiqa.services.core.cache.CacheService;
import mx.com.muxiqa.services.core.db.DatabaseService;
import mx.com.muxiqa.services.core.db.entities.Device;
import mx.com.muxiqa.services.core.db.entities.Location;
import mx.com.muxiqa.services.core.db.entities.User;
import mx.com.muxiqa.services.core.http.HttpService;
import mx.com.muxiqa.services.location.cloudmade.CloudMadeService;
import mx.com.muxiqa.services.location.google.GoogleGeocodeService;
import mx.com.muxiqa.services.location.ipinfodb.IPInfoDBService;
import mx.com.muxiqa.services.location.opencellid.OpenCellIdService;
import mx.com.muxiqa.services.music.amazon.AmazonService;
import mx.com.muxiqa.services.music.amazon.entities.AmazonAlbum;
import mx.com.muxiqa.services.music.chartlyric.ChartLyricService;
import mx.com.muxiqa.services.music.echonest.EchoNest3Service;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestArtist;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestAudio;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestBiography;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestVideo;
import mx.com.muxiqa.services.music.jamendo.JamendoService;
import mx.com.muxiqa.services.music.jamendo.entities.JamendoMusic;
import mx.com.muxiqa.services.music.lastfm.LastFMService;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMArtist;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMEvent;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMSong;
import mx.com.muxiqa.services.music.youtube.YouTubeService;
import mx.com.muxiqa.services.music.youtube.entities.YouTubeVideo;
import mx.com.muxiqa.util.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * It's the core service for the system.
 * All the clients make use of this service.
 * @author gerardomartinezgil
 * 
 */
//TODO:missing users historical registration...
//TODO:get also the time zone of the server...
//TODO:get the google translate service also...
//TODO:get the date library...
//TODO:get the facebook & twitter libraries...
public final class MuxiqaService {
	public static final Logger logger = Logger.getLogger(MuxiqaService.class
			.getName());

	// core
	private DatabaseService databaseService;
	private HttpService httpService;
	private CacheService cacheService;
	// music
	private EchoNest3Service echoNest3Service;// TODO:change it for the new
	// EchoNest4Service...
	private LastFMService lastFMService;
	private JamendoService jamendoService;
	// video
	private YouTubeService youTubeService;
	// lyrics
	private ChartLyricService chartLyricService;
	// location
	private CloudMadeService cloudMadeService;
	private IPInfoDBService ipInfoDBService;
	private GoogleGeocodeService googleGeocodeService;
	private OpenCellIdService openCellIdService;
	// buy
	private AmazonService amazonService;// TODO:adapt to the music store of the client


	/**
	 * Initializes all the services used by muxiqa.
	 */
	public MuxiqaService() {
		// core
		databaseService = new DatabaseService();
		httpService = new HttpService();
		cacheService = new CacheService();
		// music
		echoNest3Service = new EchoNest3Service(httpService, cacheService);
		lastFMService = new LastFMService(httpService, cacheService);
		jamendoService = new JamendoService(httpService, cacheService);
		// lyrics
		chartLyricService = new ChartLyricService(httpService, cacheService);
		// video
		youTubeService = new YouTubeService(cacheService);
		// location
		cloudMadeService = new CloudMadeService(httpService, cacheService);
		ipInfoDBService = new IPInfoDBService(httpService, cacheService);
		googleGeocodeService = new GoogleGeocodeService(httpService,
				cacheService);
		openCellIdService = new OpenCellIdService(httpService, cacheService);
		// buy
		try {
			amazonService = new AmazonService(httpService);
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
	}

	/**
	 * Register the device the first time of use of the app, just only one
	 * occasion. Obtains these basic information and return an unique id (long)
	 * that it's stored on the device and this's one it's send in return for
	 * statistic and identification purposes.
	 * 
	 * @param deviceType
	 * @param userAgent
	 * @param ipAddress
	 * @param uuid
	 * @param language
	 * @return
	 */
	// TODO:should be called in a secure HTTPS connection if we transmit the
	// uuid value of the device(i.e. IMEI/IMSI if available in phone)...
	public JSONObject registerDevice(final String deviceType,
			final String userAgent, final String ipAddress, final String uuid,
			final String language, final String version) {
		if ((deviceType != null && deviceType.trim().length() > 0)
				&& (userAgent != null && userAgent.trim().length() > 0)
				&& (ipAddress != null && ipAddress.trim().length() > 0)
				&& (version != null && version.trim().length() > 0)) {
			Map<String, String> map = new HashMap<String, String>();
			Device device = new Device();
			device.setType(deviceType);
			device.setUserAgent(userAgent);
			device.setIpAddress(ipAddress);
			device.setUuid((uuid != null && uuid.trim().length() > 0) ? uuid
					: "");
			device
					.setLanguage((language != null && language.trim().length() > 0) ? language
							: "");
			device.setVersion(version);
			final long id = databaseService.registerDevice(device);
			map.put(Constants.ID, String.valueOf(id));
			// TODO: should be put into a background process...
			try {
				final Location location = ipInfoDBService
						.getCityLocationFromIPAddress(ipAddress);
				if (location != null) {
					databaseService.registerDeviceLocation(location, device);
					map.put(Constants.COUNTRY_NAME, location.getCountryName());
					map.put(Constants.CITY_NAME, location.getCityName());
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
			return JSONObject.fromObject(map);
		}
		return null;
	}

	/**
	 * Register for the beta period to the announcement of the availability of
	 * the app
	 * 
	 * @param name
	 * @param email
	 * @param phone
	 * @return
	 */
	public JSONObject saveUserSubscription(final String name,
			final String email, final String phone) {
		if ((name != null && name.trim().length() > 0)
				&& (email != null && email.trim().length() > 0)
				&& (phone != null && phone.trim().length() > 0)) {
			User user = new User();
			user.setName(name);
			user.setEmailAddress(email);
			Device device = new Device();
			device.setFromUser(user);
			device.setBrand(phone);
			final long id = databaseService.saveUser(user);
			Map<String, String> map = new HashMap<String, String>();
			map.put(Constants.ID, String.valueOf(id));
			return JSONObject.fromObject(map);
		}
		return null;
	}

	/**
	 * Update the status of the user in the muxiqa network. Updates the music
	 * that she/he are listening and publish directly on the wall, for example.
	 * Or let it now to others a concert/event she/he it's attending.
	 * 
	 * @param deviceId
	 * @param userName
	 * @param eventType
	 * @param artistId
	 * @param albumName
	 * @param audioId
	 */
	public void updateStatus(final long deviceId, final String userName,
			final int eventType, final String artistId, final String albumName,
			final String audioId) {//TODO: location parameter
		if (deviceId > 0) {
			final User user = databaseService.getUserByDeviceId(deviceId);
			if (user != null) {
				switch (eventType) {
					case Constants.PLAY_AUDIO_EVENT:
						//publish to the wall the user's music it's listening...
						//databaseService.updatePlayingMusicInWall(user, artistId, audioId, albumName);
						break;
					case Constants.AT_CONCERT_EVENT:
						//publish user's concert event and location...
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public JSONArray searchForArtist(final String name, final String image) {
		if (name != null && name.trim().length() > 0) {
			try {
				final List<EchoNestArtist> artists = echoNest3Service
						.getArtist(name);
				if (artists != null && artists.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (EchoNestArtist echoNestArtist : artists) {
						final LastFMArtist lastFMArtist = lastFMService
								.getArtistInfo(echoNestArtist.getName());
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constants.ID, echoNestArtist.getId());
						map.put(Constants.NAME, echoNestArtist.getName());
						if (lastFMArtist != null) {
							if (image != null && image.trim().length() > 0) {
								if (Constants.LARGE_IMAGE_SIZE.equals(image))
									map.put(Constants.IMAGE, lastFMArtist
											.getLargePictureURL());
							} else
								map.put(Constants.IMAGE, lastFMArtist
										.getMediumPictureURL());
						} else
							map.put(Constants.IMAGE, "");
						jsonArray.add(JSONObject.fromObject(map));
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param rows
	 * @return
	 */
	public JSONArray getTopArtists(final int rows, final String image) {
		try {
			final List<EchoNestArtist> artists = echoNest3Service
					.getTopHotArtists(rows);
			if (artists != null && artists.size() > 0) {
				JSONArray jsonArray = new JSONArray();
				for (EchoNestArtist artist : artists) {
					final LastFMArtist lastFMArtist = lastFMService
							.getArtistInfo(artist.getName());
					Map<String, String> map = new HashMap<String, String>();
					map.put(Constants.ID, artist.getId());
					map.put(Constants.NAME, artist.getName());
					if (lastFMArtist != null) {
						if (image != null && image.trim().length() > 0) {
							if (Constants.LARGE_IMAGE_SIZE.equals(image))
								map.put(Constants.IMAGE, lastFMArtist
										.getLargePictureURL());
						} else
							map.put(Constants.IMAGE, lastFMArtist
									.getMediumPictureURL());
					} else
						map.put(Constants.IMAGE, "");
					jsonArray.add(JSONObject.fromObject(map));
				}
				return jsonArray;
			}
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param start
	 * @param rows
	 * @return
	 */
	public JSONArray getAudioFromArtist(final String artistId, final int start,
			final int rows) {
		if (artistId != null && artistId.trim().length() > 0) {
			try {
				final List<EchoNestAudio> audios = echoNest3Service
						.getAudioFromArtist(artistId, start, rows);
				if (audios != null && audios.size() > 0) {
					final EchoNestArtist echoNestArtist = echoNest3Service
							.getProfile(artistId);
					JSONArray jsonArray = new JSONArray();
					for (EchoNestAudio echoNestAudio : audios) {
						String audioTitle = echoNestAudio.getTitle();
						if (audioTitle != null
								&& audioTitle.trim().length() > 0) {
							if (audioTitle.contains("(")) {
								audioTitle = audioTitle.substring(0,
										audioTitle.indexOf('(')).trim();
							}
							if (audioTitle.contains("[")) {
								audioTitle = audioTitle.substring(0,
										audioTitle.indexOf('[')).trim();
							}
							if (audioTitle.contains("-")) {
								audioTitle = audioTitle.substring(0,
										audioTitle.indexOf('-')).trim();
							}
							if (audioTitle.contains("/")) {
								audioTitle = audioTitle.substring(0,
										audioTitle.indexOf('/')).trim();
							}
							final LastFMSong lastFMSong = lastFMService
									.getSongInfo(audioTitle, echoNestArtist
											.getName());
							// AmazonAlbum album =
							// amazonService.getAlbumInformation(artist.getName(),
							// audio.getTitle());
							Map<String, String> map = new HashMap<String, String>();
							map.put(Constants.TITLE, echoNestAudio.getTitle());
							// map.put("formatted-title", lastFMSong.getName());
							if (lastFMSong != null)
								map.put(Constants.ALBUM, lastFMSong
										.getAlbumName());
							else
								map.put(Constants.ALBUM, echoNestAudio
										.getRelease());
							if (lastFMSong != null)
								map.put(Constants.ARTIST, lastFMSong
										.getArtistName());
							else
								map.put(Constants.ARTIST, echoNestArtist
										.getName());
							if (lastFMSong != null)
								map.put(Constants.IMAGE, lastFMSong
										.getAlbumSmallImageURL());
							else
								map.put(Constants.IMAGE, "");
							map.put(Constants.URL, echoNestAudio.getUrl());
							jsonArray.add(JSONObject.fromObject(map));
						}
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artistName
	 * @param song
	 * @return
	 */
	public JSONObject getAlbumFromArtist(final String artistName,
			final String song) {
		try {
			final AmazonAlbum album = amazonService.getAlbumInformation(
					artistName, song);
			if (album != null)
				return JSONObject.fromObject(album);// TODO:use the same
			// Constant class for fields
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @param rows
	 * @return
	 */
	public JSONArray getVideosFromArtist(final String artistId,
			final int start, final int rows) {
		try {
			final List<EchoNestVideo> videos = echoNest3Service
					.getVideoFromArtist(artistId, start, rows);
			if (videos != null && videos.size() > 0) {
				JSONArray jsonArray = new JSONArray();
				for (EchoNestVideo video : videos) {
					jsonArray.add(JSONObject.fromObject(video));// TODO:use the
					// same Constant
					// class for
					// fields
				}
				return jsonArray;
			}
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param artistName
	 * @param songName
	 * @param image
	 * @param type
	 * @param start
	 * @param rows
	 * @return
	 */
	public JSONArray getYouTubeVideoSongsFromArtist(final String artistName,
			final String songName, final String image, final String type,
			final int start, final int rows) {
		try {
			final List<YouTubeVideo> videos = youTubeService
					.searchArtistSongVideos(artistName, songName, start, rows);
			if (videos != null && videos.size() > 0) {
				JSONArray jsonArray = new JSONArray();
				for (YouTubeVideo video : videos) {
					Map<String, String> map = new HashMap<String, String>();
					map.put(Constants.TITLE, video.getTitle());
					if (image != null && image.trim().length() > 0) {
						if (Constants.LARGE_IMAGE_SIZE.equals(image)) {
							map.put(Constants.IMAGE, video
									.getLargeThumbNailURL());
						}
					} else {
						map.put(Constants.IMAGE, video.getMediumThumbNailURL());
					}
					if (type != null && type.trim().length() > 0) {
						if (Constants.AVERAGE_QUALITY_MOBILE_VIDEO.equals(type)) {
							map.put(Constants.URL, video.getVideos().get(2));
						} else if (Constants.LARGE_QUALITY_WEB_VIDEO
								.equals(type)) {
							map.put(Constants.URL, video.getVideos().get(0));
						}
					} else {
						map.put(Constants.URL, video.getVideos().get(1));
					}
					// map.put("description", video.getDescription());
					jsonArray.add(JSONObject.fromObject(map));
				}
				return jsonArray;
			}
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param artistId
	 * @return
	 */
	public JSONObject getBiographyFromArtist(final String artistId) {
		try {
			final List<EchoNestBiography> bios = echoNest3Service
					.getBiographyFromArtist(artistId, 0, 1);
			if (bios != null && bios.size() > 0) {
				final EchoNestBiography biography = bios.get(0);
				Map<String, String> map = new HashMap<String, String>();
				map.put("summary", biography.getSummary());
				return JSONObject.fromObject(map);
			}
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param countryName
	 * @param cityName
	 * @param rows
	 * @return
	 */
	public JSONArray getTopAudiosFromLocation(final String countryName,
			final String cityName, int start, int rows) {
		if (countryName != null && countryName.trim().length() > 0) {
			try {
				final List<LastFMSong> songs = lastFMService.getTopSongs(
						countryName, cityName);
				if (songs != null && songs.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					if (start < 0)
						start = 0;
					if (start > LastFMService.MAX_RESULTS_TOP_SONGS)
						start = LastFMService.MAX_RESULTS_TOP_SONGS;
					if (rows < 0)
						rows = 0;
					if (rows > LastFMService.MAX_RESULTS_TOP_SONGS)
						rows = LastFMService.MAX_RESULTS_TOP_SONGS;
					for (int i = start; i <= rows; i++) {
						final LastFMSong song = songs.get(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constants.TITLE, song.getName());
						map.put(Constants.IMAGE, song.getAlbumMediumImageURL());
						map.put(Constants.ARTIST, song.getArtistName());
						jsonArray.add(JSONObject.fromObject(map));
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param countryName
	 * @param cityName
	 * @param image
	 * @param type
	 * @param start
	 * @param rows
	 * @return
	 */
	public JSONArray getTopVideos(final String countryName,
			final String cityName, final String image, final String type,
			int start, int rows) {
		if (countryName != null && countryName.trim().length() > 0) {
			try {
				List<LastFMSong> songs = lastFMService.getTopSongs(countryName,
						cityName);
				if (songs != null && songs.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					if (start < 0)
						start = 0;
					if (start > LastFMService.MAX_RESULTS_TOP_SONGS)
						start = LastFMService.MAX_RESULTS_TOP_SONGS;
					if (rows < 0)
						rows = 0;
					if (rows > LastFMService.MAX_RESULTS_TOP_SONGS)
						rows = LastFMService.MAX_RESULTS_TOP_SONGS;
					for (int i = start; i <= rows; i++) {
						final LastFMSong song = songs.get(i);
						Map<String, String> map = new HashMap<String, String>();
						final String artistName = song.getArtistName();
						map.put(Constants.ARTIST, artistName);
						final String songName = song.getName();
						map.put(Constants.AUDIO, songName);
						final List<YouTubeVideo> videos = youTubeService
								.searchArtistSongVideos(artistName, songName,
										1, 1);
						if (videos != null && videos.size() == 1) {
							final YouTubeVideo video = videos.get(0);
							map.put(Constants.TITLE, video.getTitle());
							if (image != null && image.trim().length() > 0) {
								if (Constants.LARGE_IMAGE_SIZE.equals(image)) {
									map.put(Constants.IMAGE, video
											.getLargeThumbNailURL());
								}
							} else {
								map.put(Constants.IMAGE, video
										.getMediumThumbNailURL());
							}
							if (type != null && type.trim().length() > 0) {
								if (Constants.AVERAGE_QUALITY_MOBILE_VIDEO
										.equals(type)) {
									map.put(Constants.URL, video.getVideos()
											.get(2));
								} else if (Constants.LARGE_QUALITY_WEB_VIDEO
										.equals(type)) {
									map.put(Constants.URL, video.getVideos()
											.get(0));
								}
							} else {
								map
										.put(Constants.URL, video.getVideos()
												.get(1));
							}
							// map.put("description", video.getDescription());
						}
						jsonArray.add(JSONObject.fromObject(map));
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param songTitle
	 * @param artistName
	 * @return
	 */
	public JSONArray searchForAudio(final String songTitle,
			final String artistName, final int rows) {
		if (songTitle != null && songTitle.trim().length() > 0) {
			try {
				final List<LastFMSong> songs = lastFMService.searchForSong(
						songTitle, artistName, rows);
				if (songs != null && songs.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < songs.size(); i++) {
						final LastFMSong song = songs.get(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constants.TITLE, song.getName());
						map.put(Constants.IMAGE, song.getAlbumMediumImageURL());
						map.put(Constants.ARTIST, song.getArtistName());
						jsonArray.add(JSONObject.fromObject(map));
					}
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param countryName
	 * @return
	 */
	public JSONArray getTopArtistsFromCountry(final String countryName,
			final String image) {
		if (countryName != null && countryName.trim().length() > 0) {
			try {
				final List<LastFMArtist> artists = lastFMService
						.getTopArtists(countryName);
				if (artists != null && artists.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i <= 5; i++) {
						final LastFMArtist lastFMArtist = artists.get(i);
						final List<EchoNestArtist> echoNestArtist = echoNest3Service
								.getArtist(lastFMArtist.getName());
						Map<String, String> map = new HashMap<String, String>();
						if (echoNestArtist != null && echoNestArtist.size() > 0)
							map
									.put(Constants.ID, echoNestArtist.get(0)
											.getId());
						else
							map.put(Constants.ID, "");
						map.put(Constants.NAME, lastFMArtist.getName());
						if (image != null && image.trim().length() > 0) {
							if (Constants.MEDIUM_IMAGE_SIZE.equals(image))
								map.put(Constants.IMAGE, lastFMArtist
										.getMediumPictureURL());
							else if (Constants.LARGE_IMAGE_SIZE.equals(image))
								map.put(Constants.IMAGE, lastFMArtist
										.getLargePictureURL());
						} else {
							map.put(Constants.IMAGE, lastFMArtist
									.getSmallPictureURL());
						}
						jsonArray.add(JSONObject.fromObject(map));
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param countryName
	 * @return
	 */
	public JSONArray getEvents(final String countryName, final String cityName) {
		if (countryName != null && countryName.trim().length() > 0) {
			try {
				final List<LastFMEvent> events = lastFMService
						.getEventsForLocation(countryName, cityName);
				if (events != null && events.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < 10; i++) {
						final LastFMEvent lastFMEvent = events.get(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constants.TITLE, lastFMEvent.getTitle());
						if (lastFMEvent.getPlacePictureURL() != null)
							map.put(Constants.IMAGE, lastFMEvent
									.getPlacePictureURL());
						else
							map.put(Constants.IMAGE, lastFMEvent
									.getArtistPictureURL());
						map.put(Constants.DATE, lastFMEvent.getEventDate());
						map.put(Constants.PLACE, lastFMEvent.getPlaceName());
						map.put("country", lastFMEvent.getPlaceCountry());
						map.put("city", lastFMEvent.getPlaceCity());
						map.put("latitude", lastFMEvent.getPlaceLatitude());
						map.put("longitude", lastFMEvent.getPlaceLongitude());
						map.put("direction", lastFMEvent.getPlaceStreet());
						map.put("phone", lastFMEvent.getPlacePhoneNumber());
						jsonArray.add(JSONObject.fromObject(map));
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param tag
	 * @param rows
	 * @return
	 */
	public JSONArray getFreeMusicByGenre(final String tag, final int rows) {
		if (tag != null & tag.trim().length() > 0) {
			try {
				final List<JamendoMusic> audios = jamendoService
						.getTopMusicThisDayByGenre(tag, rows);
				if (audios != null && audios.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (JamendoMusic audio : audios) {
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constants.TITLE, audio.getTitle());
						map.put(Constants.ALBUM, audio.getAlbumName());
						map.put(Constants.IMAGE, audio.getAlbumImageURL());
						map.put(Constants.URL, audio.getUrl());
						map.put(Constants.ARTIST, audio.getArtistName());
						jsonArray.add(JSONObject.fromObject(map));
					}
					return jsonArray;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public JSONObject getMapFromLocation(final double latitude,
			final double longitude) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", cloudMadeService.getMapURLFromLocation(latitude,
				longitude, 17, 0));
		return JSONObject.fromObject(map);
	}

	/**
	 * 
	 * @param songName
	 * @param artistName
	 * @return
	 */
	public JSONObject getLyricFromSong(final String artistName,
			final String songName) {
		if (songName != null && songName.trim().length() > 0) {
			try {
				final String lyric = chartLyricService.getLyricFromArtistSong(
						artistName, songName);
				if (lyric != null && lyric.trim().length() > 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("lyric", lyric);
					return JSONObject.fromObject(map);
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		return null;
	}

}// END OF FILE