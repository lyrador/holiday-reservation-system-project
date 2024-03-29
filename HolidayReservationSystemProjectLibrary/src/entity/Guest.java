package entity;

import java.io.Serializable;
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
public class Guest implements Serializable, ICopyable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true, length = 64)
    private String guestEmail;
    @Column(nullable = false, length = 32)
    private String password;
    
    @OneToMany(mappedBy = "guest", orphanRemoval = false, cascade = {}, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Guest() {
    }
    
    public Guest(String firstName, String lastName, String guestEmail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.guestEmail = guestEmail;
        this.password = password;
    }
   
    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
    
    @Override
    public void copy(Object object) {
        if(object.getClass().equals(this.getClass())) {
            Guest guestToCopy = (Guest)object;
            this.setGuestId(guestToCopy.getGuestId());
            this.setFirstName(guestToCopy.getFirstName());
            this.setLastName(guestToCopy.getLastName());
            this.setEmail(guestToCopy.getEmail());
            this.setPassword(guestToCopy.getPassword());
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
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
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
        return getGuestEmail();
    }

    public void setEmail(String guestEmail) {
        this.setGuestEmail(guestEmail);
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}