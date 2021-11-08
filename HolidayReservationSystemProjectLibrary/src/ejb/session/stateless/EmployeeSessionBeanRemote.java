package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Remote
public interface EmployeeSessionBeanRemote {
    
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    
    public Employee employeeLogin(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createNewEmployee(Employee employee) throws EmployeeUsernameExistException, UnknownPersistenceException ;
    
    public List<Employee> viewAllEmployees();
   
}
