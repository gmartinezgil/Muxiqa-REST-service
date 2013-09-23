/**
 * 
 */
package mx.com.muxiqa.services.core.http;

import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import mx.com.muxiqa.util.Util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * @author gerardomartinezgil
 * 
 */
public final class HttpService {
	private URLFetchService urlfetch;
	private DocumentBuilderFactory documentBuilderFactory;

	private static final Logger log = Logger.getLogger(HttpService.class
			.getName());

	/**
	 * 
	 */
	public HttpService() {
		urlfetch = URLFetchServiceFactory.getURLFetchService();
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public byte[] getContentAsBytesFrom(final String url) {
		if (url != null && url.trim().length() > 0) {
			try {
				final URL toGo = new URL(url);
				final HTTPRequest request = new HTTPRequest(toGo, HTTPMethod.GET);
				final HTTPResponse response = urlfetch.fetch(request);
				if (response.getResponseCode() == 200) {
					final byte[] content = response.getContent();
					if (content != null && content.length > 0) {
						return content;
					}
				}
			} catch (Exception e) {
				log.warning(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public Document getContentAsDocumentFrom(final String url) {
		if (url != null && url.trim().length() > 0) {
			final String content = getContentAsStringFrom(url);
			if (content != null && content.length() > 0
					&& content.startsWith("<?xml")) {
				Document doc = null;
				try {
					doc = documentBuilderFactory.newDocumentBuilder()
							.parse(
									new InputSource(new StringReader(
											content)));
				} catch (Exception e) {
					final String contentWithoutInvalidChars = Util
							.removeNonValidCharacters(content);
					try {
						doc = documentBuilderFactory
								.newDocumentBuilder()
								.parse(
										new InputSource(new StringReader(
												contentWithoutInvalidChars)));
					} catch (Exception e1) {
					}
				} finally {
					if (doc != null) {
						return doc;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	private String getContentAsStringFrom(final String url) {
		if (url != null && url.trim().length() > 0) {
			try {
				final URL toGo = new URL(url);
				final HTTPRequest request = new HTTPRequest(toGo,
						HTTPMethod.GET);
				// HTTPHeader header = new HTTPHeader(name, value)
				final HTTPResponse response = urlfetch.fetch(request);
				if (response.getResponseCode() == 200) {
					final String content = new String(response.getContent(),
							"UTF-8");
					if (content != null && content.length() > 0) {
						return content;
					}
				} else if (response.getResponseCode() >= 300
						&& response.getResponseCode() < 400) {
					Iterator<HTTPHeader> iterator = response.getHeaders()
							.iterator();
					while (iterator.hasNext()) {
						HTTPHeader httpHeader = (HTTPHeader) iterator.next();
						if (httpHeader.getName().equals("location")) {
							final String newURL = httpHeader.getValue();
							if (newURL != null && newURL.trim().length() > 0) {
								getContentAsStringFrom(newURL);
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				log.warning(e.getMessage());
			}
		}
		return null;
	}

}// END OF FILE