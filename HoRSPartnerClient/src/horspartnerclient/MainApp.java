/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horspartnerclient;

import entity.Reservation;
import entity.Room;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java.util.GregorianCalendar;
import javax.persistence.NoResultException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author yeeda
 */
public class MainApp {
    
    private Partner currentPartner;
    HolidayReservationWebService_Service service;

    public MainApp(HolidayReservationWebService_Service service) {
        this.currentPartner = null;
        this.service = service;
    }
    
    public void run() {
        
        while(true) {
            try {
                while (true) {
                    System.out.println("*** HoRS :: Welcome to Holiday Reservation System For Partner! ***");
                    int response;

                    if (currentPartner != null) {
                        System.out.println("You are login as " + currentPartner.getPartnerName() + "\n");
                        response = loggedInDisplayMenu();
                    } else {
                        response = notLoggedInDisplayMenu();
                    }

                    if (response == 3) {
                        break;
                    }

                } 
            } catch (InvalidLoginCredentialException_Exception ex) {
                System.out.println(ex.getMessage() +"\n");
            } catch (NoMoreRoomsException_Exception ex) {
                System.out.println(ex.getMessage() +"\n");
            } catch (RoomTypeNotFoundException_Exception ex) {
                System.out.println(ex.getMessage() +"\n");
            }
        }
        
    }
    
