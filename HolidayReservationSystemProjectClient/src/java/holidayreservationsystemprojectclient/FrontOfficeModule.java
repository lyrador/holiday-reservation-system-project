/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Guest;
import entity.Occupant;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author raihan
 */
public class FrontOfficeModule {
    
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemoteRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    
    private Employee currentEmployee;
    
    public FrontOfficeModule(ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, Employee currentEmployee) {
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemoteRemote = guestSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    } 
    
    public FrontOfficeModule() {
        
    }
   
    public void menuFrontOffice() throws InvalidAccessRightException {
        
        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.GUEST_RELATIONS_OFFICER) {
            throw new InvalidAccessRightException("You don't have GUEST RELATIONS OFFICER rights to access the front office module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            
            System.out.println("*** HoRS :: Front Office ***\n");
            System.out.println("1: Walk-in Search/Reserve Room");
            System.out.println("-----------------------");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("-----------------------");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    
                    walkInSearchRoom(true);
                    
                } else if(response == 2) {
                    
                    checkInGuest();
                    
                } else if(response == 3) {
                    
                    checkOutGuest();
                    
                } else if(response == 4) {
                    
                    break;
                    
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4) {
                break;
            }
        }
    }

    public void walkInSearchRoom(Boolean isWalkIn) {
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        Scanner scanner = new Scanner(System.in);
        int numOfRoomsRequested = 0;
        Date checkInDate;
        Date checkOutDate;
        String input;

        System.out.println("*** HoRS :: Front Office :: Walk In Search/Reserve Room ***\n");

        try {
            System.out.print("Enter Check In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

            List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();

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
                    roomTypePricesForDuration[seq - 1] = roomTypeSessionBeanRemote.calculatePrice(roomType.getRoomTypeId(), checkInDate, checkOutDate, isWalkIn);
                    numOfRoomsAvailable[seq - 1] = roomTypeSessionBeanRemote.calculateNumOfRoomsAvailable(roomType.getRoomTypeId(), checkInDate, checkOutDate);
                } catch (RoomTypeNotFoundException ex) {
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
                System.out.print("Enter number of rooms to reserve> ");
                numOfRoomsRequested = scanner.nextInt();
                scanner.nextLine();
            }

            Occupant occupant = new Occupant();
            System.out.print("Enter Occupant First Name> ");
            input = scanner.nextLine().trim();
            occupant.setFirstName(input);
            
            System.out.print("Enter Occupant Last Name> ");
            input = scanner.nextLine().trim();
            occupant.setLastName(input);
            
            System.out.print("Enter Occupant Email Address> ");
            input = scanner.nextLine().trim();
            occupant.setOccupantEmail(input);
            
            Long occupantId = guestSessionBeanRemoteRemote.createNewOccupant(occupant);
            int totalAmount = roomTypePricesForDuration[response - 1] * numOfRoomsRequested;
            System.out.printf("Confirm Reservation for %d of %s at $%d? (Enter 'Y' to complete checkout)> ", numOfRoomsRequested, roomNames[response - 1], totalAmount);

            String confirmCheckout = scanner.nextLine().trim();

            if (confirmCheckout.equals("Y")) {
                Reservation newReservation = new Reservation(totalAmount, checkInDate, checkOutDate, numOfRoomsRequested, false);
                Long reservationId = reservationSessionBeanRemote.createReservationForOccupant(newReservation, occupantId, roomTypes.get(response - 1).getRoomTypeId());
                System.out.println("Reservation successful!\n");
            } else {
                System.out.println("Reservation cancelled!\n");
            }

            return;
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    private void checkInGuest(){
        
        System.out.println("*** HoRS :: Front Office :: Check-in Guest ***\n");
        
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the following to check-in (1. Guest - online reservation 2. Guest - walk-in reservation 3. Partner reservation)> ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("1")) {
                System.out.print("Enter Guest ID> ");
                String id = scanner.nextLine().trim();
                Long guestId = Long.parseLong(id);
                
                reservationSessionBeanRemote.allocateRoomToCurrentDayReservations(new Date());
                
                List<Reservation> guestReservationsToday = reservationSessionBeanRemote.retrieveReservationByGuestId(guestId);
                
                System.out.println("Guest " + id + " checked in successfully to the following rooms: ");
                for (Reservation reservation : guestReservationsToday) {
                    List<Room> rooms = reservation.getRooms();
                    
                    if (!rooms.isEmpty()) {
                        for (Room room : rooms) {
                            System.out.println("Room Number: " + room.getRoomNumber());
                            room.setRoomAvailability(RoomStatusEnum.NOT_AVAILABLE);
                            roomSessionBeanRemote.updateRoom(room);
                        }
                    } else {
                        System.out.println("No room allocated! View exception report for more details");
                    }

                }
                
                
            } else if(input.equals("2")) {
                System.out.print("Enter Walk-in Guest ID> ");
                String id = scanner.nextLine().trim();
                Long occupantId = Long.parseLong(id);
                
                reservationSessionBeanRemote.allocateRoomToCurrentDayReservations(new Date());
                
                List<Reservation> guestReservationsToday = reservationSessionBeanRemote.retrieveReservationByOccupantId(occupantId);
                
                System.out.println("Walk-in Guest " + id + " checked in successfully to the following rooms: ");
                for (Reservation reservation : guestReservationsToday) {
                    List<Room> rooms = reservation.getRooms();
                    
                    if (!rooms.isEmpty()) {
                        
                        for (Room room : rooms) {
                            System.out.println("Room Number: " + room.getRoomNumber());
                            room.setRoomAvailability(RoomStatusEnum.NOT_AVAILABLE);
                            roomSessionBeanRemote.updateRoom(room);
                        }
                    } else {
                        System.out.println("No room allocated! View exception report for more details");
                    }
                   
                }
                
            } else if (input.equals("3")) {
                
                System.out.print("Enter Partner ID> ");
                String id = scanner.nextLine().trim();
                Long partnerId = Long.parseLong(id);
                
                reservationSessionBeanRemote.allocateRoomToCurrentDayReservations(new Date());
                
                List<Reservation> guestReservationsToday = reservationSessionBeanRemote.retrieveReservationByPartnerId(partnerId);
                
                System.out.println("Partner " + id + " checked in successfully to the following rooms: ");
                for (Reservation reservation : guestReservationsToday) {
                    List<Room> rooms = reservation.getRooms();
                    
                    if (!rooms.isEmpty()) {
                        for (Room room : rooms) {
                            System.out.println("Room Number: " + room.getRoomNumber());
                            room.setRoomAvailability(RoomStatusEnum.NOT_AVAILABLE);
                            roomSessionBeanRemote.updateRoom(room);
                        }
                    } else {
                        System.out.println("No room allocated! View exception report for more details");
                    }
                   
                }
                
            } else {
                System.out.println("Invalid input!");
            }
            
        } catch(ReservationNotFoundException ex) {
            ex.printStackTrace();
        } catch(RoomNotFoundException ex) {
            ex.printStackTrace();
        }
    
        
    
        
    }

    private void checkOutGuest() {
        
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the following to check-out (1. Guest - online reservation 2. Guest - walk-in reservation 3. Partner reservation)> ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("1")) {
                System.out.print("Enter Guest ID> ");
                String id = scanner.nextLine().trim();
                Long guestId = Long.parseLong(id);
                
                List<Reservation> guestReservationsToday = reservationSessionBeanRemote.retrieveReservationByGuestId(guestId);
                
                System.out.println("Guest " + id + " checked out successfully out of the following rooms: ");
                for (Reservation reservation : guestReservationsToday) {
                    List<Room> rooms = reservation.getRooms();
                    
                    for (Room room : rooms) {
                        System.out.println("Room Number: " + room.getRoomNumber());
                        room.setRoomAvailability(RoomStatusEnum.AVAILABLE);
                        roomSessionBeanRemote.updateRoom(room);
                    }
                }
                
                System.out.println("Guest " + id + " checked out successfully!");
                
            } else if (input.equals("2")) {
                System.out.print("Enter Walk-in Guest ID> ");
                String id = scanner.nextLine().trim();
                Long occupantId = Long.parseLong(id);
                
                List<Reservation> guestReservationsToday = reservationSessionBeanRemote.retrieveReservationByOccupantId(occupantId);
                
                System.out.println("Walk-in Guest " + id + " checked out successfully out of the following rooms: ");
                for (Reservation reservation : guestReservationsToday) {
                    List<Room> rooms = reservation.getRooms();
                    
                    for (Room room : rooms) {
                        System.out.println("Room Number: " + room.getRoomNumber());
                        room.setRoomAvailability(RoomStatusEnum.AVAILABLE);
                        roomSessionBeanRemote.updateRoom(room);
                    }
                }
                
                System.out.println("Walk-in Guest " + id + " checked out successfully!");
            } else if (input.equals("3")) {
                System.out.print("Enter Partner ID> ");
                String id = scanner.nextLine().trim();
                Long partnerId = Long.parseLong(id);
                
                List<Reservation> guestReservationsToday = reservationSessionBeanRemote.retrieveReservationByPartnerId(partnerId);
                
                System.out.println("Partner " + id + " checked out successfully out of the following rooms: ");
                for (Reservation reservation : guestReservationsToday) {
                    List<Room> rooms = reservation.getRooms();
                    
                    for (Room room : rooms) {
                        System.out.println("Room Number: " + room.getRoomNumber());
                        room.setRoomAvailability(RoomStatusEnum.AVAILABLE);
                        roomSessionBeanRemote.updateRoom(room);
                    }
                }
                
                System.out.println("Partner " + id + " checked out successfully!");
            } else {
                System.out.println("Invalid input!");
            }
            
        } catch(ReservationNotFoundException ex) {
            ex.printStackTrace();
        } catch (RoomNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
