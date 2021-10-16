package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import util.providedinterface.ICopyable;

@Entity
public class Occupant implements Serializable, ICopyable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long occupantId;
    private String firstName;
    private String lastName;

    public Occupant() {
    }
    
    public Occupant(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
   
    public Long getOccupantId() {
        return occupantId;
    }

    public void setOccupantId(Long occupantId) {
        this.occupantId = occupantId;
    }
    
    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            Occupant occupantToCopy = (Occupant)object;
            this.setOccupantId(occupantToCopy.getOccupantId());
            this.setFirstName(occupantToCopy.getFirstName());
            this.setLastName(occupantToCopy.getLastName());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (occupantId != null ? occupantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the occupantId fields are not set
        if (!(object instanceof Occupant)) {
            return false;
        }
        Occupant other = (Occupant) object;
        if ((this.occupantId == null && other.occupantId != null) || (this.occupantId != null && !this.occupantId.equals(other.occupantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Occupant[ id=" + occupantId + " ]";
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
    
}