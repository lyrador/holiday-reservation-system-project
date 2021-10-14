package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import util.providedinterface.ICopyable;

@Entity
public class GuestEntity implements Serializable, ICopyable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long guestId;
    private String firstName;
    private String lastName;
    private String guestEmail;

    public GuestEntity() {
    }
    
    public GuestEntity(String firstName, String lastName, String guestEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.guestEmail = guestEmail;
    }
   
    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
    
    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            GuestEntity guestEntityToCopy = (GuestEntity)object;
            this.setGuestId(guestEntityToCopy.getGuestId());
            this.setFirstName(guestEntityToCopy.getFirstName());
            this.setLastName(guestEntityToCopy.getLastName());
            this.setEmail(guestEntityToCopy.getEmail());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guestId != null ? guestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestId fields are not set
        if (!(object instanceof GuestEntity)) {
            return false;
        }
        GuestEntity other = (GuestEntity) object;
        if ((this.guestId == null && other.guestId != null) || (this.guestId != null && !this.guestId.equals(other.guestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + guestId + " ]";
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

    public String getEmail() {
        return guestEmail;
    }

    public void setEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }
    
}