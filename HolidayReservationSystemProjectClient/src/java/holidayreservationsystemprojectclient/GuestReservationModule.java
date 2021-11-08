/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import java.util.Scanner;
import ejb.session.stateless.GuestSessionBeanRemote;
import entity.Guest;
import java.util.List;
import util.exception.GuestEmailExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author raihan
 */
public class GuestReservationModule {
    
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private Guest currentGuest;
    
    public GuestReservationModule() {
        this.currentGuest = null;
    }
    
    public GuestReservationModule(holidayReservationXXXSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this();
        this.holidayReservationXXXSessionBeanRemote = holidayReservationXXXSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
    }
    
    public void runApp()
    {
        while(true)
        {
            System.out.println("*** HoRS :: Guest Reservation System ***");
            System.out.println("*** Welcome to Guest Reservation System ***\n");
            
            if(currentGuest != null) {
                System.out.println("You are login as " + currentGuest.getFullName() + "\n");
                loggedInDisplayMenu();
            } else {
                notLoggedInDisplayMenu();
            }
        }
    }
    
    public void notLoggedInDisplayMenu() {
              
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
            
            if(response == 4)
            {
                break;
            }
        }
         
    }
    
    public void loggedInDisplayMenu() {
              
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
                    viewMyReservationDetails();
                }
                else if(response == 3)
                {
                    viewAllMyReservations();
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
            currentGuest = guestSessionBeanRemote.login(email, password);
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
    
   
}
