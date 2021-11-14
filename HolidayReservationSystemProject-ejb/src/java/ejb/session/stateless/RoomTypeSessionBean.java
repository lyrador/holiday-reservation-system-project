/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;

    @Override
    public RoomType createRoomType(RoomType newRoomType) {
        em.persist(newRoomType);
        em.flush();

        return newRoomType;
    }

    @Override
    public RoomType retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.roomName = :inRoomName");
        query.setParameter("inRoomName", name);
        try {
            RoomType roomType = (RoomType) query.getSingleResult();
            return roomType;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type: " + name + " does not exist!");
        }

    }

    @Override
    public RoomType retrieveRoomTypeByRoomId(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);

        if (roomType != null) {
            return roomType;
        } else {
            throw new RoomTypeNotFoundException("Room Type ID " + roomTypeId + " does not exist!");
        }

    }

    @Override
    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException {

        if (roomType != null && roomType.getRoomTypeId() != null) {
            RoomType roomTypeToUpdate = retrieveRoomTypeByRoomId(roomType.getRoomTypeId());

            roomTypeToUpdate.setRoomName(roomType.getRoomName());
            roomTypeToUpdate.setRoomDescription(roomType.getRoomDescription());
            roomTypeToUpdate.setRoomSize(roomType.getRoomSize());
            roomTypeToUpdate.setRoomBed(roomType.getRoomBed());
            roomTypeToUpdate.setRoomCapacity(roomType.getRoomCapacity());
            roomTypeToUpdate.setRoomAmenities(roomType.getRoomAmenities());

        } else {
            throw new RoomTypeNotFoundException("Room Type ID not provided for room type to be updated");
        }
    }

    @Override
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException {
        RoomType roomTypeToRemove = retrieveRoomTypeByRoomId(roomTypeId);

        boolean canDelete = true;
        for (Room room : roomTypeToRemove.getRooms()) {
            if (!room.getRoomAvailability().equals(RoomStatusEnum.AVAILABLE)) {
                canDelete = false;
            }
        }
        
        if (canDelete) {
            em.remove(roomTypeToRemove);
        } else {
            roomTypeToRemove.setIsEnabled(false);
            throw new DeleteRoomTypeException("Room Type ID " + roomTypeId + " is currently in use and cannot be deleted. It has been set to disabled!");
        }
    }

    @Override
    public List<RoomType> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT r FROM RoomType r");
        return query.getResultList();
    }

    @Override
    public int calculatePrice(Long roomTypeId, Date checkInDate, Date checkOutDate, Boolean isWalkIn) throws RoomTypeNotFoundException {
        RoomType roomType = retrieveRoomTypeByRoomId(roomTypeId);
        roomType.getRoomRates().size();
        int totalPrice = 0;
        
        Calendar start = Calendar.getInstance();
        start.setTime(checkInDate);
        Calendar end = Calendar.getInstance();
        end.setTime(checkOutDate);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            
            if (isWalkIn) {
                for (RoomRate roomRate: roomType.getRoomRates()) {
                    if (roomRate.getRateType() == RateTypeEnum.PUBLISHED && roomRate.getIsEnabled()) {
                        totalPrice += roomRate.getRatePerNight();
                        break;  
                    }
                }  
            } else {
                boolean addedRoomRate = false;
                for (RoomRate roomRate: roomType.getRoomRates()) {
                    if (roomRate.getRateType() == RateTypeEnum.PROMOTION && roomRate.getIsEnabled()) {
                        if (roomRate.getValidityStartDate().compareTo(date) <= 0 && roomRate.getValidityEndDate().compareTo(date) >= 0 ) {
                            totalPrice += roomRate.getRatePerNight();
                            addedRoomRate = true;
                            break;
                        }
                    } else if (roomRate.getRateType() == RateTypeEnum.PEAK && roomRate.getIsEnabled()) {
                        if (roomRate.getValidityStartDate().compareTo(date) <= 0 && roomRate.getValidityEndDate().compareTo(date) >= 0) {
                            totalPrice += roomRate.getRatePerNight();
                            addedRoomRate = true;
                            break;
                        }
                    } 
                }
                
                if (!addedRoomRate) {
                    for (RoomRate roomRate : roomType.getRoomRates()) {
                        if (roomRate.getRateType() == RateTypeEnum.NORMAL && roomRate.getIsEnabled()) {
                            totalPrice += roomRate.getRatePerNight();
                            break;
                        }
                    }
                }
                
            }
        }
        return totalPrice;
    }
    
    //for a specific room type
    @Override
    public int calculateNumOfRoomsAvailable(Long roomTypeId, Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException {
        RoomType roomType = retrieveRoomTypeByRoomId(roomTypeId);
        roomType.getRoomRates().size();
        
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType.roomTypeId = :inRoomTypeId AND r.roomType.isEnabled = true");
        query.setParameter("inRoomTypeId", roomTypeId);
        List<Room> roomList = query.getResultList();
        
        Query query2 = em.createQuery("SELECT r FROM Reservation r WHERE r.roomType.roomTypeId = :inRoomTypeId AND r.roomType.isEnabled = true");
        query2.setParameter("inRoomTypeId", roomTypeId);
        List<Reservation> reservations = query2.getResultList();
        
        int numOfRoomsAvailable = 0;
        
        for (Room room : roomList) {
            if ((room.getDateOccupiedOn() == null || room.getDateOccupiedOn().before(checkInDate))) {
                numOfRoomsAvailable++;
            }
        }
        
        // reservations for a room type
        for (Reservation reservation : reservations) {
            if (!reservation.getIsAllocated()) {
                if ((reservation.getCheckInDateTime().compareTo(checkInDate) <= 0 && reservation.getCheckOutDateTime().compareTo(checkOutDate) >=0) ||
                        (reservation.getCheckOutDateTime().compareTo(checkInDate) >= 0 && reservation.getCheckInDateTime().compareTo(checkInDate) <= 0) ||
                        (reservation.getCheckInDateTime().compareTo(checkOutDate) <= 0 && reservation.getCheckOutDateTime().compareTo(checkOutDate) >= 0)) {
                    int numOfRooms = reservation.getNumOfRooms();
                    numOfRoomsAvailable -= numOfRooms;
                }
            }
        }
        
        return numOfRoomsAvailable;
        
    }
    
    //for all room types
    @Override
    public int calculateTotalNumOfRoomsAvailable(Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException {

        int totalAvailableRooms = 0;
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.isEnabled = true");
        List<RoomType> roomTypes = query.getResultList();
        
        for (RoomType roomType : roomTypes) {
            totalAvailableRooms += calculateNumOfRoomsAvailable(roomType.getRoomTypeId(), checkInDate, checkOutDate);
        }
           
        return totalAvailableRooms;
    }
    
    @Override
    public int calculateTotalNumOfRooms(Date checkInDate, Date checkOutDate) {
        Query query = em.createQuery("SELECT r FROM Room r");
        List<Room> rooms = query.getResultList();
        
        return rooms.size();
    }
}

