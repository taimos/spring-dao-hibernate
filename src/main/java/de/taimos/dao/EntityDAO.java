package de.taimos.dao;

import java.util.List;

/**
 * @author hoegertn
 * 
 * @param <E>
 *            the entity type
 * @param <I>
 *            the id type
 */
public interface EntityDAO<E extends IEntity<I>, I> {

	/**
	 * @param element
	 * @return the saved element
	 */
	E save(E element);

	/**
	 * @param element
	 */
	void delete(E element);

	/**
	 * @param id
	 */
	void deleteById(I id);

	/**
	 * @param id
	 * @return the element with the given id or null if not found
	 */
	E findById(I id);

	/**
	 * @return the list of elements
	 */
	List<E> findList();

	/**
	 * @return the entity class
	 */
	Class<E> getEntityClass();

}
