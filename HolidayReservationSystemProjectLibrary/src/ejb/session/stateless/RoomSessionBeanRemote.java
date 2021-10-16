/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomNotFoundException;

/**
 *
 * @author raihan
 */
@Remote
public interface RoomSessionBeanRemote {
    
    public Long createRoomType(Room room);

    public void updateRoomType(Long roomId, RoomType roomType);

    public void updateRoomNumber(Long roomId, Long roomNumber);

    public void updateRoomAvailability(Long roomId, Boolean roomStatus);

    public void deleteRoom(Long roomId);

    public List<Room> viewAllRoomTypes() throws RoomNotFoundException;
    
}
