/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author raihan
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomType(RoomType roomType) {
        em.persist(roomType);
        em.flush();
        
        return roomType.getRoomTypeId();
    }
    
    @Override
    public List<String> viewRoomTypeDetails(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        List<String> roomTypeDetails = new ArrayList<String>();
        
        roomTypeDetails.add(roomType.getRoomName());
        roomTypeDetails.add(roomType.getRoomDescription());
        roomTypeDetails.add(roomType.getRoomSize().toString());
        roomTypeDetails.add(roomType.getRoomBed());
        roomTypeDetails.add(roomType.getRoomAmenities());
        
        return roomTypeDetails;
    }
    
    @Override
    public void updateRoomTypeName(Long roomTypeId, String name) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        roomType.setRoomName(name);
        em.merge(roomType);
    }
    
    @Override
    public void updateRoomTypeDescription(Long roomTypeId, String description) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        roomType.setRoomDescription(description);
        em.merge(roomType);
    }
    
    @Override
    public void updateRoomTypeSize(Long roomTypeId, Integer size) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        roomType.setRoomSize(size);
        em.merge(roomType);
    }
    
    @Override
    public void updateRoomTypeBed(Long roomTypeId, String bed) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        roomType.setRoomBed(bed);
        em.merge(roomType);
    }

    @Override
    public void updateRoomTypeCapacity(Long roomTypeId, Integer capacity) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        roomType.setRoomCapacity(capacity);
        em.merge(roomType);
    }
    
    @Override
    public void updateRoomTypeAmenities(Long roomTypeId, String amenities) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        roomType.setRoomAmenities(amenities);
        em.merge(roomType);
    }
    
    @Override
    public void deleteRoomRate(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        em.remove(roomType);
    }

    @Override
    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM RoomType r");
            return query.getResultList();
        } catch(Exception e) { 
            throw new RoomTypeNotFoundException("Room Type not found");
        }
    }
    
}
