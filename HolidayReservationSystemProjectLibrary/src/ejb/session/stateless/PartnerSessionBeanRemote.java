package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Remote;
import util.exception.PartnerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException;
    
    public Partner login(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createNewPartner(Partner partner);
   
}
