/**
 * 
 */
package mx.com.muxiqa.services.music;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.core.db.entities.Album;
import mx.com.muxiqa.services.core.db.entities.Artist;
import mx.com.muxiqa.services.core.db.entities.Audio;
import mx.com.muxiqa.services.music.amazon.AmazonService;
import mx.com.muxiqa.services.music.chartlyric.ChartLyricService;
import mx.com.muxiqa.services.music.echonest.EchoNest4Service;
import mx.com.muxiqa.services.music.echonest.entities.EchoNestArtist;
import mx.com.muxiqa.services.music.echonest.entities.EchonestArtistAudio;
import mx.com.muxiqa.services.music.jamendo.JamendoService;
import mx.com.muxiqa.services.music.lastfm.LastFMService;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMArtist;
import mx.com.muxiqa.services.music.lastfm.entities.LastFMSong;
import mx.com.muxiqa.services.music.youtube.YouTubeService;
import mx.com.muxiqa.util.Constants;

/**
 * @author gerardomartinezgil
 * 
 */
public final class MusicService {
	// the music services
	private AmazonService amazonService;
	private ChartLyricService chartLyricService;
	private EchoNest4Service echoNest4Service;
	private JamendoService jamendoService;
	private LastFMService lastFMService;
	private YouTubeService youTubeService;

	/**
	 * 
	 * 
	 * @throws ParserConfigurationException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * 
	 */
	public MusicService(final CoreService core) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			ParserConfigurationException {
		amazonService = new AmazonService(core);
		chartLyricService = new ChartLyricService(core);
		echoNest4Service = new EchoNest4Service(core);
		jamendoService = new JamendoService(core);
		lastFMService = new LastFMService(core);
		youTubeService = new YouTubeService(core);
	}

