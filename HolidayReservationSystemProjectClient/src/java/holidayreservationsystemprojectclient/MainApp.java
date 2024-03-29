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

import entity.Employee;

import java.util.Scanner;

import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;


public class MainApp {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemoteRemote;
    
    private SystemAdministrationModule systemAdminModule;
    private HotelOperationModule hotelOperationModule;
    private FrontOfficeModule frontOfficeModule;
    
    private Employee currentEmployee;
    
    public MainApp() {    
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemoteRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.guestSessionBeanRemoteRemote = guestSessionBeanRemoteRemote;
    }
    
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to HoRS :: Hotel Management System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        systemAdminModule = new SystemAdministrationModule(employeeSessionBeanRemote, partnerSessionBeanRemote, currentEmployee);
                        hotelOperationModule = new HotelOperationModule(roomTypeSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, reservationSessionBeanRemote, currentEmployee);
                        frontOfficeModule = new FrontOfficeModule(reservationSessionBeanRemote, guestSessionBeanRemoteRemote, roomTypeSessionBeanRemote, roomSessionBeanRemote, currentEmployee);
                        
                        menuMain();
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2) {
                break;
            }
        }
    }
    
    public void doLogin() throws InvalidLoginCredentialException {
        
        Scanner sc = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** HoRS System :: Employee Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);      
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    public void menuMain() {
         Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Hotel Management (HoRS) System ***\n");
            System.out.println("You are logged in as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRightEnum().toString() + " rights\n");
            System.out.println("1: System Administration");
            System.out.println("2: Hotel Operation");
            System.out.println("3: Front Office");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    try {
                        systemAdminModule.menuSystemAdministration();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2) {
                    try {
                        hotelOperationModule.menuHotelOperation();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    try {
                        frontOfficeModule.menuFrontOffice();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 4)
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
                System.out.println("You have succesfully logged out!");
                break;
            }
        }
    }
}
