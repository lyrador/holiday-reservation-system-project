package ejb.session.stateless;

import entity.Reservation;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Remote
public interface ReservationSessionBeanRemote {
    
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;
    
    public Long createNewReservation(Reservation reservation);
   
}
