/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.exception.RoomRateNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author raihan
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomRate(RoomRate roomRate) {
        em.persist(roomRate);
        em.flush();
        
        return roomRate.getRoomRateId();
    }
    
    @Override
    public List<String> viewRoomRateDetails(Long roomRateId) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        List<String> roomRateDetails = new ArrayList<String>();
        
        roomRateDetails.add(roomRate.getName());
        roomRateDetails.add(roomRate.getRateType());
        roomRateDetails.add(roomRate.getRatePerNight().toString());
        roomRateDetails.add(roomRate.getValidityStartDate().toString());
        roomRateDetails.add(roomRate.getValidityEndDate().toString());
        roomRateDetails.add(roomRate.getRoomType().toString());
        
        return roomRateDetails;
    }

    @Override
    public void updateRoomRateName(Long roomRateId, String name) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        roomRate.setName(name);
        em.merge(roomRate);
    }
    
    @Override
    public void updateRoomRateRoomType(Long roomRateId, RoomType roomType) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);

        roomRate.setRoomType(roomType);
        em.merge(roomRate);
    }
    
    @Override
    public void updateRoomRateRateType(Long roomRateId, String rateType) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        roomRate.setRateType(rateType);
        em.merge(roomRate);
        
    }
    
    @Override
    public void updateRoomRateRatePerNight(Long roomRateId, Integer ratePerNight) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        roomRate.setRatePerNight(ratePerNight);
        em.merge(roomRate);
        
    }
    
    @Override
    public void updateRoomRateValidityStartDate(Long roomRateId, Date validityStartDate) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        roomRate.setValidityStartDate(validityStartDate);
        em.merge(roomRate);
        
    }
    
    @Override
    public void updateRoomRateValidityEndDate(Long roomRateId, Date validityEndDate) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        roomRate.setValidityEndDate(validityEndDate);
        em.merge(roomRate);
        
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        em.remove(roomRate);
    }
    
    @Override
    public List<RoomRate> viewAllRoomRates() throws RoomRateNotFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM RoomRate r");
            return query.getResultList();
        } catch(Exception e) { 
            throw new RoomRateNotFoundException("Room Rate not found");
        }
    }
}
