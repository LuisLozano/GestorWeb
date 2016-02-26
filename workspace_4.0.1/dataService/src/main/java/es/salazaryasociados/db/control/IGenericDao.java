package es.salazaryasociados.db.control;

import java.util.List;
import java.util.Map;

import es.salazaryasociados.db.exceptions.DataException;

public interface IGenericDao<T> {

	public T persist(T item) throws DataException;
	public T update(T item);
	public List<T> getAll(int pageSize, int first, final Map<String, Object> params, String order, boolean desc);
	public long getCount (final Map<String, Object> params);
	public T getById(Integer id, String ... fetchs);
	public void delete(T item);
	public List<T> findByAttributes(Map<String, String> attributes, int pageSize, int first);
}
