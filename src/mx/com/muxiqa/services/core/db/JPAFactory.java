/**
 * 
 */
package mx.com.muxiqa.services.core.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author gerardomartinezgil
 * 
 */
public final class JPAFactory {
	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("transactions-optional");

	private JPAFactory() {
	}

	public static EntityManagerFactory get() {
		return emfInstance;
	}

}