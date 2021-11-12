/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;

import entity.Guest;
import entity.Reservation;
import entity.RoomType;

import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.exception.InvalidLoginCredentialException;
import javax.persistence.NoResultException;
import util.exception.GuestEmailExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;

public class MainApp {

    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;

    private Guest currentGuest;

    public MainApp() {
        this.currentGuest = null;
    }

    public MainApp(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
    }

    public void run() {
        while (true) {
            System.out.println("*** HoRS :: Welcome to Holiday Reservation System For Guest! ***");
            int responseFromLoggedInMenu = 0;
            int responseFromNotLoggedInMenu = 0;

            if (currentGuest != null) {
                System.out.println("You are login as " + currentGuest.getFirstName() + "\n");
                responseFromLoggedInMenu = loggedInDisplayMenu();
                currentGuest = null;
                System.out.println("You have successfully logged out!***\n");
            } else {
                responseFromNotLoggedInMenu = notLoggedInDisplayMenu();
            }

            if (responseFromNotLoggedInMenu == 4) {
                break;
            }
        }
        System.out.println("You have successfully exited the system!***\n");
    }

    public int notLoggedInDisplayMenu() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS For Guest :: Login Page ***\n");
            System.out.println("1: Guest Login");
            System.out.println("2: Register As Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentGuest.getFirstName() + "!\n");
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                    break;
                } else if (response == 2) {
                    createGuest();
                } else if (response == 3) {
                    Boolean isWalkIn = false;
                    Boolean isLoggedIn = false;
                    searchHotelRoom(isWalkIn, isLoggedIn);
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 1 || response == 4) {
                break;
            }
        }
        return response;
    }

    public int loggedInDisplayMenu() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS For Guest :: Homepage ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View All My Reservations with Details");
            System.out.println("3: Logout\n");
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

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** HoRS For Guest :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            try {
                currentGuest = guestSessionBeanRemote.login(email, password);
            } catch (NoResultException ex) {
                System.out.println("Invalid login credential");
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    public void createGuest() {
        Scanner scanner = new Scanner(System.in);
        Guest newGuest = new Guest();

        System.out.println("*** HoRS For Guest :: Create New Guest ***\n");
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
        } catch (GuestEmailExistException ex) {
            System.out.println("An error has occurred while creating the new guest!: The email already exist\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("An unknown error has occurred while creating the new guest!: " + ex.getMessage() + "\n");
        }
    }

    public void searchHotelRoom(Boolean isWalkIn, Boolean isLoggedIn) {
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        int numOfRoomsRequested = 0;
        Date checkInDate;
        Date checkOutDate;

        System.out.println("*** HoRS For Guest :: Search Hotel Room ***\n");

        try {
            System.out.print("Enter Check In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

            List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
            
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
                        roomTypePricesForDuration[seq - 1] = roomTypeSessionBeanRemote.calculatePrice(roomType.getRoomTypeId(), checkInDate, checkOutDate, isWalkIn);
                        numOfRoomsAvailable[seq - 1] = roomTypeSessionBeanRemote.calculateNumOfRoomsAvailable(roomType.getRoomTypeId(), checkInDate, checkOutDate);
                    } catch (RoomTypeNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }

                    System.out.printf("%30s%12s%29d\n", roomNames[seq - 1], roomTypePricesForDuration[seq - 1], numOfRoomsAvailable[seq - 1]);
                    seq++;
                }
                System.out.println("------------------------------------------------------------------------");
                System.out.println("Enter any key to return> ");
                scanner.nextLine();
                return;
            } else {
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
                        roomTypePricesForDuration[seq - 1] = roomTypeSessionBeanRemote.calculatePrice(roomType.getRoomTypeId(), checkInDate, checkOutDate, isWalkIn);
                        numOfRoomsAvailable[seq - 1] = roomTypeSessionBeanRemote.calculateNumOfRoomsAvailable(roomType.getRoomTypeId(), checkInDate, checkOutDate);
                    } catch (RoomTypeNotFoundException ex) {
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
                    Reservation newReservation = new Reservation(totalAmount, checkInDate, checkOutDate, numOfRoomsRequested, false);
                    Long reservationId = reservationSessionBeanRemote.createReservation(newReservation, currentGuest.getGuestId(), roomTypes.get(response - 1).getRoomTypeId());
                    System.out.println("Reservation successful!\n");
                } else {
                    System.out.println("Reservation cancelled!\n");
                }
                return;
            }  
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    public void viewAllMyReservations() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** HoRS For Guest :: View All My Reservations ***\n");

        List<Reservation> reservations = reservationSessionBeanRemote.viewAllMyReservations(currentGuest.getGuestId());

        System.out.printf("%18s%30s%14s%20s%20s%20s\n", "Reservation ID", "Type of Room", "Quantity", "Check-In Date", "Check-Out Date", "Total Amount($)");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");

        List<Reservation> reservationList = new ArrayList<Reservation>();
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        int seq = 1;
        for (Reservation reservation : reservations) {
            reservationList.add(reservation);
            System.out.print(seq++ + ": ");
            System.out.printf("%15s%30s%14s%20s%20s%20s\n", reservation.getReservationId().toString(), 
                    reservation.getRoomType().getRoomName(), 
                    reservation.getNumOfRooms(),
                    formatter.format(reservation.getCheckInDateTime()), 
                    formatter.format(reservation.getCheckOutDateTime()), 
                    reservation.getTotalAmount());
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
