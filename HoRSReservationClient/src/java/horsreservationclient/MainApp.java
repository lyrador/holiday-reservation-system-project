/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateful.BookingSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Guest;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import util.exception.GuestEmailExistException;
import util.exception.UnknownPersistenceException;


public class MainApp {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBeanRemote;
    
    private Guest currentGuest;
    
    public MainApp() {
        this.currentGuest = null;
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, BookingSessionBeanRemote bookingSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.bookingSessionBeanRemote = bookingSessionBeanRemote;
    }
    
    public void run() {
        while(true)
        {
            System.out.println("*** HoRS :: Guest Reservation System ***");
            System.out.println("*** Welcome to Guest Reservation System ***\n");
            int response;
            
            if(currentGuest != null) {
                System.out.println("You are login as " + currentGuest.getFirstName() + "\n");
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
    
    public int notLoggedInDisplayMenu() {
              
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS :: Hotel Reservation System ***\n");
            System.out.println("1: Guest Login");
            System.out.println("2: Register As Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentGuest.getFirstName() + "!\n");                                                
                    }
                    catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                    break;
                }
                else if(response == 2) {
                    createGuest();
                }
                else if(response == 3)
                {
                    searchHotelRoom();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 1 || response == 4)
            {
                break;
            }
        }
        return response;
    }
    
    public int loggedInDisplayMenu() {
              
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS :: Hotel Reservation System ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservations");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    searchHotelRoom();
                }
                else if(response == 2) {
                    System.out.println("viewMyReservationDetails()");
                    //viewMyReservationDetails();
                }
                else if(response == 3)
                {
                    System.out.println("viewAllMyReservations()");
                    //viewAllMyReservations();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
        return response;      
    }
     
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        
        System.out.println("*** Guest Reservation System :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(email.length() > 0 && password.length() > 0) {
            try {
                currentGuest = guestSessionBeanRemote.login(email, password);
            } 
            catch (NoResultException ex) {
                System.out.println("Invalid login credential");
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
         
    public void createGuest() {
        Scanner scanner = new Scanner(System.in);
        Guest newGuest = new Guest();
        
        System.out.println("*** POS System :: System Administration :: Create New Guest ***\n");
        System.out.print("Enter First Name> ");
        newGuest.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newGuest.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Email> ");
        newGuest.setEmail(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newGuest.setPassword(scanner.nextLine().trim());
        
        try {
            Long newGuestId = guestSessionBeanRemote.createNewGuest(newGuest);
            System.out.println("New guest created successfully!: " + newGuestId + "\n");
        }
        catch(GuestEmailExistException ex) {
            System.out.println("An error has occurred while creating the new guest!: The email already exist\n");
        }
        catch(UnknownPersistenceException ex) {
            System.out.println("An unknown error has occurred while creating the new guest!: " + ex.getMessage() + "\n");
        }
    }
    
    private void searchHotelRoom() {
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        Scanner scanner = new Scanner(System.in);
        int numOfRooms = 0;
        Date checkInDate;
        Date checkOutDate;
        
        System.out.println("*** Guest Reservation System :: Login ***\n");
        System.out.print("Enter number of rooms> ");
        numOfRooms = scanner.nextInt();
        scanner.nextLine();
        
        try {
            System.out.print("Enter Check In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

//            List<RoomType> roomTypes = roomTypeSessionBeanRemote.searchHotelRooms(checkInDate, checkOutDate);
//            
//            System.out.println("Enter the respective number from 1 to " + roomTypes.size() + " to reserve that room");
//            
//            System.out.printf("%8s%22s   %s\n", "Type of Room", "Price", "Number of Rooms Available");
//            
//            for(RoomType roomType:roomTypes)
//            {
//                int seq = 1;
//                System.out.print(seq + ": ");
//                System.out.printf("%8s%22s   %s\n", roomType.getRoomName(), roomTypeSessionBeanRemote.calculatePrice(roomType, checkInDate, checkOutDate), roomSessionBeanRemote.calculateNumOfRoomsAvailable(roomType));
//                seq++;
//            }
//            
//            System.out.println((roomTypes.size() + 1) + ": Back\n");
            
            int i;
            for(i = 0; i < 5; i++) {
                System.out.println(i + ": hello");
            }
            System.out.println(i + ": Back\n");
            
            System.out.print("> ");
            response = scanner.nextInt();
        }
        catch(ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }
}
