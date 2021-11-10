package ejb.session.stateless;

import entity.Guest;
import entity.Occupant;
import javax.ejb.Remote;
import util.exception.GuestEmailExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OccupantNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Remote
public interface GuestSessionBeanRemote {
    
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException;
    
    public Guest login(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createNewGuest(Guest guest) throws GuestEmailExistException, UnknownPersistenceException;
    
    public Long createNewOccupant(Occupant occupant);
    
    public Occupant retrieveOccupantById(Long occupantId) throws OccupantNotFoundException;
   
}
