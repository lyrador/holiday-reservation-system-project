package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Guest;
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
    
    @Override
    public Long createReservation(Reservation reservation, Long guestId, Long roomTypeId) {
        
        Guest guest = em.find(Guest.class, guestId);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        reservation.setGuest(guest);
        reservation.setRoomType(roomType);
        guest.getReservations().add(reservation);
        roomType.getReservations().add(reservation);
        
        em.persist(reservation);
        em.flush();
        
        return reservation.getReservationId();
    }
    
    @Override
    public Long createReservationForOccupant(Reservation reservation, Long occupantId, Long roomTypeId) {
        
        Occupant occupant = em.find(Occupant.class, occupantId);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        reservation.setOccupant(occupant);
        reservation.setRoomType(roomType);
        occupant.getReservations().add(reservation);
        roomType.getReservations().add(reservation);
        
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
    public List<Reservation> viewAllMyReservations(Long guestId) {
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :inGuestId");
        query.setParameter("inGuestId", guestId);
        
        List<Reservation> reservations = new ArrayList<>();
        
        if (query.getResultList() != null) {
            reservations = query.getResultList();
        } 
        
        for (Reservation reservation: reservations) {
            reservation.getRoomType();
        }
        
        return reservations;
    }
    
    public List<Reservation> viewAllPartnerReservationsFor(Long partnerId) {
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :inPartnerId");
        query.setParameter("inPartnerId", partnerId);
        
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
        
        List<Room> roomsAvailable = new ArrayList<>();
        List<Room> roomsReserved = new ArrayList<>();
        
        ExceptionReport report = new ExceptionReport();
        
        for (Reservation reservationToday : reservationsOnCheckInDate) {
            
            List<Room> allAvailableRooms = roomSessionBeanLocal.retrieveAvailableRooms();
            RoomType reservationRoomType = reservationToday.getRoomType();
            System.out.println("Reservation room type: " + reservationToday.getRoomType());
            
            for (Room availableRoom : allAvailableRooms) {
                if (availableRoom.getRoomType().equals(reservationRoomType)) {
                    System.out.println("Room type of available room: " + availableRoom.getRoomType());
                    roomsAvailable.add(availableRoom);
                }
            }
            
            // check if there are any rooms about to be available
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
            System.out.println("Rooms to allocate this round: " + roomsToAllocate);

            for (Room room : roomsAvailable) {
                if (room.getRoomAvailability().equals(RoomStatusEnum.NOT_AVAILABLE)) {
                    room.setRoomAvailability(RoomStatusEnum.OCCUPIED_RESERVED);
                } else if (room.getRoomAvailability().equals(RoomStatusEnum.AVAILABLE)) {
                    room.setRoomAvailability(RoomStatusEnum.RESERVED);
                }
                
                roomsReserved.add(room);
                reservationToday.getRooms().add(room);
                room.setReservation(reservationToday);
                room.setDateOccupiedOn(reservationToday.getCheckOutDateTime());
                
                roomsToAllocate--;
                
                if(roomsToAllocate == 0) {
                    break;
                }
            }
            
            roomsAvailable.clear();
            
            boolean allocationStatus = false;
            if (roomsToAllocate == 0) {
                allocationStatus = true;
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
                        allocationStatus = true;
                        break;
                    }
                    
                }
                
            }
            
            //no more rooms to upgrade to
            if (roomsToAllocate > 0) {
                report.setDescription("No available room for reserved room type, no upgrade to next higher room type available. No room allocated.");
                createExceptionReport(report);
            }
            
            if (allocationStatus == true) {
                reservationToday.setIsAllocated(true);
            }
        }
        
        //change status of rooms whose reservation ended today
        for (Reservation reservationEndedToday : reservationsOnCheckOutDate) {
            
            for (Room reservationEndedRoom : reservationEndedToday.getRooms()) {
                if (reservationEndedRoom.getRoomAvailability().equals(RoomStatusEnum.NOT_AVAILABLE)) {
                    reservationEndedRoom.setRoomAvailability(RoomStatusEnum.AVAILABLE);
                }
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
     
    @Override
    public List<Reservation> retrieveReservationByGuestId(Long guestId) throws ReservationNotFoundException {
        Date today = new Date();
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :inGuestId AND r.checkInDateTime = :today");
        query.setParameter("inGuestId", guestId);
        query.setParameter("today", today);
        
        List<Reservation> reservations = new ArrayList<>();
        
        if (query.getResultList() != null) {
            reservations = query.getResultList();
        } 
        
        for (Reservation reservation : reservations) {
            reservation.getRooms().size();
        }
        
        return reservations;
    }
    
    @Override
    public List<Reservation> retrieveReservationByOccupantId(Long occupantId) throws ReservationNotFoundException {
        Date today = new Date();
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.occupant.occupantId = :inOccupantId AND r.checkInDateTime = :today");
        query.setParameter("inOccupantId", occupantId);
        query.setParameter("today", today);
        List<Reservation> reservations = new ArrayList<>();
        
        if (query.getResultList() != null) {
            reservations = query.getResultList();
        } 
        
        for (Reservation reservation : reservations) {
            reservation.getRooms().size();
        }
        
        return reservations;
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
