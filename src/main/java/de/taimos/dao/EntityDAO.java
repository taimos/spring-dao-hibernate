package de.taimos.dao;

import java.util.List;

/**
 * @author hoegertn
 * 
 * @param <T>
 *            the entity type
 * @param <U>
 *            the id type
 */
public interface EntityDAO<T extends IEntity<U>, U> {

	/**
	 * @param element
	 * @return the saved element
	 */
	T save(T element);

	/**
	 * @param element
	 */
	void delete(T element);

	/**
	 * @param id
	 */
	void deleteById(U id);

	/**
	 * @param id
	 * @return the element with the given id or null if not found
	 */
	T findById(U id);

	/**
	 * @return the list of elements
	 */
	List<T> findList();

}
