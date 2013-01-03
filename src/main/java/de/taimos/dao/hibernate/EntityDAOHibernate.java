package de.taimos.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.taimos.dao.EntityDAO;
import de.taimos.dao.IEntity;
import de.taimos.dao.exceptions.ConstraintException;
import de.taimos.dao.exceptions.DAOException;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hoegertn
 * 
 * @param <T>
 *            the entity type
 * @param <U>
 *            the id type
 */
public abstract class EntityDAOHibernate<T extends IEntity<U>, U> implements EntityDAO<T, U> {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public T save(final T element) {
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
	public void delete(final T element) {
		try {
			this.entityManager.remove(element);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public T findById(final U id) {
		try {
			return this.entityManager.find(this.getEntityClass(), id);
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<T> findList() {
		try {
			final TypedQuery<T> query = this.entityManager.createQuery(this.getFindListQuery(), this.getEntityClass());
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

	protected T findByQuery(final String query, final Object... params) {
		try {
			final List<T> list = this.findListByQuery(query, params);
			if (list.size() == 1) {
				return list.get(0);
			}
			return null;
		} catch (final HibernateException e) {
			throw new DAOException(e);
		}
	}

	protected List<T> findListByQuery(final String query, final Object... params) {
		try {
			final TypedQuery<T> tq = this.entityManager.createQuery(query, this.getEntityClass());
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

	protected abstract Class<T> getEntityClass();

}
