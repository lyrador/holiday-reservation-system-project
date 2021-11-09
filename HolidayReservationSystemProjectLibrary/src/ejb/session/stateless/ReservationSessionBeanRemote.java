package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.Room;
import java.util.Date;
import java.util.List;
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
    
    //public Long createNewReservation(Reservation reservation);
    
    public List<Reservation> retrieveReservationsByCheckOutDate(Date checkOutDate);

    public List<Reservation> retrieveReservationsByCheckInDate(Date checkInDate);

    public List<Room> allocateRoomToCurrentDayReservations();

    public ExceptionReport createExceptionReport(ExceptionReport exceptionReport);
   
}
