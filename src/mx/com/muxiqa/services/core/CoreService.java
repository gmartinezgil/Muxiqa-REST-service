/**
 * 
 */
package mx.com.muxiqa.services.core;

import org.w3c.dom.Document;

import mx.com.muxiqa.services.core.cache.CacheService;
import mx.com.muxiqa.services.core.db.DatabaseService;
import mx.com.muxiqa.services.core.db.entities.Device;
import mx.com.muxiqa.services.core.db.entities.User;
import mx.com.muxiqa.services.core.http.HttpService;

/**
 * @author gerardomartinezgil
 * 
 */
public final class CoreService {
	// the core services...
	private CacheService cacheService;
	private DatabaseService databaseService;
	private HttpService httpService;

	/**
	 * 
	 */
	public CoreService() {
		databaseService = new DatabaseService();
		cacheService = new CacheService();
		httpService = new HttpService();
	}

	// ///////////////////CACHE////////////////////////
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addToCache(final Object key, final Object value) {
		cacheService.put(key, value);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getFromCache(final Object key) {
		return cacheService.get(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean cacheContains(final Object key) {
		return cacheService.containsKey(key);
	}

	// ///////////////////HTTP////////////////////////
	/**
	 * 
	 * @param url
	 * @return
	 */
	public Document getHTTPContentAsXMLDocument(final String url) {
		return httpService.getContentAsDocumentFrom(url);
	}

	// ///////////////////DATABASE////////////////////////
	/**
	 * 
	 * @param device
	 * @return
	 */
	public long registerDeviceInDatabase(final Device device) {
		return databaseService.registerDevice(device);
	}

	/**
	 * 
	 * @param deviceId
	 * @return
	 */
	public User getUserByDeviceFromDatabase(final long deviceId) {
		return databaseService.getUserByDeviceId(deviceId);
	}

}// END OF FILE