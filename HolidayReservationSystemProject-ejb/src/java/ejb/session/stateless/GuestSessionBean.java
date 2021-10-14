package ejb.session.stateless;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Guest;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    public GuestSessionBean() {
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
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException {
        Guest guest = em.find(Guest.class, guestId);
        
        if(guest != null) {
            return guest;
        } else {
            throw new GuestNotFoundException("Guest does not exist: " + guestId);
        }
    }
    
    public Guest login(String email, String password) throws InvalidLoginCredentialException {
        try {
            Query query = em.createQuery("SELECT c FROM Guest c WHERE c.email = :inEmail");
            query.setParameter("inEmail", email);
            Guest guest = (Guest)query.getSingleResult();
            
            if(guest.getPassword().equals(password)) {
                return guest;
            } else {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
    
    @Override
    public Long createNewGuest(Guest guest)
    {
        em.persist(guest);
        em.flush();
        
        return guest.getGuestId();
    }
}