    public int notLoggedInDisplayMenu() throws InvalidLoginCredentialException_Exception, NoMoreRoomsException_Exception, RoomTypeNotFoundException_Exception {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS For Partner :: Login Page ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doLogin();
                    System.out.println("Login successful as " + currentPartner.getPartnerName() + "!\n");
                    break;
                } else if (response == 2) {
                    Boolean isWalkIn = false;
                    Boolean isLoggedIn = false;
                    searchHotelRoom(isWalkIn, isLoggedIn);
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 1 || response == 3) {
                break;
            }
        }
        return response;
    }
    
    public int loggedInDisplayMenu() throws RoomTypeNotFoundException_Exception, NoMoreRoomsException_Exception {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS For Partner :: Homepage ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View All My Reservations With Details");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    Boolean isWalkIn = false;
                    Boolean isLoggedIn = true;
                    searchHotelRoom(isWalkIn, isLoggedIn);
                } else if (response == 2) { 
                    viewAllMyReservations();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
        return response;
    }
    
    private void doLogin() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** HoRS For Partner :: Login ***\n");
        System.out.print("Enter username> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        try {
            service = new HolidayReservationWebService_Service();    
            currentPartner = service.getHolidayReservationWebServicePort().partnerLogin(email, password);
        } catch (NoResultException ex) {
                System.out.println("Invalid login credential");
        }
    }
    
    public void searchHotelRoom(Boolean isWalkIn, Boolean isLoggedIn) throws RoomTypeNotFoundException_Exception, NoMoreRoomsException_Exception {
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        int numOfRoomsRequested = 0;
        Date checkInDate;
        Date checkOutDate;

        System.out.println("*** HoRS For Partner :: Search Hotel Room ***\n");

        try {
            System.out.print("Enter Check In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());
            
            XMLGregorianCalendar xmlCheckInDate = null;
            GregorianCalendar gcIn = new GregorianCalendar();
            gcIn.setTime(checkInDate);
            
            try {
                xmlCheckInDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcIn);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            XMLGregorianCalendar xmlCheckOutDate = null;
            GregorianCalendar gcOut = new GregorianCalendar();
            gcOut.setTime(checkOutDate);
            
            try {
                xmlCheckOutDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcOut);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            service = new HolidayReservationWebService_Service();
            List<RoomType> roomTypes = service.getHolidayReservationWebServicePort().viewAllRoomTypes();
            
            if(isLoggedIn == false) {
                System.out.printf("\n%30s%12s%29s\n", "Type of Room", "Price($)", "Number of Rooms Available");
                System.out.println("----------------------------------------------------------------------------");

                String[] roomNames = new String[roomTypes.size()];
                int[] roomTypePricesForDuration = new int[roomTypes.size()];
                int[] numOfRoomsAvailable = new int[roomTypes.size()];

                int seq = 1;
                for (RoomType roomType : roomTypes) {
                    roomNames[seq - 1] = roomType.getRoomName();
                    try {
                        roomTypePricesForDuration[seq - 1] = service.getHolidayReservationWebServicePort().calculatePrice(roomType.getRoomTypeId(), xmlCheckInDate, xmlCheckOutDate);
                        numOfRoomsAvailable[seq - 1] = service.getHolidayReservationWebServicePort().calculateNumOfRoomsAvailable(roomType.getRoomTypeId(), xmlCheckInDate, xmlCheckOutDate);
                    } catch (RoomTypeNotFoundException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    System.out.printf("%30s%12s%29s\n", roomNames[seq - 1], roomTypePricesForDuration[seq - 1], numOfRoomsAvailable[seq - 1]);
                    seq++;
                }
                System.out.println("----------------------------------------------------------------------------");
                System.out.println("Enter any key to return> ");
                scanner.nextLine();
                return;
            }
            else {
                System.out.println("Enter the respective number from 1 to " + roomTypes.size() + " to reserve that room");
                System.out.printf("\n%30s%12s%29s\n", "Type of Room", "Price($)", "Number of Rooms Available");
                System.out.println("------------------------------------------------------------------------");
                
                String[] roomNames = new String[roomTypes.size()];
                int[] roomTypePricesForDuration = new int[roomTypes.size()];
                int[] numOfRoomsAvailable = new int[roomTypes.size()];

                int seq = 1;
                for (RoomType roomType : roomTypes) {
                    System.out.print(seq + ": ");

                    roomNames[seq - 1] = roomType.getRoomName();
                    try {
                        roomTypePricesForDuration[seq - 1] = service.getHolidayReservationWebServicePort().calculatePrice(roomType.getRoomTypeId(), xmlCheckInDate, xmlCheckOutDate);
                        numOfRoomsAvailable[seq - 1] = service.getHolidayReservationWebServicePort().calculateNumOfRoomsAvailable(roomType.getRoomTypeId(), xmlCheckInDate, xmlCheckOutDate);
                    } catch (RoomTypeNotFoundException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    System.out.printf("%27s%12s%29s\n", roomNames[seq - 1], roomTypePricesForDuration[seq - 1], numOfRoomsAvailable[seq - 1]);
                    seq++;
                }
            
                System.out.println("------------------------------------------------------------------------");
                System.out.println((roomTypes.size() + 1) + ": Back\n");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == roomTypes.size() + 1) {
                    return;
                } else if (response < 1 || response > roomTypes.size() + 1) {
                    System.out.println("Invalid option! Returning...\n");
                    return;
                } else {
                    System.out.print("Enter number of rooms> ");
                    numOfRoomsRequested = scanner.nextInt();
                    scanner.nextLine();
                }

                int totalAmount = roomTypePricesForDuration[response - 1] * numOfRoomsRequested;
                System.out.printf("Confirm Reservation for %d of %s at $%d? (Enter 'Y' to complete checkout)> ", numOfRoomsRequested, roomNames[response - 1], totalAmount);

                String confirmCheckout = scanner.nextLine().trim();

                if (confirmCheckout.equals("Y")) {
                    horspartnerclient.Reservation newReservation = new horspartnerclient.Reservation();
                    newReservation.setTotalAmount(totalAmount);
                    newReservation.setCheckInDateTime(xmlCheckInDate);
                    newReservation.setCheckOutDateTime(xmlCheckOutDate);
                    newReservation.setNumOfRooms(numOfRoomsRequested);
                    newReservation.setIsAllocated(false);

                    Long reservationId = service.getHolidayReservationWebServicePort().createPartnerReservation(newReservation, currentPartner.getPartnerId(), roomTypes.get(response - 1).getRoomTypeId());
                    System.out.println("Reservation successful!\n");
                } else {
                    System.out.println("Reservation cancelled!\n");
                }
                return;
            }
        } 
        catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        } 
    }
    
    public void viewAllMyReservations() {
        Scanner scanner = new Scanner(System.in);
        int response;

        System.out.println("HoRS For Partner :: View All My Reservations with Details ***\n");

        service = new HolidayReservationWebService_Service();
        List<horspartnerclient.Reservation> reservations = service.getHolidayReservationWebServicePort().viewAllPartnerReservationsFor(currentPartner.getPartnerId());

        System.out.printf("%18s%20s%14s%20s%20s%20s\n", "Reservation ID", "Type of Room", "Quantity", "Check-In Date", "Check-Out Date", "Total Amount($)");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");

        List<horspartnerclient.Reservation> reservationList = new ArrayList<horspartnerclient.Reservation>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                
        int seq = 1;
        for (horspartnerclient.Reservation reservation : reservations) {
            reservationList.add(reservation);  
            System.out.print(seq++ + ": ");
            System.out.printf("%15s%20s%14s%20s%20s%20s\n", reservation.getReservationId().toString(), 
                    reservation.getRoomType().getRoomName(), 
                    reservation.getNumOfRooms(),
                    formatter.format(reservation.getCheckInDateTime().toGregorianCalendar().getTime()), 
                    formatter.format(reservation.getCheckOutDateTime().toGregorianCalendar().getTime()), 
                    reservation.getTotalAmount()
                    );
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
        System.out.println();
    }
}
