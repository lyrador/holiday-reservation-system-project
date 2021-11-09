package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Occupant;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.ReservationNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RoomTypeNotFoundException;

@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

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
    
//    @Override
//    public Reservation createNewReservationWithExistingOccupant(Reservation newReservation, Long occupantId) {
//        em.persist(newReservation);
//        Occupant occupant = em.find(Occupant.class, occupantId);
//        newReservation.setOccupant(occupant);
//        occupant.getReservations().add(newReservation);
//        em.flush();
//        
//        return newReservation;
//    }
    public Long createReservation(Reservation reservation) {
        
        em.persist(reservation);
        em.flush();
        
        return reservation.getReservationId();
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
    public List<Reservation> retrieveReservationsByCheckOutDate(Date checkOutDate) {
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.checkOutDateTime = :inCheckOutDate");
        query.setParameter("inCheckOutDate", checkOutDate);
        
        List<Reservation> reservations = new ArrayList<>();
        
        if (query.getResultList() != null) {
            reservations = query.getResultList();
        } 
        
        return reservations;

    }
    
    @Override
    public List<Reservation> retrieveReservationsByCheckInDate(Date checkInDate) {
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.checkInDateTime = :inCheckInDate");
        query.setParameter("inCheckInDate", checkInDate);
        
        List<Reservation> reservations = new ArrayList<>();
        
        if (query.getResultList() != null) {
            reservations = query.getResultList();
        } 
        
        return reservations;

    }
    
    @Override
    public List<Room> allocateRoomToCurrentDayReservations() {
        
        Date today = new Date();
        List<Reservation> reservationsOnCheckInDate = retrieveReservationsByCheckInDate(today);
        List<Reservation> reservationsOnCheckOutDate = retrieveReservationsByCheckOutDate(today);
        List<Room> roomsAvailable = new ArrayList<Room>();
        List<Room> roomsReserved = new ArrayList<Room>();
        
        ExceptionReport report = new ExceptionReport();
        
        for (Reservation reservationToday : reservationsOnCheckInDate) {
            
            List<Room> allAvailableRooms = roomSessionBeanLocal.retrieveAvailableRooms();
            RoomType reservationRoomType = reservationToday.getRoomType();
            
            for (Room availableRoom : allAvailableRooms) {
                if (availableRoom.getRoomType().equals(reservationRoomType)) {
                    roomsAvailable.add(availableRoom);
                }
            }
            
            if (!reservationsOnCheckOutDate.isEmpty()) {
                
                for (Reservation endedReservation : reservationsOnCheckOutDate) {
                    
                    if (endedReservation.getRoomType().equals(reservationRoomType)) {
                        
                        for (Room room : endedReservation.getRooms()) {
                            roomsAvailable.add(room);
                        }
                        
                    }
                    
                }
                
            }
            
            Integer roomsToAllocate = reservationToday.getNumOfRooms();

            for (Room room : roomsAvailable) {
                if (room.getRoomAvailability().equals(RoomStatusEnum.NOT_AVAILABLE)) {
                    room.setRoomAvailability(RoomStatusEnum.OCCUPIED_RESERVED);
                } else if (room.getRoomAvailability().equals(RoomStatusEnum.AVAILABLE)) {
                    room.setRoomAvailability(RoomStatusEnum.RESERVED);
                }
                
                roomsReserved.add(room);
                reservationToday.getRooms().add(room);
                room.setReservation(reservationToday);
                roomsToAllocate--;
                
                if(roomsToAllocate == 0) {
                    break;
                }
            }
            
            //did not allocate everything
            if (roomsToAllocate > 0) {
                
                List<Room> allUpdatedAvailableRooms = roomSessionBeanLocal.retrieveAvailableRooms();
                
                for (Room updatedAvailableRoom : allUpdatedAvailableRooms) {
                    
                    if (updatedAvailableRoom.getRoomType().getRoomRank() > reservationRoomType.getRoomRank()) {
                        updatedAvailableRoom.setRoomAvailability(RoomStatusEnum.RESERVED);
                        roomsReserved.add(updatedAvailableRoom);
                        reservationToday.getRooms().add(updatedAvailableRoom);
                        updatedAvailableRoom.setReservation(reservationToday);
                        roomsToAllocate--;
                        
                        report.setDescription("No available room for reserved room type, upgrade to next higher room type available. Room " + updatedAvailableRoom.getRoomNumber() + " allocated.");
                        createExceptionReport(report);
                        
                    }
                    
                    if(roomsToAllocate == 0) {
                        break;
                    }
                    
                }
                
            }
            
            //no more rooms to upgrade to
            if (roomsToAllocate > 0) {
                report.setDescription("No available room for reserved room type, no upgrade to next higher room type available. No room allocated.");
                createExceptionReport(report);
            }
        }
        
        return roomsReserved;
        
        
    }
    
    @Override
     public ExceptionReport createExceptionReport(ExceptionReport exceptionReport){
        em.persist(exceptionReport);
        em.flush();
        
        return exceptionReport;
    }
    
//    @Override
//    public Reservation retrieveReservationById(Long reservationId, Boolean loadRoomType, Boolean loadOccupant) throws ReservationNotFoundException {
//        Reservation reservation = retrieveReservationById(reservationId);
//        
//        if(reservation != null) {
//            if (loadRoomType) {
//                reservation.getRoomType();
//            } if (loadOccupant) {
//                reservation.getOccupant();
//            }
//        }
//        
//        return reservation;
//    }
}
