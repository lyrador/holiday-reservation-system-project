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

    public Long createRoomRate(RoomRate roomRate);

    public List<String> viewRoomRateDetails(Long roomRateId);

    public void updateRoomRateName(Long roomRateId, String name);

    public void updateRoomRateRoomType(Long roomRateId, RoomType roomType);

    public void updateRoomRateRateType(Long roomRateId, String rateType);

    public void updateRoomRateRatePerNight(Long roomRateId, Integer ratePerNight);

    public void updateRoomRateValidityStartDate(Long roomRateId, Date validityStartDate);

    public void updateRoomRateValidityEndDate(Long roomRateId, Date validityEndDate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRate> viewAllRoomRates() throws RoomRateNotFoundException;
    
}
