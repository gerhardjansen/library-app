package de.gdevelop.library.app.common;

import javax.persistence.EntityManager;

import org.junit.Ignore;

@Ignore
public class DBCommandTxExecutor {

	private EntityManager em;

	public DBCommandTxExecutor(final EntityManager em) {
		this.em = em;
	}

	public <T> T executeCommand(final DBCommand<T> dbCommand) {
		try {
			em.getTransaction().begin();
			final T retVal = dbCommand.execute();
			em.getTransaction().commit();
			em.clear();
			return retVal;
		} catch (final Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			throw new IllegalStateException(e);
		}
	}
}
