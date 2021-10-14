package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import util.providedinterface.ICopyable;

@Entity
public class PartnerEntity implements Serializable, ICopyable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long partnerId;
    private String partnerName;
    private String username;
    private String password;

    public PartnerEntity() {
    }

    public PartnerEntity(String partnerName) {
        this.partnerName = partnerName;
    }
    
    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            PartnerEntity partnerEntityToCopy = (PartnerEntity)object;
            this.setPartnerId(partnerEntityToCopy.getPartnerId());
            this.setPartnerName(partnerEntityToCopy.getPartnerName());
            this.setUsername(partnerEntityToCopy.getUsername());
            this.setPassword(partnerEntityToCopy.getPassword());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }
    
    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
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
    
}
