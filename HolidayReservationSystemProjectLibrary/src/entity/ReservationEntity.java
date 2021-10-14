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
public class ReservationEntity implements Serializable, ICopyable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long reservationId;   
    private BigDecimal totalAmount;
    private Date checkInDateTime;
    private Date checkOutDateTime;    
    private Boolean reservationStatus;
    private Boolean voidRefundStatus;
    
    private RoomEntity roomEntity;
    
    public ReservationEntity()
    {
        roomEntity = new RoomEntity();
        reservationStatus = false;
        voidRefundStatus = false;
    }
    
    
    
    public ReservationEntity(Long reservationId, BigDecimal totalAmount, Date checkInDateTime, Date checkOutDateTime, Boolean reservationStatus, Boolean voidRefundStatus)
    {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.checkInDateTime = checkInDateTime;
        this.reservationStatus = reservationStatus;
        this.voidRefundStatus = voidRefundStatus;       
    }
    
    
    
    public ReservationEntity(Long reservationId, BigDecimal totalAmount, Date checkInDateTime, Date checkOutDateTime, Boolean reservationStatus, Boolean voidRefundStatus, RoomEntity roomEntity)
    {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.checkInDateTime = checkInDateTime;
        this.reservationStatus = reservationStatus;
        this.voidRefundStatus = voidRefundStatus;       
        this.roomEntity = roomEntity;
    }

    
    
    public ReservationEntity(BigDecimal totalAmount, Date checkInDateTime, Date checkOutDateTime, Boolean reservationStatus, Boolean voidRefundStatus, RoomEntity roomEntity)
    {
        this.totalAmount = totalAmount;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
        this.checkInDateTime = checkInDateTime;
        this.reservationStatus = reservationStatus;
        this.voidRefundStatus = voidRefundStatus;       
        this.roomEntity = roomEntity;
    }
    
    
    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            ReservationEntity reservationEntityToCopy = (ReservationEntity)object;
            this.setReservationId(reservationEntityToCopy.getReservationId());
            this.setTotalAmount(reservationEntityToCopy.getTotalAmount());
            this.setCheckInDateTime(reservationEntityToCopy.getCheckInDateTime());
            this.setCheckOutDateTime(reservationEntityToCopy.getCheckOutDateTime());
            this.setReservationStatus(reservationEntityToCopy.getReservationStatus());
            this.setVoidRefundStatus(reservationEntityToCopy.getVoidRefundStatus());
            this.setRoomEntity(reservationEntityToCopy.getRoomEntity());   
            
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
        if (!(object instanceof ReservationEntity)) 
        {
            return false;
        }
        
        ReservationEntity other = (ReservationEntity) object;
        
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.ReservationEntity[ reservationId=" + this.reservationId + " ]";
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

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
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