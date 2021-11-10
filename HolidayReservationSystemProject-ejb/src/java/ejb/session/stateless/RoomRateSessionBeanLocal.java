/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author raihan
 */
@Local
public interface RoomRateSessionBeanLocal {

    public RoomRate createRoomRate(RoomRate newRoomRate, RoomType roomType);

    public RoomRate retrieveRoomRateById(Long roomRateId) throws RoomRateNotFoundException;

    public void updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException;

    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException;

    public List<RoomRate> viewAllRoomRates();
    
}
