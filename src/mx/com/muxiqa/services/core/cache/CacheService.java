/**
 * 
 */
package mx.com.muxiqa.services.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

/**
 * @author gerardomartinezgil
 *
 */
public final class CacheService {
	public static final Logger logger = Logger
	.getLogger(CacheService.class.getName());

	private Cache cache;
	/**
	 * 
	 */
	public CacheService() {
		Map<Integer, Integer> props = new HashMap<Integer, Integer>();
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		try {
			CacheFactory cacheFactory = CacheManager.getInstance()
					.getCacheFactory();
			cache = cacheFactory.createCache(props);
		} catch (CacheException e) {
			logger.warning(e.getMessage());
		}
	}
	
	public boolean containsKey(final Object key) {
		return cache.containsKey(key);
	}
	
	public Object get(Object key) {
		return cache.get(key);
	}
	
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

}