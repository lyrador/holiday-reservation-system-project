package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Guest;
import entity.Occupant;
import entity.Partner;
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
import javax.ejb.Schedule;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.NoMoreRoomsException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;

@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;
    
    

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
    
    //Create a reservation for a guest
    @Override
    public Long createReservation(Reservation reservation, Long guestId, Long roomTypeId) throws RoomTypeNotFoundException, NoMoreRoomsException {
        
        Guest guest = em.find(Guest.class, guestId);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        int totalRooms = roomTypeSessionBeanLocal.calculateNumOfRoomsAvailable(roomTypeId, reservation.getCheckInDateTime(), reservation.getCheckOutDateTime());
        int reservedRooms = reservation.getNumOfRooms();
        
        if(reservedRooms <= totalRooms) {
            reservation.setGuest(guest);
            reservation.setRoomType(roomType);
            guest.getReservations().add(reservation);
            roomType.getReservations().add(reservation);

            em.persist(reservation);
            em.flush();

            return reservation.getReservationId();
        } else {
            throw new NoMoreRoomsException("No more rooms available!");
        }
        
    }
    
    //Create a reservation for a partner
    @Override
    public Long createPartnerReservation(Reservation reservation, Long partnerId, Long roomTypeId) throws RoomTypeNotFoundException, NoMoreRoomsException {
        
        Partner partner = em.find(Partner.class, partnerId);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        int totalRooms = roomTypeSessionBeanLocal.calculateNumOfRoomsAvailable(roomTypeId, reservation.getCheckInDateTime(), reservation.getCheckOutDateTime());
        int reservedRooms = reservation.getNumOfRooms();
        
        if (reservedRooms <= totalRooms) {
            reservation.setPartner(partner);
            reservation.setRoomType(roomType);
            partner.getReservations().add(reservation);
            roomType.getReservations().add(reservation);

            em.persist(reservation);
            em.flush();
            
            return reservation.getReservationId();
        } else {
            throw new NoMoreRoomsException("No more rooms available!");
        }
        
       
    }
    
    //Create a reservation for a walk-in guest
    @Override
    public Long createReservationForOccupant(Reservation reservation, Long occupantId, Long roomTypeId) throws RoomTypeNotFoundException, NoMoreRoomsException {
        
        Occupant occupant = em.find(Occupant.class, occupantId);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        int totalRooms = roomTypeSessionBeanLocal.calculateNumOfRoomsAvailable(roomTypeId, reservation.getCheckInDateTime(), reservation.getCheckOutDateTime());
        int reservedRooms = reservation.getNumOfRooms();
        
        if (reservedRooms <= totalRooms) {
               reservation.setOccupant(occupant);
            reservation.setRoomType(roomType);
            occupant.getReservations().add(reservation);
            roomType.getReservations().add(reservation);

            em.persist(reservation);
            em.flush();

            return reservation.getReservationId();
        } else {
            throw new NoMoreRoomsException("No more rooms available!");
        }
        
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
    
    @Override
    public List<Reservation> viewAllPartnerReservationsFor(Long partnerId) {
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :inPartnerId");
        query.setParameter("inPartnerId", partnerId);
        
        List<Reservation> reservations = new ArrayList<>();
        
        if (query.getResultList() != null) {
            reservations = query.getResultList();
        } 
        
        for (Reservation reservation: reservations) {
            reservation.getRoomType();
        }
        
        return reservations;
    }
    
//    @Schedule(hour="2")
    @Override
    public List<Room> allocateRoomToCurrentDayReservations(Date today) {
        
        List<Reservation> reservationsOnCheckInDate = retrieveReservationsByCheckInDate(today);
        List<Reservation> reservationsOnCheckOutDate = retrieveReservationsByCheckOutDate(today);
        
        List<Room> roomsAvailable = new ArrayList<>();
        List<Room> roomsReserved = new ArrayList<>();
        
        
        for (Reservation reservationToday : reservationsOnCheckInDate) {
            
            if (!reservationToday.getIsAllocated()) {
                List<Room> allAvailableRooms = roomSessionBeanLocal.retrieveAvailableRooms();
                RoomType reservationRoomType = reservationToday.getRoomType();
                System.out.println("Reservation room type: " + reservationToday.getRoomType());

                for (Room availableRoom : allAvailableRooms) {
                    if (availableRoom.getRoomType().equals(reservationRoomType) && reservationRoomType.isIsEnabled()) {
                        System.out.println("Room type of available room: " + availableRoom.getRoomType());
                        roomsAvailable.add(availableRoom);
                    }
                }

                // check if there are any rooms about to be available
                if (!reservationsOnCheckOutDate.isEmpty()) {
                    for (Reservation endedReservation : reservationsOnCheckOutDate) {
                        if (endedReservation.getRoomType().equals(reservationRoomType) && reservationRoomType.isIsEnabled()) {
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
                        
                        ExceptionReport report = new ExceptionReport();
                        
                        if (updatedAvailableRoom.getRoomType().getRoomRank() > reservationRoomType.getRoomRank()) {
                            updatedAvailableRoom.setRoomAvailability(RoomStatusEnum.RESERVED);
                            roomsReserved.add(updatedAvailableRoom);
                            reservationToday.getRooms().add(updatedAvailableRoom);
                            updatedAvailableRoom.setReservation(reservationToday);
                            roomsToAllocate--;

                            report.setDescription("No available room for reserved room type, but upgrade to next higher room type "
                                    + "available. Room " + updatedAvailableRoom.getRoomNumber() + " of Room Type " + updatedAvailableRoom.getRoomType().getRoomName() + " allocated.");
                            allocationStatus = true;
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
                    ExceptionReport report = new ExceptionReport();
                    report.setDescription("No available room for reserved room type, no upgrade to next higher room type available. No room allocated.");
                    createExceptionReport(report);
                }

                if (allocationStatus == true) {
                    reservationToday.setIsAllocated(true);
                }
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
    
    @Schedule(hour="2")
    @Override
    public void allocateRoomToCurrentDayReservationsAutomated() {
        
        Date today = new Date();
        List<Reservation> reservationsOnCheckInDate = retrieveReservationsByCheckInDate(today);
        List<Reservation> reservationsOnCheckOutDate = retrieveReservationsByCheckOutDate(today);
        
        List<Room> roomsAvailable = new ArrayList<>();
        List<Room> roomsReserved = new ArrayList<>();
        
        
        for (Reservation reservationToday : reservationsOnCheckInDate) {
            
            if (!reservationToday.getIsAllocated()) {
                List<Room> allAvailableRooms = roomSessionBeanLocal.retrieveAvailableRooms();
                RoomType reservationRoomType = reservationToday.getRoomType();

                for (Room availableRoom : allAvailableRooms) {
                    if (availableRoom.getRoomType().equals(reservationRoomType)) {
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
                        
                        ExceptionReport report = new ExceptionReport();
                        
                        if (updatedAvailableRoom.getRoomType().getRoomRank() > reservationRoomType.getRoomRank()) {
                            updatedAvailableRoom.setRoomAvailability(RoomStatusEnum.RESERVED);
                            roomsReserved.add(updatedAvailableRoom);
                            reservationToday.getRooms().add(updatedAvailableRoom);
                            updatedAvailableRoom.setReservation(reservationToday);
                            roomsToAllocate--;

                            report.setDescription("No available room for reserved room type, but upgrade to next higher room type "
                                    + "available. Room " + updatedAvailableRoom.getRoomNumber() + " of Room Type " + updatedAvailableRoom.getRoomType().getRoomName() + " allocated.");
                            allocationStatus = true;
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
                    ExceptionReport report = new ExceptionReport();
                    report.setDescription("No available room for reserved room type, no upgrade to next higher room type available. No room allocated.");
                    createExceptionReport(report);
                }

                if (allocationStatus == true) {
                    reservationToday.setIsAllocated(true);
                }
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
        
        System.out.println("Rooms allocated: ");
        int counter = 1;
        for (Room room : roomsReserved) {
            System.out.println(counter + ": Room " + room.getRoomNumber());
            counter++;
        }
        
        
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
    
    
    @Override
    public List<Reservation> retrieveReservationByPartnerId(Long partnerId) throws ReservationNotFoundException {
        
        Date today = new Date();
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :inPartnerId AND r.checkInDateTime = :today");
        query.setParameter("inOccupantId", partnerId);
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
    
    
}
