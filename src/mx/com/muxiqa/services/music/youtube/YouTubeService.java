/**
 * 
 */
package mx.com.muxiqa.services.music.youtube;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.muxiqa.services.core.CoreService;
import mx.com.muxiqa.services.music.youtube.entities.YouTubeVideo;

import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.MediaGroup;
import com.sun.syndication.feed.module.mediarss.types.Thumbnail;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author gerardomartinezgil
 * 
 */
public final class YouTubeService {
	private static final String API_KEY = "YOUTUBE_API_KEY";
	private static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/";
	private static final String SEARCH_VIDEO = "videos?";
	private static final String ORDERBY = "&relevance";
	private static final String FORMAT = "&format=1";
	private static final String TYPE = "&alt=rss";
	private static final String VERSION = "&v=2";
	
	private CoreService core;
	
	public YouTubeService(final CoreService core) {
		this.core = core;
	}

	/**
	 * 
	 * @param artistName
	 * @return
	 * @throws IOException
	 * @throws FeedException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public List<YouTubeVideo> searchArtistSongVideos(final String artistName,
			final String songName, int start, int rows)
			throws IllegalArgumentException, FeedException, IOException {
		List<YouTubeVideo> videos = null;
		if (artistName != null && artistName.trim().length() > 0) {
			final String urlAddress = YOUTUBE_URL
					+ SEARCH_VIDEO
					+ "q="
					+ URLEncoder.encode(artistName, "UTF-8")
					+ ((songName != null && songName.trim().length() > 0) ? URLEncoder
							.encode(" " + songName, "UTF-8")
							: "") + "&start-index=" + start + "&max-results="
					+ rows + "&key=" + API_KEY + FORMAT + TYPE + ORDERBY
					+ VERSION;
			if (core.cacheContains(urlAddress)) {
				return (List<YouTubeVideo>) core.getFromCache(urlAddress);
			}
			final URL url = new URL(urlAddress);
			final SyndFeedInput input = new SyndFeedInput();
			final SyndFeed feed = input.build(new XmlReader(url));
			if(feed.getEntries().size() > 0) {
				videos = new ArrayList<YouTubeVideo>();
				for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i
						.hasNext();) {
					SyndEntry entry = (SyndEntry) i.next();
					YouTubeVideo video = new YouTubeVideo();
					video.setTitle(entry.getTitle());
					MediaEntryModule m = (MediaEntryModule) entry
							.getModule(MediaEntryModule.URI);
					for (MediaGroup mg : m.getMediaGroups()) {
						video.setDescription(mg.getMetadata().getDescription());
						Thumbnail[] thumbnails = mg.getMetadata().getThumbnail();
						for (Thumbnail thumbnail : thumbnails) {
							if (thumbnail.getUrl().toString().endsWith(
									"/default.jpg"))
								video.setMediumThumbNailURL(thumbnail.getUrl()
										.toString());
							else if (thumbnail.getUrl().toString().endsWith(
									"/hqdefault.jpg"))
								video.setLargeThumbNailURL(thumbnail.getUrl()
										.toString());
						}
						for (MediaContent mc : mg.getContents()) {
							video.addVideo(mc.getReference().toString());
						}
					}
					videos.add(video);
				}
				core.addToCache(urlAddress, videos);
			}
		}
		return videos;
	}

}// END OF FILE