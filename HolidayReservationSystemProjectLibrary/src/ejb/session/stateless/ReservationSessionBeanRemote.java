package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.Room;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoMoreRoomsException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author yeeda
 */
@Remote
public interface ReservationSessionBeanRemote {
    
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;
    
    public Long createReservation(Reservation reservation, Long guestId, Long roomTypeId) throws RoomTypeNotFoundException, NoMoreRoomsException;
    
    public List<Reservation> viewAllMyReservations(Long guestId);
    
    public List<Reservation> retrieveReservationsByCheckOutDate(Date checkOutDate);

    public List<Reservation> retrieveReservationsByCheckInDate(Date checkInDate);

    public List<Room> allocateRoomToCurrentDayReservations(Date today);

    public ExceptionReport createExceptionReport(ExceptionReport exceptionReport);
    
    public List<Reservation> retrieveReservationByOccupantId(Long occupantId) throws ReservationNotFoundException;

    public List<Reservation> retrieveReservationByGuestId(Long guestId) throws ReservationNotFoundException;
    
    public Long createReservationForOccupant(Reservation reservation, Long occupantId, Long roomTypeId) throws RoomTypeNotFoundException, NoMoreRoomsException;
    
    public List<Reservation> retrieveReservationByPartnerId(Long partnerId) throws ReservationNotFoundException;
    
     public void allocateRoomToCurrentDayReservationsAutomated();
     
     public Long createPartnerReservation(Reservation reservation, Long partnerId, Long roomTypeId) throws RoomTypeNotFoundException, NoMoreRoomsException;
   
}
