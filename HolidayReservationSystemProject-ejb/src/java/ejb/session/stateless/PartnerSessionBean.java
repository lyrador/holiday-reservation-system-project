package ejb.session.stateless;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Partner;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeUsernameExistException;
import util.exception.PartnerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    public PartnerSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
    }  
    
    @PreDestroy
    public void preDestroy() {
    }

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);
        
        if(partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner does not exist: " + partnerId);
        }
    }
    
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Query query = em.createQuery("SELECT c FROM Partner c WHERE c.username = :inUsername");
            query.setParameter("inUsername", username);
            Partner partner = (Partner)query.getSingleResult();
            
            if(partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
    
    @Override
    public Long createNewPartner(Partner partner) throws PartnerUsernameExistException, UnknownPersistenceException {      
        try {
            em.persist(partner);
            em.flush();
            return partner.getPartnerId();
        }
        catch(PersistenceException ex) 
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")){
                    throw new PartnerUsernameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public List<Partner> viewAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");
        return query.getResultList();
    }
}
