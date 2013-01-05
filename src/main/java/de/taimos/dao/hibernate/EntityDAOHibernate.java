package de.taimos.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.taimos.dao.EntityDAO;
import de.taimos.dao.IEntity;
import de.taimos.dao.exceptions.ConstraintException;
import de.taimos.dao.exceptions.DAOException;
import de.taimos.dao.exceptions.EntityNotFoundException;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hoegertn
 * 
 * @param <E>
 *            the entity type
 * @param <I>
 *            the id type
 */
public abstract class EntityDAOHibernate<E extends IEntity<I>, I> implements EntityDAO<E, I> {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public E save(final E element) {
		try {
			return this.entityManager.merge(element);
		} catch (final ConstraintViolationException e) {
			throw new ConstraintException(e);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@Transactional
	public void delete(final E element) {
		try {
			this.entityManager.remove(this.entityManager.merge(element));
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void deleteById(final I id) {
		try {
			final E element = this.findById(id);
			if (element == null) {
				throw new EntityNotFoundException();
			}
			this.entityManager.remove(element);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public E findById(final I id) {
		try {
			return this.entityManager.find(this.getEntityClass(), id);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<E> findList() {
		try {
			final TypedQuery<E> query = this.entityManager.createQuery(this.getFindListQuery(), this.getEntityClass());
			return query.getResultList();
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Override in sub-class if not "id"
	 * 
	 * @return the id field
	 */
	protected String getIdField() {
		return "id";
	}

	/**
	 * @param query
	 * @param params
	 * @return the entity or null if not found or more than one found
	 */
	protected E findByQuery(final String query, final Object... params) {
		try {
			final List<E> list = this.findListByQuery(query, params);
			if (list.size() == 1) {
				return list.get(0);
			}
			return null;
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	protected List<E> findListByQuery(final String query, final Object... params) {
		try {
			final TypedQuery<E> tq = this.entityManager.createQuery(query, this.getEntityClass());
			for (int i = 0; i < params.length; i++) {
				tq.setParameter(i + 1, params[i]);
			}
			return tq.getResultList();
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Override in sub-class if not "FROM 'EntityName'"
	 * 
	 * @return the select query
	 */
	protected String getFindListQuery() {
		return "FROM " + this.getEntityClass().getSimpleName();
	}

}
