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
import java.util.List;
import javax.ejb.EJB;
import util.exception.RoomRateNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.DeleteRoomRateException;
import util.exception.RoomTypeNotFoundException;

@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    

    @Override
    public RoomRate createRoomRate(RoomRate newRoomRate, Long roomTypeId) throws RoomTypeNotFoundException {
        
        RoomType roomType = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomId(roomTypeId);
        
        newRoomRate.setRoomType(roomType);
        roomType.getRoomRates().size();
        roomType.getRoomRates().add(newRoomRate);
        
        em.persist(newRoomRate);
        em.flush();
        
        return newRoomRate;
    }
    
    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        if (roomRate != null) {
            return roomRate;
        } else {
            throw new RoomRateNotFoundException("Room Rate ID " + roomRateId + " does not exist!");
        }      
        
    }

    @Override
    public void updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException {

        if(roomRate != null && roomRate.getRoomRateId()!= null) {
            RoomRate roomRateToUpdate = retrieveRoomRateById(roomRate.getRoomRateId());
            
            roomRateToUpdate.setName(roomRate.getName());
            roomRateToUpdate.setRoomType(roomRate.getRoomType());
            roomRateToUpdate.setRateType(roomRate.getRateType());
            roomRateToUpdate.setRatePerNight(roomRate.getRatePerNight());
            roomRateToUpdate.setValidityStartDate(roomRate.getValidityStartDate());
            roomRateToUpdate.setValidityEndDate(roomRate.getValidityEndDate());
            
        } else {
            throw new RoomRateNotFoundException("Room Rate ID not provided for room rate to be updated");
        }
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, DeleteRoomRateException { //not complete
        RoomRate roomRateToRemove = retrieveRoomRateById(roomRateId);
        
        //check rooms with this room rate before deleting
        Query query = em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> reservations = query.getResultList();
        
        //check if current reservations use the room rate and is already allocated -> set to disabled
        for (Reservation reservation : reservations) {
            if (reservation.getRoomType().getRoomRates().contains(roomRateToRemove) && reservation.getIsAllocated()) {
                Integer rate = reservation.getTotalAmount() / reservation.getNumOfRooms();
                if (rate.equals(roomRateToRemove.getRatePerNight())) {
                    roomRateToRemove.setIsEnabled(false);                    
                }
            }
        }
        
        //if room rate is not in use, can delete
        if(roomRateToRemove.getIsEnabled()) {
            roomRateToRemove.getRoomType().getRoomRates().remove(roomRateToRemove);
            em.remove(roomRateToRemove);
        } else {
            throw new DeleteRoomRateException("RoomRate ID " + roomRateId + " is currently in use and cannot be deleted. It has been set to disabled!");
        }
        
        
    }
    
    @Override
    public List<RoomRate> viewAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM RoomRate r");
        
        return query.getResultList();
    }
}
