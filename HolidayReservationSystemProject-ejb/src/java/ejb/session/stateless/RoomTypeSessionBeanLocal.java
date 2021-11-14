/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author raihan
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public RoomType createRoomType(RoomType newRoomType);

    public RoomType retrieveRoomTypeByRoomId(Long roomTypeId) throws RoomTypeNotFoundException;

    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException;

    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException;

    public List<RoomType> viewAllRoomTypes();

    public RoomType retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException;
    
    public int calculatePrice(Long roomTypeId, Date checkInDate, Date checkOutDate, Boolean isWalkIn) throws RoomTypeNotFoundException;
    
    public int calculateNumOfRoomsAvailable(Long roomTypeId, Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException;

    public int calculateTotalNumOfRoomsAvailable(Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException;

    public int calculateTotalNumOfRooms(Date checkInDate, Date checkOutDate);
    
}
