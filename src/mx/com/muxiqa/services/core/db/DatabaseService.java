/**
 * 
 */
package mx.com.muxiqa.services.core.db;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import mx.com.muxiqa.services.core.db.entities.Device;
import mx.com.muxiqa.services.core.db.entities.Location;
import mx.com.muxiqa.services.core.db.entities.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author gerardomartinezgil
 * 
 */
public final class DatabaseService {
	// the log...
	private static final Logger log = Logger.getLogger(DatabaseService.class
			.getName());

	// the key to the datastore...
	private EntityManager session;

	/**
	 * Initializes the JPAFactory for getting the session to the datastore...
	 * 
	 */
	public DatabaseService() {
		session = JPAFactory.get().createEntityManager();
	}

	// /////////////////////DEVICE///////////////////////////
	//TODO:should take in account if the user removes the storage(RMS in mobile, for example) when she/him updates the application
	//have to take in account the UUID of the device when searching for it...if it's not already registered.
	/**
	 * register for the first time a device which uses the muxiqa system...only once
	 */
	public long registerDevice(final Device device) {
		if (device != null) {
			EntityTransaction tx = null;
			try {
				tx = session.getTransaction();
				tx.begin();
				User user = new User();
				session.persist(user);
				device.setFromUser(user);
				session.persist(device);
				session.flush();
				tx.commit();
				return device.getId().getId();
			} catch (Throwable t) {
				tx.rollback();
				log.warning(t.getMessage());
			}
		}
		return -1;
	}

	/**
	 * Looking for a device already registered...
	 * 
	 * @param id
	 * @return
	 */
	public Device getDevice(final long id) {
		if (id > 0) {
			final Key key = KeyFactory.createKey(Device.class.getSimpleName(),
					id);
			return (Device) session.createQuery(
					"SELECT d FROM " + Device.class.getSimpleName()
							+ " d WHERE d.id = '" + KeyFactory.keyToString(key)
							+ "'").getSingleResult();
		}
		return null;
	}
	
	/**
	 * Register the location for a device 
	 * @param location
	 * @param device
	 */
	public void registerDeviceLocation(Location location, final Device device) {
		if(location != null) {
			EntityTransaction tx = null;
			try {
				tx = session.getTransaction();
				tx.begin();
				location.setFromDevice(device);
				session.persist(location);
				session.flush();
				tx.commit();
			} catch (Throwable t) {
				tx.rollback();
				log.warning(t.getMessage());
			}
		}
	}
	
	// /////////////////////USER///////////////////////////

	/**
	 * 
	 * @param user
	 */
	public long saveUser(final User user) {
		if (user != null) {
			EntityTransaction tx = null;
			try {
				tx = session.getTransaction();
				tx.begin();
				session.persist(user);
				session.flush();
				tx.commit();
				return user.getId().getId();
			} catch (Throwable t) {
				tx.rollback();
				log.warning(t.getMessage());
			}
		}
		return -1;
	}

	/**
	 * Gets a user by referencing her/his device...
	 * 
	 * @param deviceId
	 * @return
	 */
	public User getUserByDeviceId(final long deviceId) {
		if(deviceId > 0) {
			Device device = getDevice(deviceId);
			if(device != null) {
				return device.getFromUser();
			}
		}
		return null;
	}

	
	/**
	 * Gets all users from the storage.
	 * @return
	 */
	/*
	@SuppressWarnings("unchecked")
	private List<User> getAllUsers() {
		return session.createQuery(
				"SELECT u FROM " + User.class.getSimpleName() + " u")
				.getResultList();
	}
	*/

}// END OF FILE