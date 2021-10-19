package ejb.session.stateless;

import entity.Staff;
import java.util.List;
import javax.ejb.Remote;
import util.exception.StaffNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Remote
public interface StaffSessionBeanRemote {
    
    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException;
    
    public Staff login(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createNewStaff(Staff staff);
    
    public List<Staff> viewAllStaffs() throws StaffNotFoundException;
   
}
