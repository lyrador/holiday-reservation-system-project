/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.InvalidLoginCredentialException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author raihan
 */
@WebService(serviceName = "holidayReservationWebService")
@Stateless()
public class HolidayReservationWebService {

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "email") String email, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        return partnerSessionBeanLocal.partnerLogin(email, password);
    }
    
    @WebMethod(operationName = "viewAllRoomTypes")
    public List<RoomType> viewAllRoomTypes() {
        
        List<RoomType> roomTypes = roomTypeSessionBeanLocal.viewAllRoomTypes();
        
        for (RoomType roomType : roomTypes) {
            
            em.detach(roomType);
            
            for (Room room : roomType.getRooms()) {
                em.detach(room);
                room.setRoomType(null);
            }
            
            for (RoomRate roomRate : roomType.getRoomRates()) {
                em.detach(roomRate);
                roomRate.setRoomType(null);
            }
            
            for (Reservation reservation : roomType.getReservations()) {
                em.detach(reservation);
                reservation.setRoomType(null);
            }
            
        }
        
        return roomTypes;
    }
    
    @WebMethod(operationName = "calculatePrice")
    public int calculatePrice(@WebParam(name = "roomTypeId") Long roomTypeId, @WebParam(name = "checkInDate") XMLGregorianCalendar checkInDate, @WebParam(name = "checkOutDate") XMLGregorianCalendar checkOutDate) throws RoomTypeNotFoundException {
        return roomTypeSessionBeanLocal.calculatePrice(roomTypeId, checkInDate.toGregorianCalendar().getTime(), checkOutDate.toGregorianCalendar().getTime(), false);
    }

    @WebMethod(operationName = "calculateNumOfRoomsAvailable")
    public int calculateNumOfRoomsAvailable(@WebParam(name = "roomTypeId") Long roomTypeId, @WebParam(name = "checkInDate") XMLGregorianCalendar checkInDate, @WebParam(name = "checkOutDate") XMLGregorianCalendar checkOutDate) throws RoomTypeNotFoundException {
        return roomTypeSessionBeanLocal.calculateNumOfRoomsAvailable(roomTypeId, checkInDate.toGregorianCalendar().getTime(), checkOutDate.toGregorianCalendar().getTime());
    }
    
    @WebMethod(operationName = "createPartnerReservation")
    public Long createPartnerReservation(@WebParam(name = "newPartnerReservation") Reservation newPartnerReservation, @WebParam(name = "partnerId") Long partnerId, @WebParam(name = "roomTypeId") Long roomTypeId) {
        return reservationSessionBeanLocal.createPartnerReservation(newPartnerReservation, partnerId, roomTypeId);
    }
    
    @WebMethod(operationName = "viewAllPartnerReservationsFor")
    public List<Reservation> viewAllPartnerReservationsFor(@WebParam(name = "partnerId") Long partnerId) {
        
        List<Reservation> partnerReservations = reservationSessionBeanLocal.viewAllPartnerReservationsFor(partnerId);
        Partner p = new Partner();
        List<Reservation> reservationsToRemove1 = new ArrayList<>();
        List<Reservation> reservationsToRemove2 = new ArrayList<>();
        
        for(Reservation partnerReservation : partnerReservations) {
            em.detach(partnerReservation);
            
            p = partnerReservation.getPartner();
            em.detach(p);
            if (p.getReservations() != null) {
                p.setReservations(null);
            }
            
            RoomType rt = partnerReservation.getRoomType();
            em.detach(rt);
            if (rt.getReservations() != null) {
                rt.setReservations(null);
            }
           

            for (Room room : partnerReservation.getRooms()) {
                em.detach(room);
                room.setReservation(null);
            }
        }
        
      
        return partnerReservations;
    }
}
