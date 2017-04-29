package de.gdevelop.library.app.category.repository;

import java.util.Comparator;
import java.util.List;

import de.gdevelop.library.app.category.model.Category;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CategoryRepository {

	@PersistenceContext
	EntityManager em;

	public Category add(final Category category) {
		em.persist(category);
		return category;
	}

	public Category findById(final Long id) {
		if (id == null)
			return null;
		return em.find(Category.class, id);
	}

	public Category findByName(final String categoryName) {
		if (categoryName == null)
			return null;

		Category cat = null;
		try {
			cat = em.createNamedQuery(Category.FINDBY_NAME, Category.class)
					.setParameter("name", categoryName)
					.getSingleResult();
		} catch (final NoResultException e) {
		}
		return cat;
	}

	public void update(final Category category) {
		em.merge(category);
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(final Comparator<Category> comparator) {
		final Query q = em.createNamedQuery(Category.FINDALL);
		List<Category> results = null;
		results = q.getResultList();

		results.sort(comparator);

		return results;
	}

	public boolean categoryExists(final Category category) {
		if (category.getId() != null) {
			return em.createNamedQuery(Category.FINDBY_NAME_AND_NOT_ID)
					.setParameter("name", category.getName())
					.setParameter("id", category.getId())
					.setMaxResults(1)
					.getResultList().size() > 0;

		} else {
			return em.createNamedQuery(Category.FINDBY_NAME)
					.setParameter("name", category.getName())
					.setMaxResults(1)
					.getResultList().size() > 0;
		}
	}

}
