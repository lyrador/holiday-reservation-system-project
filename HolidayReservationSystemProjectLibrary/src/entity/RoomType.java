/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    
    @Column(nullable = false, unique = true)
    private String roomName;
    
    @Column(nullable = false)
    private String roomDescription;
    
    @Column(nullable = false)
    private Integer roomSize;
    
    @Column(nullable = false)
    private String roomBed;
    
    @Column(nullable = false)
    private Integer roomCapacity;
    
    @Column(nullable = false)
    private String roomAmenities;
    
    @OneToMany(mappedBy="roomType") // owned side
    private List<Room> rooms;
    
    @OneToMany(mappedBy="roomType") // owned side
    private List<Reservation> reservations;
    
    @OneToMany(mappedBy="roomType") // owned side
    private List<RoomRate> roomRates;

    public RoomType() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.roomRates = new ArrayList<>();
    }

    public RoomType(String roomName, String roomDescription, Integer roomSize, String roomBed, Integer roomCapacity, String roomAmenities, List<Room> rooms, List<Reservation> reservations, List<RoomRate> roomRates) {
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomSize = roomSize;
        this.roomBed = roomBed;
        this.roomCapacity = roomCapacity;
        this.roomAmenities = roomAmenities;
        this.rooms = rooms;
        this.reservations = reservations;
        this.roomRates = roomRates;
    }

    
    
    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public Integer getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Integer roomSize) {
        this.roomSize = roomSize;
    }

    public String getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(String roomBed) {
        this.roomBed = roomBed;
    }

    public Integer getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomAmenities() {
        return roomAmenities;
    }

    public void setRoomAmenities(String roomAmenities) {
        this.roomAmenities = roomAmenities;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }
    
}
