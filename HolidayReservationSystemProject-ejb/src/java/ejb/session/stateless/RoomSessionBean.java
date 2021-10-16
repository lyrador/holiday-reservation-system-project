/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomNotFoundException;

/**
 *
 * @author raihan
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createRoomType(Room room) {
        em.persist(room);
        em.flush();
        
        return room.getRoomId();
    }
    
    @Override
    public void updateRoomType(Long roomId, RoomType roomType) {
        Room room = em.find(Room.class, roomId);
        
        room.setRoomType(roomType);
        em.merge(room);
    }
    
    @Override
    public void updateRoomNumber(Long roomId, Long roomNumber) {
        Room room = em.find(Room.class, roomId);
        
        room.setRoomNumber(roomNumber);
        em.merge(room);
    }
    
    @Override
    public void updateRoomAvailability(Long roomId, Boolean roomStatus) {
        Room room = em.find(Room.class, roomId);
        
        room.setRoomAvailability(roomStatus);
        em.merge(room);
    }
    
    @Override
    public void deleteRoom(Long roomId) {
        Room room = em.find(Room.class, roomId);
        
        em.remove(room);
    }
    
    @Override
    public List<Room> viewAllRoomTypes() throws RoomNotFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM Room r");
            return query.getResultList();
        } catch(Exception e) { 
            throw new RoomNotFoundException("Room not found");
        }
    }
    

    
}
