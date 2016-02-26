package es.salazaryasociados.db.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import es.salazaryasociados.db.model.Role;
import es.salazaryasociados.db.model.RoleVSUser;
import es.salazaryasociados.db.model.User;

public class UserDao extends GenericJPADao<User> {

	public User getUser(String userName) {

		User result = null;
		
		StringBuffer queryString = new StringBuffer("SELECT u from User u where u.username = :theName");		
        try{
        	Query query = em.createQuery(queryString.toString());
        	query.setParameter("theName", userName);
            
            List<User> users = query.getResultList();  
            if (users != null && users.size() > 0)
            	result = users.get(0);
        }catch(Exception e)
        {
        	//TODO: log
        	e.printStackTrace();
        	result = null;
        }		
		return result;
	}

	public List<Role> getAllRoles(int pageSize, int first,
			Map<String, Object> params, String order, boolean desc) {
		RoleDao roleDao = new RoleDao();
		roleDao.setEntityManager(em);
		
		return roleDao.getAll(pageSize, first, params, order, desc);
	}
	
	public User update(User item) {
        if (item == null)
            throw new PersistenceException("Item may not be null");
        
        /*
        User user = getById(item.getId());
        List<Role> roles = new ArrayList<Role>();
        
        for (Role role : user.getRoles()){
        	if (!item.getRoles().contains(role)) {
        		roles.add(role);
        	}
        }
        user.getRoles().removeAll(roles);
        
        for (Role role : item.getRoles()){
        	if (!user.getRoles().contains(role)){
        		user.getRoles().add(role);
        	}
        }
        
        return em.merge(user);*/
        return em.merge(item);
    }	
}

class RoleDao extends GenericJPADao<Role>{
	
}

class RoleVsUserDao extends GenericJPADao<RoleVSUser>{

	public void deleteAll(Integer userID) {
		StringBuffer queryString = new StringBuffer("DELETE from RoleVSUser r where r.user = :userID");		
        try{
        	Query query = em.createQuery(queryString.toString());
        	query.setParameter("userID", userID);

        	query.executeUpdate(); 
        }catch(Exception e)
        {
        	//TODO: log
        	e.printStackTrace();
        }				
	}
	
}
