package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Local
public interface GuestSessionBeanLocal {

    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException;

    public Guest login(String email, String password) throws InvalidLoginCredentialException;

    public Long createNewGuest(Guest guest);
    
}
