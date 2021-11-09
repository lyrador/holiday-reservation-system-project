package ejb.session.stateful;

import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 *
 * @author yeeda
 */
@Stateful
public class BookingSessionBean implements BookingSessionBeanRemote, BookingSessionBeanLocal {
    
    private BigDecimal totalAmount;
    private Date checkInDate;
    private Date checkOutDate;
    private Boolean reservationStatus;
    private Boolean voidRefundStatus;
    
    private List<Reservation> reservations;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Remove
   // @Override
    public void remove() {
        // Do nothing
    }
    
    
    
    @PreDestroy
    public void preDestroy() {
        if(reservations != null) {
            reservations.clear();
            reservations = null;
        }
    }
    
//    public List<Reservation> searchRoom(Date checkInDate, Date checkOutDate) {
//        
//    }
}
