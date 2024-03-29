/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.RoomStatusEnum;

@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;  
    @Column(nullable = false, unique = true, length = 4)
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatusEnum roomAvailability;   
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = true)
    private Date dateOccupiedOn;
    @Column(nullable = false)
    private Boolean isEnabled;
    
    @ManyToOne(optional = false) // owning side
    @JoinColumn(nullable = false)
    private RoomType roomType;   
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Reservation reservation;

    public Room() {
        this.isEnabled = true;
    }

    public Room(String roomNumber, RoomStatusEnum roomAvailability, Date dateOccupiedOn, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomAvailability = roomAvailability;
        this.dateOccupiedOn = dateOccupiedOn;
        this.roomType = roomType;
        this.isEnabled = true;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStatusEnum getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(RoomStatusEnum roomAvailability) {
        this.roomAvailability = roomAvailability;
    }

    public Date getDateOccupiedOn() {
        return dateOccupiedOn;
    }

    public void setDateOccupiedOn(Date dateOccupiedOn) {
        this.dateOccupiedOn = dateOccupiedOn;
    }

    @XmlTransient
    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @XmlTransient
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }  
}
