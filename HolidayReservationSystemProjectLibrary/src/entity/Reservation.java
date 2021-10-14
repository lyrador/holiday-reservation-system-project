package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import util.providedinterface.ICopyable;

//import util.exception.EntityInstanceExistsInCollectionException;
//import util.exception.EntityInstanceMissingInCollectionException;


@Entity
public class Reservation implements Serializable, ICopyable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long reservationId;   
    private BigDecimal totalAmount;
    private Date checkInDateTime;
    private Date checkOutDateTime;    
    private Boolean reservationStatus;
    private Boolean voidRefundStatus;
    
    private Room room;
    
    public Reservation()
    {
        room = new Room();
        reservationStatus = false;
        voidRefundStatus = false;
    }
    
    
    
    public Reservation(Long reservationId, BigDecimal totalAmount, Date checkInDateTime, Date checkOutDateTime, Boolean reservationStatus, Boolean voidRefundStatus)
    {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.checkInDateTime = checkInDateTime;
        this.reservationStatus = reservationStatus;
        this.voidRefundStatus = voidRefundStatus;       
    }
    
    
    
    public Reservation(Long reservationId, BigDecimal totalAmount, Date checkInDateTime, Date checkOutDateTime, Boolean reservationStatus, Boolean voidRefundStatus, Room room)
    {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.checkInDateTime = checkInDateTime;
        this.reservationStatus = reservationStatus;
        this.voidRefundStatus = voidRefundStatus;       
        this.room = room;
    }

    
    
    public Reservation(BigDecimal totalAmount, Date checkInDateTime, Date checkOutDateTime, Boolean reservationStatus, Boolean voidRefundStatus, Room room)
    {
        this.totalAmount = totalAmount;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.checkInDateTime = checkInDateTime;
        this.reservationStatus = reservationStatus;
        this.voidRefundStatus = voidRefundStatus;       
        this.room = room;
    }   
    
    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            Reservation reservationToCopy = (Reservation)object;
            this.setReservationId(reservationToCopy.getReservationId());
            this.setTotalAmount(reservationToCopy.getTotalAmount());
            this.setCheckInDateTime(reservationToCopy.getCheckInDateTime());
            this.setCheckOutDateTime(reservationToCopy.getCheckOutDateTime());
            this.setReservationStatus(reservationToCopy.getReservationStatus());
            this.setVoidRefundStatus(reservationToCopy.getVoidRefundStatus());
            this.setRoomEntity(reservationToCopy.getRoom());   
            
        }
    }
 
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.reservationId != null ? this.reservationId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Reservation)) 
        {
            return false;
        }
        
        Reservation other = (Reservation) object;
        
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.Reservation[ reservationId=" + this.reservationId + " ]";
    }
    
    
    
//    public void addRoomEntity(RoomEntity roomEntity) throws EntityInstanceExistsInCollectionException
//    {
//        if(!this.saleTransactionLineItemEntities.contains(roomEntity))
//        {
//            this.saleTransactionLineItemEntities.add(roomEntity);
//        }
//        else
//        {
//            throw new EntityInstanceExistsInCollectionException("Room already exist");
//        }
//    }
//    
//    
//    
//    public void removeRoomEntity(RoomEntity roomEntity) throws EntityInstanceMissingInCollectionException
//    {
//        if(this..contains(roomEntity))
//        {
//            this.saleTransactionLineItemEntities.remove(roomEntity);
//        }
//        else
//        {
//            throw new EntityInstanceMissingInCollectionException("Room missing");
//        }
//    }
    
    
    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(Date checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }
    
    public Date getCheckOutDateTime() {
        return checkOutDateTime;
    }
    
    public Boolean getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(Boolean reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
    
    public Boolean getVoidRefundStatus() {
        return voidRefundStatus;
    }

    public void setVoidRefundStatus(Boolean voidRefundStatus) {
        this.voidRefundStatus = voidRefundStatus;
    }

    public void setCheckOutDateTime(Date checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoomEntity(Room room) {
        this.room = room;
    }   
    
//    public StaffEntity getStaffEntity() {
//        return staffEntity;
//    }
//
//    public void setStaffEntity(StaffEntity staffEntity) 
//    {
//        if(this.staffEntity != null)
//        {
//            this.staffEntity.getReservationEntities().remove(this);
//        }
//        
//        this.staffEntity = staffEntity;
//        
//        if(this.staffEntity != null)
//        {
//            if(!this.staffEntity.getReservationEntities().contains(this))
//            {
//                this.staffEntity.getReservationEntities().add(this);
//            }
//        }
//    }
}