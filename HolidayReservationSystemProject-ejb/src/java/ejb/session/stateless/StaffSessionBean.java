package ejb.session.stateless;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Staff;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exception.StaffNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
public class StaffSessionBean implements StaffSessionBeanRemote, StaffSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    public StaffSessionBean() {
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
    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException {
        Staff staff = em.find(Staff.class, staffId);
        
        if(staff != null) {
            return staff;
        } else {
            throw new StaffNotFoundException("Staff does not exist: " + staffId);
        }
    }
    
    public Staff login(String email, String password) throws InvalidLoginCredentialException {
        try {
            Query query = em.createQuery("SELECT c FROM Staff c WHERE c.email = :inEmail");
            query.setParameter("inEmail", email);
            Staff staff = (Staff)query.getSingleResult();
            
            if(staff.getPassword().equals(password)) {
                return staff;
            } else {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
    
    @Override
    public Long createNewStaff(Staff staff)
    {
        em.persist(staff);
        em.flush();
        
        return staff.getStaffId();
    }
    
    @Override
    public List<Staff> viewAllStaffs() throws StaffNotFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM Staff r");
            return query.getResultList();
        } catch(Exception e) { 
            throw new StaffNotFoundException("Staff not found");
        }
    }
}
