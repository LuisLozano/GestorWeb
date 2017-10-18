package es.salazaryasociados.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import es.salazaryasociados.model.Evento;
import es.salazaryasociados.model.Responsable;

public class EventoDao extends GenericJPADao<Evento> {

	public List<Evento> getAllEventsFromResponsible(Responsable responsible) {
		List<Evento> result = new ArrayList<Evento>();
		
		String strQuery = "SELECT e from Evento e where (e.expediente.responsable1 = :resp)";
		Query query = em.createQuery(strQuery);
        query.setParameter("resp", responsible);
        
        result = query.getResultList();
		
		return result;
	}

	public List<Evento> getAllEvents(Date startDate, Date endDate) {
		List<Evento> result = new ArrayList<Evento>();
		
		String strQuery = "SELECT e from Evento e where (e.fechaInicio >= :start and e.fechaInicio <= :end)";		
		Query query = em.createQuery(strQuery);
        query.setParameter("start", startDate);
        query.setParameter("end", endDate);
        
        result = query.getResultList();
		
		return result;
	}
}
