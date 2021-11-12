package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import util.providedinterface.ICopyable;

@Entity
public class Occupant implements Serializable, ICopyable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long occupantId;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true, length = 64)
    private String occupantEmail;
    
    @OneToMany(mappedBy = "occupant", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Occupant() {
        reservations = new ArrayList<>();
    }
    
    public Occupant(String firstName, String lastName) {
        this();
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
    public void copy(Object object) {
        if(object.getClass().equals(this.getClass())) {
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

    public String getOccupantEmail() {
        return occupantEmail;
    }

    public void setOccupantEmail(String occupantEmail) {
        this.occupantEmail = occupantEmail;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}