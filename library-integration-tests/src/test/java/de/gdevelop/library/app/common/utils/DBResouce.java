package de.gdevelop.library.app.common.utils;

import de.gdevelop.library.app.common.TestRepositoryEJB;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

@Path("/DB")
public class DBResouce {

	@Inject
	TestRepositoryEJB testRepositoryEJB;

	@DELETE
	public void deleteAll() {
		testRepositoryEJB.deleteAll();
	}
}
