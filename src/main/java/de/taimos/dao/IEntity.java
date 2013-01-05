package de.taimos.dao;

/**
 * @author hoegertn
 * 
 * @param <I>
 *            id class
 */
public interface IEntity<I> {

	/**
	 * @return the id of the entity
	 */
	public I getId();

}
