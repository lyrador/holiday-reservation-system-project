package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.Room;
import java.util.Date;
import java.util.List;
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

    public Long createReservation(Reservation reservation, Long guestId, Long roomTypeId);

    public List<Reservation> retrieveReservationsByCheckOutDate(Date checkOutDate);

    public List<Reservation> retrieveReservationsByCheckInDate(Date checkInDate);

    public List<Room> allocateRoomToCurrentDayReservations(Date today);

    public ExceptionReport createExceptionReport(ExceptionReport exceptionReport);
   
    public List<Reservation> viewAllMyReservations(Long guestId);

    public List<Reservation> retrieveReservationByOccupantId(Long occupantId) throws ReservationNotFoundException;

    public List<Reservation> retrieveReservationByGuestId(Long guestId) throws ReservationNotFoundException;

    public Long createReservationForOccupant(Reservation reservation, Long occupantId, Long roomTypeId);

    public Long createPartnerReservation(Reservation reservation, Long partnerId, Long roomTypeId);

    public List<Reservation> viewAllPartnerReservationsFor(Long partnerId);

    public List<Reservation> retrieveReservationByPartnerId(Long partnerId) throws ReservationNotFoundException;
    
}
