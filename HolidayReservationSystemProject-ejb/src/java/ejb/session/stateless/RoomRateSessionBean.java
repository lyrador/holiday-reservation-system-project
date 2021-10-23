/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import util.exception.RoomRateNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;

    @Override
    public RoomRate createRoomRate(RoomRate newRoomRate) {
        em.persist(newRoomRate);
        em.flush();
        
        return newRoomRate;
    }
    
    @Override
    public RoomRate viewRoomRateDetails(Long roomRateId) throws RoomRateNotFoundException {
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
            RoomRate roomRateToUpdate = viewRoomRateDetails(roomRate.getRoomRateId());
            
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
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException { //not complete
        RoomRate roomRateToRemove = viewRoomRateDetails(roomRateId);
        
        em.remove(roomRateToRemove);
    }
    
    @Override
    public List<RoomRate> viewAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM RoomRate r");
        
        return query.getResultList();
    }
}
