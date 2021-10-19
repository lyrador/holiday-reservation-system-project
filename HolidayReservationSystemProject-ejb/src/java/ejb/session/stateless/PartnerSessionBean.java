package ejb.session.stateless;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Partner;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exception.PartnerNotFoundException;
import util.exception.InvalidLoginCredentialException;

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
    
    public Partner login(String email, String password) throws InvalidLoginCredentialException {
        try {
            Query query = em.createQuery("SELECT c FROM Partner c WHERE c.email = :inEmail");
            query.setParameter("inEmail", email);
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
    public Long createNewPartner(Partner partner)
    {
        em.persist(partner);
        em.flush();
        
        return partner.getPartnerId();
    }
    
    @Override
    public List<Partner> viewAllPartners() throws PartnerNotFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM Partner r");
            return query.getResultList();
        } catch(Exception e) { 
            throw new PartnerNotFoundException("Partner not found");
        }
    }
}
