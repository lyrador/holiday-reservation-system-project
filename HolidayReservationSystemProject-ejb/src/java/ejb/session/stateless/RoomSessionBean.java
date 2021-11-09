/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Room;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.DeleteRoomException;
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
    public Room retrieveRoomByRoomId(Long roomId) throws RoomNotFoundException {
        Room roomType = em.find(Room.class, roomId);
        
        if (roomType != null) {
            return roomType;
        } else {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }

    }
    
    @Override
    public Room retrieveRoomByRoomNumber(Integer roomNumber) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :inRoomNumber");
        query.setParameter("inRoomNumber", roomNumber);
        
        try {
            Room room = (Room) query.getSingleResult();
            return room;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomNotFoundException("Room: " + roomNumber + " does not exist!");
        }

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
    public void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException { //not complete
        Room roomToRemove = retrieveRoomByRoomId(roomId);
        
        if (roomToRemove.getRoomAvailability().equals(RoomStatusEnum.AVAILABLE)) {
            roomToRemove.getRoomType().getRooms().remove(roomToRemove);
            em.remove(roomToRemove);
        } else {
            throw new DeleteRoomException("Room ID " + roomId + " is currently in use and cannot be deleted!");
        }

  
    }
    
    @Override
    public List<Room> viewAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r");
        
        return query.getResultList();
    }
    
    @Override
    public List<ExceptionReport> generateRoomAllocationExceptionReport(){
        Query query = em.createQuery("SELECT e FROM ExceptionReport e");
        return query.getResultList();
    }
    
    @Override
    public List<Room> retrieveAvailableRooms() {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomAvailability = :inRoomAvailability");
        query.setParameter("inRoomAvailability", RoomStatusEnum.AVAILABLE);
        
        return query.getResultList();
    }
    

    
}
