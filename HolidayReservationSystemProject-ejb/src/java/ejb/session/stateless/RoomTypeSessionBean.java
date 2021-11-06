/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.roomTypeName = :inRoomTypeName");
        query.setParameter("inRoomTypeName", name);
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
        
        if(roomType != null && roomType.getRoomTypeId()!= null) {
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
        
        if (roomTypeToRemove.getRooms().isEmpty() && roomTypeToRemove.getRooms().isEmpty() && roomTypeToRemove.getReservations().isEmpty()) {
            em.remove(roomTypeToRemove);
        } else {
            throw new DeleteRoomTypeException("Room Type ID " + roomTypeId + " is currently in use and cannot be deleted!");
        }
    }

    @Override
    public List<RoomType> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT r FROM RoomType r");
        
        return query.getResultList();
    }
    
}
