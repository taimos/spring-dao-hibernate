package de.taimos.dao;

/**
 * @author hoegertn
 * 
 * @param <T>
 *            id class
 */
public interface IEntity<T> {

	/**
	 * @return the id of the entity
	 */
	public T getId();

}
