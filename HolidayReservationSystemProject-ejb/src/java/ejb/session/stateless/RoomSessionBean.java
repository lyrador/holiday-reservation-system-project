/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomNotFoundException;

@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    @Override
    public Room createRoom(Room newRoom) {
        em.persist(newRoom);
        em.flush();
        
        return newRoom;
    }
    
    @Override
    public void updateRoom(Room room) throws RoomNotFoundException {
        
        if(room != null && room.getRoomId()!= null) {
            Room roomToUpdate = em.find(Room.class, room.getRoomId());
            
            roomToUpdate.setRoomType(room.getRoomType());
            roomToUpdate.setRoomNumber(room.getRoomNumber());
            roomToUpdate.setRoomAvailability(room.getRoomAvailability());
            
        } else {
            throw new RoomNotFoundException("Room ID not provided for room to be updated");
        }
    }
    
    
    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException { //not complete
        Room room = em.find(Room.class, roomId);
        
        if (room != null) {
            em.remove(room);
        } else {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }
        
        em.remove(room);
    }
    
    @Override
    public List<Room> viewAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r");
        
        return query.getResultList();
    }
    

    
}
