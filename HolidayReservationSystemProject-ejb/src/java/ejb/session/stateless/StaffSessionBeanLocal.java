package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Local;
import util.exception.StaffNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Local
public interface StaffSessionBeanLocal {

    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException;

    public Staff login(String email, String password) throws InvalidLoginCredentialException;

    public Long createNewStaff(Staff staff);
    
}
