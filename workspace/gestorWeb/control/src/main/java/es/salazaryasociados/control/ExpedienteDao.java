package es.salazaryasociados.control;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.salazaryasociados.control.exceptions.DataException;
import es.salazaryasociados.control.exceptions.GestorErrors;
import es.salazaryasociados.model.Expediente;

public class ExpedienteDao  extends GenericJPADao<Expediente>{
	
	private static Logger logger = LoggerFactory.getLogger(ExpedienteDao.class);

	public List<Expediente> getFilesGroupBy(int pageSize, int first, Map<String, Object> params, String order, boolean desc, String groupBy) throws DataException {
		
		StringBuffer groupByQuery =  new StringBuffer("(SELECT MIN(id) from ");
		groupByQuery.append(entityClass.getSimpleName());
		groupByQuery.append(" GROUP BY " + groupBy + ")");
		
		StringBuffer queryString = new StringBuffer("SELECT o from ");
		queryString.append(entityClass.getSimpleName());
        queryString.append(" o "); 
        queryString.append(this.getWhere(params, groupByQuery.toString()));
        queryString.append(orderBy(order, desc));
        
        try{
        	Query query = em.createQuery(queryString.toString());
            setQueryParameters(query, params);
            
            if (pageSize > 0 && first >= 0)
            {
            	query.setFirstResult(first);
            	query.setMaxResults(pageSize);
            }        
            return query.getResultList();        	
        }catch(Exception e)
        {
        	throw new DataException(GestorErrors.UNKNOWN_EXCEPTION, e);
        }
	}
	
	private Object getWhere(Map<String, Object> params, String groupByQuery) {
		
		String result = getWhere(params);
		if (result.length() > 0) {
			result += " and o.id in " + groupByQuery;
		}
		else {
			result = " where o.id in " + groupByQuery;
		}
		return result;
	}

	public long getCount(Map<String, Object> params, String groupBy) {
		
		StringBuffer groupByQuery =  new StringBuffer("(SELECT MIN(id) from ");
		groupByQuery.append(entityClass.getSimpleName());
		groupByQuery.append(" GROUP BY " + groupBy + ")");
		
		StringBuffer queryString = new StringBuffer("SELECT count(o) from ");
		queryString.append(entityClass.getSimpleName());
        queryString.append(" o ");         
        queryString.append(this.getWhere(params, groupByQuery.toString()));
        
        try{
        	Query query = em.createQuery(queryString.toString());
            setQueryParameters(query, params);
            
            return ((Long)query.getSingleResult()).longValue();        	
        }catch(Exception e)
        {
        	logger.error("Error al obtener los asuntos", e);
        	return 0;
        }            
	}
	
	public List<String> getSubjects(int pageSize, int first, Map<String, Object> params, String order, boolean desc) {
		
		StringBuffer queryString = new StringBuffer("SELECT distinct(o.asunto) from ");
		
        queryString.append(entityClass.getSimpleName());
        queryString.append(" o "); 
        queryString.append(this.getWhere(params));
        queryString.append(orderBy(order, desc));
        
        try{
        	Query query = em.createQuery(queryString.toString());
            setQueryParameters(query, params);
            
            if (pageSize > 0 && first >= 0)
            {
            	query.setFirstResult(first);
            	query.setMaxResults(pageSize);
            }        
            
            return query.getResultList();        	
        }catch(Exception e)
        {
        	logger.error("Error al obtener los asuntos", e);
        	return null;
        }
	}
	
    public long getSubjectsCount (final Map<String, Object> params) {
		StringBuffer queryString = new StringBuffer("SELECT count(distinct o.asunto) from ");
		
        queryString.append(entityClass.getSimpleName());
        queryString.append(" o "); 
        queryString.append(this.getWhere(params));
        
        try{
        	Query query = em.createQuery(queryString.toString());
            setQueryParameters(query, params);
            
            return ((Long)query.getSingleResult()).longValue();        	
        }catch(Exception e)
        {
        	logger.error("Error al obtener los asuntos", e);
        	return 0;
        }            
    }	
	
    public Integer getLastFileNumber() {
    	int result = 0;
		Query query = em.createQuery("select max(id) from " + entityClass.getSimpleName());
		result = (Integer)query.getSingleResult();
    	return result;
    }
    
	public void unify(List<String> selectedSubjects, String newSubject) throws DataException {

		try {
			if (selectedSubjects != null && selectedSubjects.size() > 0 && newSubject != null
					&& newSubject.length() > 0) {
				for (String subject : selectedSubjects) {
					
					Query query = em.createQuery(
							"update " + entityClass.getSimpleName() +" set observaciones = ''" + " where asunto = :antiguoAsunto and observaciones is null");
					query.setParameter("antiguoAsunto", subject);
					query.executeUpdate();
					
					query = em.createQuery(
							"update " + entityClass.getSimpleName() +" set asunto = :nuevoAsunto, observaciones = concat(observaciones, ' ', :antiguoAsunto)" + " where asunto = :antiguoAsunto");
					query.setParameter("antiguoAsunto", subject);
					query.setParameter("nuevoAsunto", newSubject);
					int result = query.executeUpdate();
					if (result <= 0) {
						logger.info("Unificar asuntos: Para el asunto " + subject + " no se ha cambiado nada");
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error unificando asuntos.", e);
			throw new DataException(GestorErrors.UNKNOWN_EXCEPTION, e.getMessage());
		}
	}
}
