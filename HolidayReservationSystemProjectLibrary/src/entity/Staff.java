package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.AccessRightEnum;

import util.providedinterface.ICopyable;


@Entity
public class Staff implements Serializable, ICopyable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long staffId;
    private String firstName;
    private String lastName;
    private AccessRightEnum accessRightEnum;
    private String username;
    private String password;
    private List<Reservation> reservations;

    public Staff() {
        reservations = new ArrayList<>();
    }

    public Staff(String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password) 
    {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
    }

    public Staff(Long staffId, String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password)
    {
        this();
        
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;     
    }

    
    public Staff(Long staffId, String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password, List<Reservation> reservations)
    {
        this();
        
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
        this.reservations = reservations;
    }

    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            Staff staffToCopy = (Staff)object;
            this.setStaffId(staffToCopy.getStaffId());
            this.setFirstName(staffToCopy.getFirstName());
            this.setLastName(staffToCopy.getLastName());
            this.setAccessRightEnum(staffToCopy.getAccessRightEnum());
            this.setUsername(staffToCopy.getUsername());
            this.setPassword(staffToCopy.getPassword());
            this.setReservations(staffToCopy.getReservations());
        }
    }

    @Override
    public int hashCode() 
    {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the staffId fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Staff[ id=" + staffId + " ]";
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}