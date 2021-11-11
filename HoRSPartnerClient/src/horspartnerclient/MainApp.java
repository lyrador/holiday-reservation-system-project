/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horspartnerclient;

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
    
    public void run() throws InvalidLoginCredentialException_Exception {
        while (true) {
            System.out.println("*** HoRS :: Holiday Reservation System For Partner***");
            System.out.println("*** Welcome to Holiday Reservation System For Partner ***\n");
            int response;

            if (currentPartner != null) {
                System.out.println("You are login as " + currentPartner.getPartnerName() + "\n");
                response = loggedInDisplayMenu();
            } else {
                response = notLoggedInDisplayMenu();
            }

            if (response == 4) {
                break;
            }

        }
        System.out.println("You have succesfully exited the system!***\n");
    }
    
    public int notLoggedInDisplayMenu() throws InvalidLoginCredentialException_Exception {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS :: Hotel Reservation System ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: Back\n");
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
                    searchHotelRoom(isWalkIn);
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
    
    public int loggedInDisplayMenu() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS :: Hotel Reservation System ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservations");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    Boolean isWalkIn = false;
                    searchHotelRoom(isWalkIn);
                } else if (response == 2) {
                    //viewMyReservationDetails();
                } else if (response == 3) {
                    viewAllMyReservations();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
        return response;
    }
    
    private void doLogin() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** Partner Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        try {
                currentPartner = service.getHolidayReservationWebServicePort().partnerLogin(email, password);
            } catch (NoResultException ex) {
                System.out.println("Invalid login credential");
        }
    }
    
    public void searchHotelRoom(Boolean isWalkIn) {
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        Scanner scanner = new Scanner(System.in);
        int numOfRoomsRequested = 0;
        Date checkInDate;
        Date checkOutDate;

        System.out.println("*** Holiday Reservation System :: Search Hotel Room ***\n");

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

            System.out.println("Enter the respective number from 1 to " + roomTypes.size() + " to reserve that room");

            System.out.printf("%8s%22s%30s\n", "Type of Room", "Price($)", "Number of Rooms Available");

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
                
                System.out.printf("%20s%22s%10d\n", roomNames[seq - 1], roomTypePricesForDuration[seq - 1], numOfRoomsAvailable[seq - 1]);
                seq++;
            }

            System.out.println((roomTypes.size() + 1) + ": Back\n");

            response = scanner.nextInt();
            scanner.nextLine();

            if (response == roomTypes.size() + 1) {
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
                Reservation newReservation = new Reservation();
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
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }
    
    public void viewAllMyReservations() {
        Scanner scanner = new Scanner(System.in);
        int response;

        System.out.println("*** POS System :: System Administration :: View All My Reservations ***\n");

        service = new HolidayReservationWebService_Service();
        List<Reservation> reservations = service.getHolidayReservationWebServicePort().viewAllPartnerReservationsFor(currentPartner.getPartnerId());

        System.out.println("Enter the respective number from 1 to " + reservations.size() + " to view rooms of that reservation");

        System.out.printf("%20s%20s%8s%20s%20s%20s%20s\n", "   Reservation ID", "Type of Room", "Quantity", "Check-In Date", "Check-Out Date", "Total Amount($)", "Allocated Status");

        List<Reservation> reservationList = new ArrayList<Reservation>();

        for (Reservation reservation : reservations) {
            reservationList.add(reservation);
            int seq = 1;
            System.out.print(seq++ + ": ");
            System.out.printf("%8s%20s%20d%20s%20s%20d\n", reservation.getReservationId().toString(), 
                    reservation.getRoomType().getRoomName(), 
                    reservation.getNumOfRooms(),
                    reservation.getCheckInDateTime().toString(), 
                    reservation.getCheckOutDateTime().toString(), 
                    reservation.getTotalAmount()
                    );
        }

        System.out.println((reservations.size() + 1) + ": Back\n");

        response = scanner.nextInt();
        scanner.nextLine();

        if (response == reservations.size() + 1) {
            return;
        } else {
            System.out.println("Displaying Rooms for Reservation: ");
            for (Room room : reservations.get(response-1).getRooms()) {
                System.out.printf("%8s%20s%20s\n", room.getRoomId().toString(), room.getRoomNumber().toString());
            }
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
