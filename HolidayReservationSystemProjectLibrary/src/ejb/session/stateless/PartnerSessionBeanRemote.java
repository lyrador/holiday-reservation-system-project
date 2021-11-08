package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
import util.exception.PartnerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException;
    
    public Partner partnerLogin(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createNewPartner(Partner partner)  throws PartnerUsernameExistException, UnknownPersistenceException;
    
    public List<Partner> viewAllPartners();
   
}