	/**
	 * Full search for artist and/or music given it's name. Returns the
	 * artist/album/audio full objects and it's relation graph.
	 * 
	 * @param query
	 * @param start
	 * @param rows
	 * @param imageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	public List<Artist> search(final String query, final int start,
			final int rows, final String imageSize)
			throws UnsupportedEncodingException, XPathExpressionException {
		List<EchonestArtistAudio> echoNestArtistsAudios = echoNest4Service
				.search(query, start, rows);
		if (echoNestArtistsAudios != null && echoNestArtistsAudios.size() > 0) {
			List<Artist> results = new ArrayList<Artist>(echoNestArtistsAudios
					.size());
			for (final EchonestArtistAudio echoNestArtistAudio : echoNestArtistsAudios) {
				String audioTitle = echoNestArtistAudio.getAudioTitle();
				if (audioTitle != null && audioTitle.trim().length() > 0) {
					if (audioTitle.contains("(")) {// TODO:a regexp should be
						// better for this work...
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
					final LastFMSong lastFMSong = lastFMService.getSongInfo(
							audioTitle, echoNestArtistAudio.getArtistName());
					// artist
					Artist artist = new Artist();
					artist.setArtistId(echoNestArtistAudio.getArtistId());
					artist.setName(echoNestArtistAudio.getArtistName());
					// album
					Album album = new Album();
					album.setName(echoNestArtistAudio.getAudioAlbumName());
					if (imageSize != null && imageSize.trim().length() > 0) {
						if (lastFMSong != null) {
							if (Constants.SMALL_IMAGE_SIZE.equals(imageSize)) {
								album.setImageURL(lastFMSong
										.getAlbumSmallImageURL());
							}
							if (Constants.MEDIUM_IMAGE_SIZE.equals(imageSize)) {
								album.setImageURL(lastFMSong
										.getAlbumMediumImageURL());
							}
							if (Constants.LARGE_IMAGE_SIZE.equals(imageSize)) {
								album.setImageURL(lastFMSong
										.getAlbumLargeImageURL());
							}
						} else {
							album.setImageURL(echoNestArtistAudio
									.getAudioAlbumImageURL());
						}
					} else {
						album.setImageURL(echoNestArtistAudio
								.getAudioAlbumImageURL());
					}
					artist.addAlbum(album);
					// audio
					Audio audio = new Audio();
					audio.setAudioId(echoNestArtistAudio.getAudioId());
					audio.setName(echoNestArtistAudio.getAudioTitle());
					audio.setPreviewURL(echoNestArtistAudio
							.getAudioPreviewURL());
					album.addAudio(audio);
					// append
					results.add(artist);
				}
			}
			return results;
		}
		return null;
	}

	/**
	 * Search for a specific artist based on her/his name. Returns an array with
	 * the possible Artist(s) founded.
	 * 
	 * @param artistName
	 * @param rows
	 * @param imageType
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	public List<Artist> searchForArtists(final String artistName,
			final int rows, final String imageType)
			throws UnsupportedEncodingException, XPathExpressionException {
		List<EchoNestArtist> echoNestArtists = echoNest4Service
				.searchForArtist(artistName, rows);
		if (echoNestArtists != null && echoNestArtists.size() > 0) {
			List<Artist> results = new ArrayList<Artist>(echoNestArtists.size());
			for (final EchoNestArtist echoNestArtist : echoNestArtists) {
				final LastFMArtist lastFMArtist = lastFMService
						.getArtistInfo(echoNestArtist.getName());
				Artist artist = new Artist();
				artist.setArtistId(echoNestArtist.getId());
				artist.setName(echoNestArtist.getName());
				if (lastFMArtist != null) {
					if (imageType != null && imageType.trim().length() > 0) {
						if (Constants.SMALL_IMAGE_SIZE.equals(imageType)) {
							artist.setImageURL(lastFMArtist
									.getSmallPictureURL());
						}
						if (Constants.MEDIUM_IMAGE_SIZE.equals(imageType)) {
							artist.setImageURL(lastFMArtist
									.getMediumPictureURL());
						}
						if (Constants.LARGE_IMAGE_SIZE.equals(imageType)) {
							artist.setImageURL(lastFMArtist
									.getLargePictureURL());
						}
					}
				}
				results.add(artist);
			}
			return results;
		}
		return null;
	}

	/**
	 * Search for audio given the artist and/or the song name. Returns the
	 * artist/album/audio full objects and it's relation graph. The audio it's
	 * only a preview from the full song.
	 * 
	 * @param artistId
	 * @param artistName
	 * @param songName
	 * @param start
	 * @param rows
	 * @param imageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XPathExpressionException
	 */
	public List<Artist> searchForAudios(final String artistId,
			final String artistName, final String songName, final int start,
			final int rows, final String imageSize)
			throws UnsupportedEncodingException, XPathExpressionException {
		List<EchonestArtistAudio> echoNestArtistsAudios = echoNest4Service
				.searchForAudio(artistId, artistName, songName, start, rows);
		if (echoNestArtistsAudios != null && echoNestArtistsAudios.size() > 0) {
			List<Artist> results = new ArrayList<Artist>(echoNestArtistsAudios
					.size());
			for (final EchonestArtistAudio echoNestArtistAudio : echoNestArtistsAudios) {
				String audioTitle = echoNestArtistAudio.getAudioTitle();
				if (audioTitle != null && audioTitle.trim().length() > 0) {
					if (audioTitle.contains("(")) {// TODO:a regexp should be
						// better for this work...
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
					final LastFMSong lastFMSong = lastFMService.getSongInfo(
							audioTitle, echoNestArtistAudio.getArtistName());
					// artist
					Artist artist = new Artist();
					artist.setArtistId(echoNestArtistAudio.getArtistId());
					artist.setName(echoNestArtistAudio.getArtistName());
					// album
					Album album = new Album();
					album.setName(echoNestArtistAudio.getAudioAlbumName());
					if (imageSize != null && imageSize.trim().length() > 0) {
						if (lastFMSong != null) {
							if (Constants.SMALL_IMAGE_SIZE.equals(imageSize)) {
								album.setImageURL(lastFMSong
										.getAlbumSmallImageURL());
							}
							if (Constants.MEDIUM_IMAGE_SIZE.equals(imageSize)) {
								album.setImageURL(lastFMSong
										.getAlbumMediumImageURL());
							}
							if (Constants.LARGE_IMAGE_SIZE.equals(imageSize)) {
								album.setImageURL(lastFMSong
										.getAlbumLargeImageURL());
							}
						} else {
							album.setImageURL(echoNestArtistAudio
									.getAudioAlbumImageURL());
						}
					} else {
						album.setImageURL(echoNestArtistAudio
								.getAudioAlbumImageURL());
					}
					artist.addAlbum(album);
					// audio
					Audio audio = new Audio();
					audio.setAudioId(echoNestArtistAudio.getAudioId());
					audio.setName(echoNestArtistAudio.getAudioTitle());
					audio.setPreviewURL(echoNestArtistAudio
							.getAudioPreviewURL());
					album.addAudio(audio);
					// append
					results.add(artist);
				}
			}
			return results;
		}
		return null;
	}

}// END OF FILE