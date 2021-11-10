/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
            throw new DeleteRoomTypeException("Room Type ID " + roomTypeId + " is currently in use and cannot be deleted. Try again next time!");
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
                    if (roomRate.getRateType() == RateTypeEnum.PUBLISHED) {
                        totalPrice += roomRate.getRatePerNight();
                        break;  
                    }
                }  
            } else {
                for (RoomRate roomRate: roomType.getRoomRates()) {
                    if (roomRate.getRateType() == RateTypeEnum.PROMOTION) {
                        if (roomRate.getValidityStartDate().compareTo(date) <= 0 && roomRate.getValidityEndDate().compareTo(date) >= 0) {
                            totalPrice += roomRate.getRatePerNight();
                            break;
                        }
                    } else if (roomRate.getRateType() == RateTypeEnum.PEAK) {
                        if (roomRate.getValidityStartDate().compareTo(date) <= 0 && roomRate.getValidityEndDate().compareTo(date) >= 0) {
                            totalPrice += roomRate.getRatePerNight();
                            break;
                        }
                    } else if (roomRate.getRateType() == RateTypeEnum.NORMAL) {
                        totalPrice += roomRate.getRatePerNight();
                        break;
                    }
                }
            }
        }
        return totalPrice;
    }
    
    @Override
    public int calculateNumOfRoomsAvailable(Long roomTypeId, Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException {
        RoomType roomType = retrieveRoomTypeByRoomId(roomTypeId);
        roomType.getRoomRates().size();
        int totalNumOfRooms = 0;
        
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType.roomTypeId = :inRoomTypeId");
        query.setParameter("inRoomTypeId", roomTypeId);
        List<Room> roomList = query.getResultList();
        
        int numOfRoomsAvailable = 0;
        
        for (Room room : roomList) {
            if ((room.getDateOccupiedOn() == null || room.getDateOccupiedOn().before(checkInDate)) && room.getRoomAvailability().equals(RoomStatusEnum.AVAILABLE)) {
                numOfRoomsAvailable++;
            }
        }
        
        return numOfRoomsAvailable;
        
//        Calendar start = Calendar.getInstance();
//        start.setTime(checkInDate);
//        Calendar end = Calendar.getInstance();
//        end.setTime(checkOutDate);
//        
//        for (Room room: roomType.getRooms()) {
//            boolean availableOnAllSelectedDates = true;
//            for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
//                if (room.getDateOccupiedOn() == date && room.getRoomAvailability() == RoomStatusEnum.NOT_AVAILABLE) {
//                    availableOnAllSelectedDates = false;
//                    break;
//                }
//            }
//            if (availableOnAllSelectedDates) {
//                totalNumOfRooms++;
//            }
//        }
//        return totalNumOfRooms;
    }
}
