/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author yeeda
 */

//This is the reservation client
public class Main {

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;

    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(roomTypeSessionBeanRemote, reservationSessionBeanRemote, guestSessionBeanRemote);
        mainApp.run();
    }
    
}
