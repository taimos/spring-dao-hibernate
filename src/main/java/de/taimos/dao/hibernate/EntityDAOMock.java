package de.taimos.dao.hibernate;

/*
 * #%L
 * Hibernate DAO for Spring
 * %%
 * Copyright (C) 2013 - 2015 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import de.taimos.dao.IEntity;
import de.taimos.dao.IEntityDAO;

/**
 * @author hoegertn
 * 
 * @deprecated use in memory db and regular DAOs
 * 
 * @param <E> the entity type
 * @param <I> the id type
 */
@Deprecated
public abstract class EntityDAOMock<E extends IEntity<I>, I> implements IEntityDAO<E, I> {
	
	protected final ConcurrentHashMap<I, E> entities = new ConcurrentHashMap<>();
	
	
	@Override
	@Transactional
	public E save(final E element) {
		this.entities.put(element.getId(), element);
		return element;
	}
	
	@Override
	@Transactional
	public void delete(final E element) {
		this.deleteById(element.getId());
	}
	
	@Override
	public void deleteById(final I id) {
		E remove = this.entities.remove(id);
		if (remove == null) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public E findById(final I id) {
		if (this.entities.containsKey(id)) {
			return this.entities.get(id);
		}
		throw new EntityNotFoundException();
	}
	
	@Override
	public List<E> findList(final int first, final int max) {
		List<E> values = new ArrayList<>(this.entities.values());
		this.sortById(values);
		
		if (first >= 0) {
			if (max >= 0) {
				return Collections.unmodifiableList(values.subList(first, Math.max(first + max, values.size())));
			}
			return Collections.unmodifiableList(values.subList(first, values.size()));
		}
		if (max >= 0) {
			return Collections.unmodifiableList(values.subList(0, Math.max(first + max, values.size())));
		}
		return Collections.unmodifiableList(values);
	}
	
	protected void sortById(List<E> values) {
		Collections.sort(values, new Comparator<E>() {
			
			@Override
			@SuppressWarnings({"unchecked", "rawtypes"})
			public int compare(E o1, E o2) {
				return ((Comparable) o1.getId()).compareTo(o2.getId());
			}
		});
	}
	
	@Override
	public List<E> findList() {
		return this.findList(-1, -1);
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
		final List<E> list = this.findListByQuery(query, params);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}
	
	protected List<E> findListByQuery(final String query, final Object... params) {
		return this.findListByQuery(query, -1, -1, params);
	}
	
	protected List<E> findListByQueryLimit(final String query, final int first, final int max, final Object... params) {
		Collection<E> values = this.entities.values();
		List<E> filtered = new ArrayList<>();
		for (E e : values) {
			if (this.findListByQueryLimitFilter(e, query, params)) {
				filtered.add(e);
			}
		}
		
		List<E> sorted = this.findListByQueryLimitSort(filtered, query, params);
		
		if (first >= 0) {
			if (max >= 0) {
				return Collections.unmodifiableList(sorted.subList(first, Math.max(first + max, values.size())));
			}
			return Collections.unmodifiableList(sorted.subList(first, values.size()));
		}
		if (max >= 0) {
			return Collections.unmodifiableList(sorted.subList(0, Math.max(first + max, values.size())));
		}
		return Collections.unmodifiableList(sorted);
	}
	
	protected abstract List<E> findListByQueryLimitSort(List<E> filtered, String query, Object... params);
	
	protected abstract boolean findListByQueryLimitFilter(E e, String query, Object... params);
	
	/**
	 * Override in sub-class if not "FROM 'EntityName'"
	 * 
	 * @return the select query
	 */
	protected String getFindListQuery() {
		return "FROM " + this.getEntityClass().getSimpleName();
	}
	
}
