/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author raihan
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public RoomType createRoomType(RoomType newRoomType);

    public RoomType viewRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException;

    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException;

    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;

    public List<RoomType> viewAllRoomTypes();
    
}
