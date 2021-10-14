package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.PartnerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author yeeda
 */
@Local
public interface PartnerSessionBeanLocal {

    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException;

    public Partner login(String email, String password) throws InvalidLoginCredentialException;

    public Long createNewPartner(Partner partner);
    
}
