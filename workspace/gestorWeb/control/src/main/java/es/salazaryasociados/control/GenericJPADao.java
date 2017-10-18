package es.salazaryasociados.control;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import es.salazaryasociados.control.exceptions.DataException;
import es.salazaryasociados.control.exceptions.GestorErrors;

public abstract class GenericJPADao <T> implements IGenericDao<T>{

	private static Validator validator;
	@PersistenceContext(unitName="salazarPU")
	protected EntityManager em;
	 
    public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	protected Class<T> entityClass;
 
    public Class<T> getEntityClass() {
        return entityClass;
    }
 
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
 
    public GenericJPADao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
 
    public GenericJPADao(Class clazz) {
        this.entityClass = clazz;
    }
 
	 public void setEntityManager(EntityManager em) {
		 this.em = em;
	 }
 
    public T persist(T item) throws DataException {
    	
    	T result = item;
    	
        if (item == null)
            throw new PersistenceException("Item may not be null");
        
        validate(item);
    	em.persist(item);
        return result;
    }
 
	public T update(T item) {
        if (item == null)
            throw new PersistenceException("Item may not be null");
        validate(item);
        em.merge(item);
        return item;
    }
 
    public List<T> getAll(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException{

		StringBuffer queryString = new StringBuffer("SELECT o from ");
		
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
        	throw new DataException(GestorErrors.UNKNOWN_EXCEPTION, e);
        }
    }
    
    public long getCount (final Map<String, Object> params) throws DataException {
		StringBuffer queryString = new StringBuffer("SELECT count(o) from ");
		
        queryString.append(entityClass.getSimpleName());
        queryString.append(" o "); 
        queryString.append(this.getWhere(params));
        
        try{
        	Query query = em.createQuery(queryString.toString());
            setQueryParameters(query, params);
            
            return ((Long)query.getSingleResult()).longValue();        	
        }catch(Exception e)
        {
        	throw new DataException(GestorErrors.UNKNOWN_EXCEPTION, e);
        }            
    }

	protected String orderBy(String order, boolean desc) {
		String result = "";
		if (order != null && order.length() > 0)
		{
			result += " order by o." + order;
			if (desc)
			{
				result += " desc";
			}
		}
		return result;
	}

	protected String getWhere(Map<String, Object> params) {
		String result = "";
		if (params != null && params.size() > 0)
		{
			result += "where ";
			int i = 0;
			for (String field : params.keySet())
			{
				Object value = params.get(field);
				if (value instanceof String)
				{
					String strVal = ((String)params.get(field)).toUpperCase();
					result += "UPPER(o." + field + ") like \'%" + strVal + "%\'";
				}
				else
				{
					result += "o." + field + "=:p" + field;
				}
				i++;
				if (i < params.size())
				{
					result += " and ";
				}
			}
		}
		return result;
	}
	
	protected void setQueryParameters(Query query, Map<String, Object> params) {
		if (params != null && params.size() > 0)
		{
			for (String field : params.keySet())
			{
				Object value = params.get(field);
				if (value instanceof String)
				{
				}
				else
				{
					query.setParameter("p" + field, params.get(field));
				}								
			}
		}		
	}
	

	public T getById(Integer id, String ... fetchs)  throws DataException{
        if (id == null)
            throw new PersistenceException("Id may not be null");
 
        T result = null;
        if (fetchs != null){
        	
        	EntityGraph<T> graph = em.createEntityGraph(entityClass);
        	
        	try {
	        	for (String fetch : fetchs){            	
	            	graph.addSubgraph(fetch);
	        	}
	
	        	Map<String, Object> hints = new HashMap<String, Object>();
	        	hints.put("javax.persistence.loadgraph", graph);
	        	result = em.find(entityClass, id, hints);
        	}catch(IllegalArgumentException | IllegalStateException e) {
        		throw new PersistenceException("Error al obtener el objeto por su id.", e);
        	}
        }
        else
        {
        	result = em.find(entityClass, id);
        }
        return result;
    }
 
    public void delete(T item) {
        if (item == null)
            throw new PersistenceException("Item may not be null");
 
        em.remove(em.merge(item));
    }
 
    public List<T> findByAttributes(Map<String, String> attributes, int pageSize, int first) {
        //set up the Criteria query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> foo = cq.from(getEntityClass());
 
        List<Predicate> predicates = new ArrayList<Predicate>();
        for(String s : attributes.keySet())
        {
            if(foo.get(s) != null){
                predicates.add(cb.like(foo.get(s), "%" + attributes.get(s) + "%" ));
            }
        }
        cq.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<T> q = em.createQuery(cq);
 
        if (first >= 0 && pageSize > 0)
        {
        	q.setFirstResult(first);
        	q.setMaxResults(pageSize);
        }
        return q.getResultList();
    }
    
    protected static void validate(Object o) throws ValidationException {

    	if (validator == null) {
	        ValidatorFactory factory = Validation.byDefaultProvider().providerResolver(new OsgiValidationProvider()).configure()
	                .buildValidatorFactory();
	        validator = factory.getValidator();
    	}

        Set<ConstraintViolation<Object>> validatorResult = validator.validate(o);

        if (!validatorResult.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<Object> v : validatorResult) {
                sb.append(v.getMessage() + ". ");
            }

            String msg = sb.toString().substring(0, sb.toString().length() - 1);

            throw new ValidationException(msg);
        }
    }    
}
