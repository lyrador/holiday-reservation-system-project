/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horspartnerclient;

/**
 *
 * @author yeeda
 */
public class HoRSPartnerClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidLoginCredentialException_Exception {
        // TODO code application logic here
        HolidayReservationWebService_Service service = new HolidayReservationWebService_Service();
        
        MainApp mainApp = new MainApp(service);
        mainApp.run();
    }
    
}
