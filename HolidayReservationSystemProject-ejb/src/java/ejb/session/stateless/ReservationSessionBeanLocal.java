package ejb.session.stateless;

import entity.Reservation;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Local
public interface ReservationSessionBeanLocal {

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    //public Long createNewReservation(Reservation reservation);
   
    
}
