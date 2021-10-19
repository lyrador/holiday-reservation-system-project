package ejb.session.stateless;

import entity.Occupant;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Reservation;
import entity.RoomType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    public ReservationSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
    }
    
    @PreDestroy
    public void preDestroy() {
    }

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
//    @Override
//    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
//        Reservation reservation = em.find(Reservation.class, reservationId);
//        
//        if(reservation != null) {
//            return reservation;
//        } else {
//            throw new ReservationNotFoundException("Reservation does not exist: " + reservationId);
//        }
//    }
//    
//    @Override
//    public Long createReservation(Reservation reservation)
//    {
//        em.persist(reservation);
//        em.flush();
//        
//        return reservation.getReservationId();
//    }
    
//    @Override
//    public Reservation createOnlineReservation(Date checkInDate, Date checkOutDate...) {
//       
//    }
    
    @Override
    public Reservation createNewReservationWithExistingOccupant(Reservation newReservation, Long occupantId) {
        em.persist(newReservation);
        Occupant occupant = em.find(Occupant.class, occupantId);
        newReservation.setOccupant(occupant);
        occupant.getReservations().add(newReservation);
        em.flush();
        
        return newReservation;
    }
    
    public Reservation createNewReservationWithExistingRoomType(Reservation newReservation, Long roomTypeId) {
        em.persist(newReservation);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        newReservation.setRoomType(roomType);
        roomType.getReservations().add(newReservation);
        em.flush();
        
        return newReservation;
    }
    
    public void associateExistingReservationWithExistingOccupant(Long reservationId, Long occupantId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        Occupant occupant = em.find(Occupant.class, occupantId);
        
        if (reservation.getOccupant() != null) {
            reservation.getOccupant().getReservations().remove(reservation);
        }
        
        reservation.setOccupant(occupant);
        occupant.getReservations().add(reservation);
    }
    
    public void deleteReservation(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        reservation.getOccupant().getReservations().remove(reservation);   
        reservation.getRoomType().getReservations().remove(reservation);
        
        em.remove(reservation);
    }
    
    @Override
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        if(reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation does not exist: " + reservationId);
        }
    }
    
    @Override
    public Reservation retrieveReservationById(Long reservationId, Boolean loadRoomType, Boolean loadOccupant) throws ReservationNotFoundException {
        Reservation reservation = retrieveReservationById(reservationId);
        
        if(reservation != null) {
            if (loadRoomType) {
                reservation.getRoomType();
            } if (loadOccupant) {
                reservation.getOccupant();
            }
        }
        
        return reservation;
    }
}
