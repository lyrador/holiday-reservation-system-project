/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author yeeda
 */

//This is the reservation client
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;
   
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, roomTypeSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, reservationSessionBeanRemote, partnerSessionBeanRemote, guestSessionBeanRemote);
        mainApp.run();
    }
    
}
