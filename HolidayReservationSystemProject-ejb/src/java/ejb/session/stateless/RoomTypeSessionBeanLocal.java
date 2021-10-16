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

    public Long createRoomType(RoomType roomType);

    public List<String> viewRoomTypeDetails(Long roomTypeId);

    public void updateRoomTypeName(Long roomTypeId, String name);

    public void updateRoomTypeDescription(Long roomTypeId, String description);

    public void updateRoomTypeSize(Long roomTypeId, Integer size);

    public void updateRoomTypeBed(Long roomTypeId, String bed);

    public void updateRoomTypeCapacity(Long roomTypeId, Integer capacity);

    public void updateRoomTypeAmenities(Long roomTypeId, String amenities);

    public void deleteRoomRate(Long roomTypeId);

    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException;
    
}
