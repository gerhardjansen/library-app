package de.gdevelop.library.app.common;

import java.util.Arrays;
import java.util.List;

import de.gdevelop.library.app.category.model.Category;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TestRepositoryEJB {

	@PersistenceContext
	private EntityManager em;

	private static final List<Class<?>> ENTITES_TO_REMOVE = Arrays.asList(Category.class);

	public void deleteAll() {
		for (final Class<?> entity : ENTITES_TO_REMOVE) {
			deleteAllForEntity(entity);
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteAllForEntity(final Class<?> entityClass) {
		final List<Object> entities = em.createQuery("Select e from " + entityClass.getSimpleName() + " e")
				.getResultList();
		for (final Object entity : entities) {
			em.remove(entity);
		}
	}

}
