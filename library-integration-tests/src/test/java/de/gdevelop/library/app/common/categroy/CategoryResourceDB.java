package de.gdevelop.library.app.common.categroy;

import static de.gdevelop.library.app.category.CategoryForRepositoryTests.*;

import de.gdevelop.library.app.category.model.Category;
import de.gdevelop.library.app.category.services.CategoryServices;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
@Path("/DB/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResourceDB {

	Logger log = LoggerFactory.getLogger(CategoryResourceDB.class);

	@Inject
	private CategoryServices categoryServices;

	@POST
	public void addAll() {
		for (final Category category : allCategories()) {
			log.info("+++++++++ Adding category: " + category.toString() + " ++++++++++++");
			categoryServices.add(category);
		}
	}
}
