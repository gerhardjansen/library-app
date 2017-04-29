package de.gdevelop.library.app.category.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "category")
@NamedQueries({
		@NamedQuery(
				name = Category.FINDALL,
				query = "SELECT e FROM Category e"),
		@NamedQuery(
				name = Category.FINDBY_NAME,
				query = "SELECT e FROM Category e where e.name = :name"),
		@NamedQuery(
				name = Category.FINDBY_NAME_AND_NOT_ID,
				query = "SELECT e FROM Category e where e.name = :name AND e.id != :id ")
})
public class Category implements Serializable {

	private static final String PACKAGE = "de.gdevelop.library.app.category.model.Category.";
	public static final String FINDALL = PACKAGE + ".findAll";
	public static final String FINDBY_NAME = PACKAGE + ".findByName";
	public static final String FINDBY_NAME_AND_NOT_ID = PACKAGE + ".findByNameAndNotId";

	private static final long serialVersionUID = -4473636668727157532L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 2, max = 25)
	@Column(unique = true)
	private String name;

	public Category() {
	}

	public Category(final String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}

}
