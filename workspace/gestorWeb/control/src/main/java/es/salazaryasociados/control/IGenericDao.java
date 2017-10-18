package es.salazaryasociados.control;

import java.util.List;
import java.util.Map;

import es.salazaryasociados.control.exceptions.DataException;

public interface IGenericDao<T> {

	public T persist(T item) throws DataException;
	public T update(T item) throws DataException;
	public List<T> getAll(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws Exception;
	public long getCount (final Map<String, Object> params) throws DataException;
	public T getById(Integer id, String ... fetchs) throws DataException;
	public void delete(T item) throws DataException;
	public List<T> findByAttributes(Map<String, String> attributes, int pageSize, int first) throws DataException;
}
