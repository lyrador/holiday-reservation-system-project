/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRoomException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author raihan
 */
@Remote
public interface RoomSessionBeanRemote {
    
    public Room createRoom(Room newRoom);

    public void updateRoom(Room room) throws RoomNotFoundException;

    public void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException ;

    public List<Room> viewAllRooms();
    
    public Room retrieveRoomByRoomId(Long roomId) throws RoomNotFoundException;
    
    public Room retrieveRoomByRoomNumber(Integer roomNumber) throws RoomNotFoundException;
    
}
