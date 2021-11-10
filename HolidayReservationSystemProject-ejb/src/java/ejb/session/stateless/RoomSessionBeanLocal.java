/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author raihan
 */
@Local
public interface RoomSessionBeanLocal {

    public Room createRoom(Room newRoom);

    public void updateRoom(Room room) throws RoomNotFoundException;

    public void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException ;

    public List<Room> viewAllRooms();

    public Room retrieveRoomByRoomId(Long roomId) throws RoomNotFoundException;

    public Room retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public List<ExceptionReport> generateRoomAllocationExceptionReport();

    public List<Room> retrieveAvailableRooms();
    
}
