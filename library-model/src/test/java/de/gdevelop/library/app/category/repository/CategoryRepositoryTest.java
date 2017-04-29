package de.gdevelop.library.app.category.repository;

import static de.gdevelop.library.app.category.CategoryForRepositoryTests.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.List;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.common.DBCommandTxExecutor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CategoryRepositoryTest {

	private EntityManagerFactory emf;
	private EntityManager em;

	private CategoryRepository cut;
	private DBCommandTxExecutor dbCommandExecutor;

	@Before
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		dbCommandExecutor = new DBCommandTxExecutor(em);
		cut = new CategoryRepository();
		cut.em = em;
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryAndFindIt() {

		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return cut.add(java()).getId();
		});

		// Alternativ dazu die Implementierung mit anonymer innerer Klasse
		//		final Long categoryAddedId = dbCommandExecutor.executeCommand(new DBCommand<Long>() {
		//
		//			@Override
		//			public Long execute() {
		//				return cut.add(java()).getId();
		//			}
		//		});

		assertThat(categoryAddedId, is(notNullValue()));

		final Category category = cut.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));

	}

	@Test
	public void findCategoryByIdWithUnknownId() {
		final Category category = cut.findById(-999l);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryByIdWithNullId() {
		final Category category = cut.findById(null);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryByName() {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return cut.add(java()).getId();
		});

		assertThat(categoryAddedId, is(notNullValue()));

		final Category category = dbCommandExecutor.executeCommand(() -> {
			return cut.findByName(java().getName());
		});
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
		// returned category should have id of added cat
		assertThat(category.getId(), is(categoryAddedId));
	}

	@Test
	public void findCategoryWithNullName() {
		final Category category = cut.findByName(null);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryWithUnknownName() {
		final Category category = cut.findByName(networks().getName());
		assertThat(category, is(nullValue()));
	}

	@Test
	public void updateCategory() {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return cut.add(java()).getId();
		});

		final Category addedCategory = cut.findById(categoryAddedId);
		System.out.println("Added category: " + addedCategory);
		assertThat(addedCategory.getName(), is(equalTo(java().getName())));

		addedCategory.setName(networks().getName());

		dbCommandExecutor.executeCommand(() -> {
			cut.update(addedCategory);
			return null;
		});

		final Category updatedCategory = cut.findById(categoryAddedId);
		assertThat(updatedCategory.getName(), is(equalTo(networks().getName())));

	}

	@Test
	public void findAllCategories() {
		dbCommandExecutor.executeCommand(() -> {
			allCategories().forEach(cut::add);
			return null;
		});

		// alternativ mit for-loop
		//		dbCommandExecutor.executeCommand(() -> {
		//			for (final Category category : allCategories()) {
		//				cut.add(category);
		//			}
		//		});

		final Comparator<Category> comparator = (c1, c2) -> c1.getName().compareTo(c2.getName());
		final List<Category> categories = cut.findAll(comparator);
		assertThat(categories.size(), is(4));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));

	}

	@Test
	public void alreadyExistsAfterAdd() {
		dbCommandExecutor.executeCommand(() -> {
			return cut.add(java()).getId();
		});
		assertThat(cut.categoryExists(java()), is(true));
		assertThat(cut.categoryExists(networks()), is(false));
	}

	@Test
	public void alreadyExistsWithId() {
		final Category java = dbCommandExecutor.executeCommand(() -> {
			cut.add(cleanCode());
			return cut.add(java());
		});

		System.out.println("category: " + java);
		assertThat(cut.categoryExists(java), is(false));

		java.setName(cleanCode().getName());
		assertThat(cut.categoryExists(java), is(true));

		java.setName(networks().getName());
		assertThat(cut.categoryExists(java), is(false));
	}

	@Test
	public void existsById() {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return cut.add(java()).getId();
		});

		assertThat(cut.findById(categoryAddedId), is(notNullValue()));
	}
}
