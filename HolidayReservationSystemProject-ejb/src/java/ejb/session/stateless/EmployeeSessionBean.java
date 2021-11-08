package ejb.session.stateless;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Employee;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeUsernameExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    public EmployeeSessionBean() {
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
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);
        
        if(employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee does not exist: " + employeeId);
        }
    }
    
    @Override
    public Employee employeeLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            Query query = em.createQuery("SELECT c FROM Employee c WHERE c.email = :inEmail");
            query.setParameter("inEmail", email);
            Employee employee = (Employee)query.getSingleResult();
            
            if(employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
    
    @Override
    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException {
        try {
            em.persist(newEmployee);
            em.flush();

            return newEmployee.getEmployeeId();
        }
        catch(PersistenceException ex) 
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")){
                    throw new EmployeeUsernameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public List<Employee> viewAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");
        return query.getResultList();
    }
}
