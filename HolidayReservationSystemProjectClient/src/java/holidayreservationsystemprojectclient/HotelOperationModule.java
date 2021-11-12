/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import entity.Employee;
import entity.ExceptionReport;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.Schedule;
import util.enumeration.AccessRightEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.DeleteRoomException;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author raihan
 */
public class HotelOperationModule {
    
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private Employee currentEmployee;

    public HotelOperationModule() {
    }
    
    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, Employee currentEmployee) {
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuHotelOperation() throws InvalidAccessRightException {
        
        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.OPERATIONS_MANAGER && currentEmployee.getAccessRightEnum() != AccessRightEnum.SALES_MANAGER) {
            throw new InvalidAccessRightException("You don't have MANAGER rights to access the hotel operations module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS :: Hotel Operation ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View/Update/Delete Room Type Details");
            System.out.println("3: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("4: Create New Room");
            System.out.println("5: Update Room");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("8: View Room Allocation Exception Report");
            System.out.println("-----------------------");
            System.out.println("9: Create New Room Rate");
            System.out.println("10: View/Update/Delete Room Rate Details");
            System.out.println("11: View All Room Rates");
            System.out.println("-----------------------");
            System.out.println("12: Allocate Room");
            System.out.println("-----------------------");
            System.out.println("13: Back\n");
            response = 0;
            
            while(response < 1 || response > 13) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1) {
                    
                    createRoomType();
                    
                } else if(response == 2) {
                    
                    viewRoomTypeDetails();
                    
                } else if(response == 3) {
                    
                    viewAllRoomTypes();
                    
                } else if(response == 4) {

                    createRoom();
                    
                } else if(response == 5) {
                    
                    updateRoom();
                    
                } else if(response == 6) {
                    
                    deleteRoom();
                    
                } else if(response == 7) {
                    
                    viewAllRooms();
                    
                } else if(response == 8) {
                    
                    viewRoomAllocationExceptionReport();
                    
                } else if(response == 9) {
                    
                    createRoomRate();
                    
                } else if(response == 10) {
                    
                    viewRoomRateDetails();
                    
                } else if(response == 11) {
                    
                    viewAllRoomRates();
                    
                } else if(response == 12) {
                    
                    allocateRoom();
                    
                } else if(response == 13) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 13) {
                break;
            }
        }
    }

    private void createRoomType() {
        Scanner scanner = new Scanner(System.in);
        RoomType newRoomType = new RoomType();

        System.out.println("*** HoRS :: Hotel Operations :: Create New Room Type ***\n");
        
        System.out.print("Enter Name> ");
        newRoomType.setRoomName(scanner.nextLine().trim());
        
        System.out.print("Enter Description> ");
        newRoomType.setRoomDescription(scanner.nextLine().trim());
        
        System.out.print("Enter Room Size (square meters)> ");
        newRoomType.setRoomSize(scanner.nextInt());
        scanner.nextLine();
        
        System.out.print("Enter Bed> ");
        newRoomType.setRoomBed(scanner.nextLine().trim());
        
        System.out.print("Enter Capacity> ");
        newRoomType.setRoomCapacity(scanner.nextInt());
        scanner.nextLine();
        
        System.out.print("Enter Amenities> ");
        newRoomType.setRoomAmenities(scanner.nextLine().trim());
        
        System.out.print("Enter Room Rank> ");
        newRoomType.setRoomRank(scanner.nextInt());
        scanner.nextLine();
        
        newRoomType = roomTypeSessionBeanRemote.createRoomType(newRoomType);
        System.out.println("New room type created successfully!: " + newRoomType.getRoomTypeId()+ "\n");
        System.out.print("Press any key to continue...> ");
    }

    private void viewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** HoRS :: Hotel Operations :: View Room Type Details ***\n");
        
        System.out.print("Enter Room Type Name> ");
        String name = scanner.nextLine().trim();
        
        try {
            RoomType roomType = roomTypeSessionBeanRemote.retrieveRoomTypeByName(name); 
            
            System.out.println("\n" + "Room Type name: " + roomType.getRoomName());
            System.out.println("Room Type description: " + roomType.getRoomDescription());
            System.out.println("Room Type size: " + roomType.getRoomSize());
            System.out.println("Room Type bed: " + roomType.getRoomBed());
            System.out.println("Room Type capacity: " + roomType.getRoomCapacity());
            System.out.println("Room Type amenities: " + roomType.getRoomAmenities());
            System.out.println("Room Type rank: " + roomType.getRoomRank());
            System.out.println("------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1) {
                updateRoomType(roomType);
            } else if(response == 2) {
                deleteRoomType(roomType);
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving Room Type details: " + ex.getMessage() + "\n");
            System.out.print("Press any key to continue...> ");
        }
    }
   
    private void updateRoomType(RoomType roomType) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** HoRS :: Hotel Operations :: Update Room Type ***\n");
        System.out.print("Enter Room Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        if (input.length() > 0) {
            roomType.setRoomName(input);
        }
        
        System.out.print("Enter Room Description (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        if (input.length() > 0) {
            roomType.setRoomDescription(input);
        }
        
        System.out.print("Enter Room Size - in square meters (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        if (input.length() > 0) {
            
            while(true) {
                try {
                    roomType.setRoomSize(Integer.parseInt(input));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
                }
            }
            
        }
        
        System.out.print("Enter Room Bed (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        if (input.length() > 0) {
            roomType.setRoomBed(input);
        }
        
        System.out.print("Enter Room Capacity (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        if (input.length() > 0) {
            
            while(true) {
                try {
                    roomType.setRoomCapacity(Integer.parseInt(input));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
                }
            }
            
        }
        
        System.out.print("Enter Room Amenities (blank if no change)> ");
        input = scanner.nextLine().trim();
        
        if (input.length() > 0) {
            roomType.setRoomAmenities(input);
        }
      
        try {
            roomTypeSessionBeanRemote.updateRoomType(roomType);
            System.out.println("Room Type updated successfully!\n");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while updating room type: " + ex.getMessage() + "\n");
        }
    }

    private void deleteRoomType(RoomType roomType) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** HoRS :: Hotel Operations :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomType.getRoomName(), roomType.getRoomTypeId());
        input = scanner.nextLine().trim();
        
        if (input.equals("Y")) {
            try {
                roomTypeSessionBeanRemote.deleteRoomType(roomType.getRoomTypeId());
                System.out.println("Room Type deleted successfully!\n");
            }
            catch (RoomTypeNotFoundException | DeleteRoomTypeException ex) 
            {
                System.out.println("An error has occurred while deleting Room Type: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room Type not deleted!\n");
        }
    }

    private void viewAllRoomTypes() {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** HoRS :: Hotel Operations :: View All Room Types ***\n");
        System.out.printf("%11s%30s%30s%10s%25s%10s%30s\n", "RoomType Id", "Name", "Description", "Size", "Bed", "Capacity", "Amenities");
        List<RoomType> allRoomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (RoomType roomType : allRoomTypes) {
            System.out.printf("%11s%30s%30s%10s%25s%10s%30s\n", roomType.getRoomTypeId().toString(), roomType.getRoomName(), roomType.getRoomDescription(), roomType.getRoomSize().toString(), roomType.getRoomBed(), roomType.getRoomCapacity().toString(), roomType.getRoomAmenities());    
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void createRoom() {
        
         try {
            Scanner scanner = new Scanner(System.in);
            Room newRoom = new Room();
            
            System.out.println("*** HoRS :: Hotel Operations :: Create New Room ***\n");
            
            System.out.print("Enter Room Type Id> ");
            Long roomTypeId = scanner.nextLong();
            scanner.nextLine();
            RoomType roomType = roomTypeSessionBeanRemote.retrieveRoomTypeByRoomId(roomTypeId);
            newRoom.setRoomType(roomType);
            
            System.out.print("Enter Room Number (Eg.2015)> ");
            newRoom.setRoomNumber(scanner.nextLine().trim());
            
            
            newRoom.setRoomAvailability(RoomStatusEnum.AVAILABLE);
            
            newRoom = roomSessionBeanRemote.createRoom(newRoom);
            System.out.println("New room " + newRoom.getRoomNumber() + " created successfully!: \n");
            
        }  catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "!\n");
        } 
        
    }

    private void updateRoom() { //might have issue
        
        try {
            Scanner scanner = new Scanner(System.in);
            String input;
            
            System.out.println("*** HoRS :: Hotel Operations :: Update Room ***\n");
            System.out.print("Enter Room Number> ");
            String roomNumber = scanner.nextLine().trim();
            
            Room room = roomSessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);
            System.out.println("Select room type (blank if no change)>");
            
            Integer counter = 1;
            for (RoomType roomType : roomTypeSessionBeanRemote.viewAllRoomTypes()) {
                System.out.println("(" + counter + ") " + roomType.getRoomName());
                counter++;
            }
            
            String s = scanner.nextLine().trim();
            int option = 0;
            if (s.length() > 0) {
                option = Integer.parseInt(s);
                room.setRoomType(roomTypeSessionBeanRemote.viewAllRoomTypes().get(option-1));
            }
            
            while(true) {
                System.out.print("Choose Room Status (1. Available, 2. Not available)> ");
                Integer statusOption = scanner.nextInt();
                
                    
                if(statusOption == 1) {
                    room.setRoomAvailability(RoomStatusEnum.AVAILABLE);
                    System.out.println("Room status set to availalbe!\n");
                    break;
                } else if (statusOption == 2){ 
                    room.setRoomAvailability(RoomStatusEnum.NOT_AVAILABLE);
                    System.out.println("Room status set to not available!\n");
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            roomSessionBeanRemote.updateRoom(room);
            
        } catch (RoomNotFoundException ex) {
                System.out.println("An error has occurred while updating room : " + ex.getMessage() + "\n");
        }
    }

    private void deleteRoom() {
        
         try {
            Scanner scanner = new Scanner(System.in);
            String input;
            
            System.out.println("*** HoRS :: Hotel Operations :: Delete Room ***\n");
            System.out.print("Enter Room Number> ");
            String roomNumber = scanner.nextLine().trim();
            
            Room room = roomSessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);
            
            System.out.printf("Confirm Delete Room Number %s (Enter 'Y' to Delete)> ", room.getRoomNumber());
            input = scanner.nextLine().trim();
            
            if(input.equals("Y")) {
                roomSessionBeanRemote.deleteRoom(room.getRoomId());
                System.out.println("Room deleted successfully!\n");
            } else  {
                System.out.println("Room not deleted!\n");
            }
        } catch (RoomNotFoundException | DeleteRoomException ex) {
            System.out.println("An error has occurred while deleting Room : " + ex.getMessage() + "\n");
        }
    }

    private void viewAllRooms() {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** HoRS :: Hotel Operations :: View All Rooms ***\n");
        
        List<Room> rooms = roomSessionBeanRemote.viewAllRooms();
        System.out.printf("%10s%14s%20s%30s\n", "Room ID", "Room Number", "Room Status", "Room Type");
        System.out.println("--------------------------------------------------------------------------------------");
        
        for (Room room : rooms) { 
             System.out.printf("%10s%14s%20s%30s\n", room.getRoomId().toString(), room.getRoomNumber().toString(), room.getRoomAvailability().toString(),room.getRoomType().getRoomName());
        }
        System.out.println("--------------------------------------------------------------------------------------");

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void viewRoomAllocationExceptionReport() {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** HoRS :: Hotel Management System :: View Room Allocation Exception Report ***\n");
        
        List<ExceptionReport> exReports = roomSessionBeanRemote.generateRoomAllocationExceptionReport();
        System.out.printf("%20s%100s\n", "Exception Report Id", "Description");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for(ExceptionReport exReport:exReports)
        {
            System.out.printf("%20s%150s\n", exReport.getExceptionReportId().toString(), exReport.getDescription());
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void createRoomRate() {
        
        try {
            
            Scanner scanner = new Scanner(System.in);
            RoomRate newRoomRate = new RoomRate();
            
            System.out.println("*** HoRS :: Hotel Operations :: Create New Room Rate ***\n");
            
            System.out.print("Enter Room Rate Name> ");
            newRoomRate.setName(scanner.nextLine());
            
            System.out.print("Enter Room Type Id> ");
            Long roomTypeId = scanner.nextLong();
            RoomType roomType = roomTypeSessionBeanRemote.retrieveRoomTypeByRoomId(roomTypeId);
            newRoomRate.setRoomType(roomType);
            
            while (true) {
                
                System.out.print("Select Room Rate Type (1. Normal Rate, 2. Published Rate, 3. Promotion Rate, 4. Peak Rate)> ");
                Integer roomRateTypeInt = scanner.nextInt();
                scanner.nextLine();
                
                if (roomRateTypeInt >= 1 && roomRateTypeInt <= 4) {
                    
                    if (roomRateTypeInt == 1) {
                        newRoomRate.setRateType(RateTypeEnum.NORMAL);
                        break;
                    } else if(roomRateTypeInt == 2) {
                        newRoomRate.setRateType(RateTypeEnum.PUBLISHED);
                        break;
                    } else if(roomRateTypeInt == 3) {
                        newRoomRate.setRateType(RateTypeEnum.PROMOTION);
                        break;
                    } else if(roomRateTypeInt == 4) {
                        newRoomRate.setRateType(RateTypeEnum.PEAK);
                        break;
                    } else {
                        System.out.println("Room rate type not available!\n");
                    }
                    
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            System.out.print("Enter Room Rate Per Night> ");
            Integer roomRatePerNight = scanner.nextInt();
            newRoomRate.setRatePerNight(roomRatePerNight);
            scanner.nextLine();
            
            Date date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            while (true) {
                
                if (newRoomRate.getRateType().equals(RateTypeEnum.PEAK)) {
                    
                    try {
                        System.out.print("Enter Peak Rate Start Date (yyyy-MM-dd)> ");
                        date = sdf.parse(scanner.nextLine());
                        newRoomRate.setValidityStartDate(date);
                    } catch (ParseException ex) {
                        System.out.println("Invalid start date!");
                    }
                    
                    try {
                        System.out.print("Enter Peak Rate End Date (yyyy-MM-dd)> ");
                        date = sdf.parse(scanner.nextLine());
                        newRoomRate.setValidityEndDate(date);
                    } catch (ParseException ex) {
                        System.out.println("Invalid end date!");
                    }
                    
                    break;
                
                } else if (newRoomRate.getRateType().equals(RateTypeEnum.PROMOTION)) {
                    
                    try {
                        System.out.print("Enter Promotion Rate Start Date (yyyy-MM-dd)> ");
                        date = sdf.parse(scanner.nextLine());
                        newRoomRate.setValidityStartDate(date);
                    } catch (ParseException ex) {
                        System.out.println("'Invalid start date!");
                    }
                    
                    try {
                        System.out.print("Enter Promotion Rate End Date (yyyy-MM-dd)> ");
                        date = sdf.parse(scanner.nextLine());
                        newRoomRate.setValidityEndDate(date);
                    } catch (ParseException ex) {
                        System.out.println("'Invalid end date!");
                    }
                    
                    break;
                    
                } else {
                    break;
                }
                
            }

            System.out.println("Room rate + " + newRoomRate.getName() + " for Room Type: " + roomType.getRoomName() + "created! \n");

            newRoomRate = roomRateSessionBeanRemote.createRoomRate(newRoomRate,roomType.getRoomTypeId());
            System.out.println("New room rate created successfully!: " + newRoomRate.getRoomRateId()+ "\n");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "!\n");
        } 
        
    }

    private void viewRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** HoRS :: Hotel Operations :: View Room Rate Details ***\n");
        
        System.out.print("Enter Room Rate Id> ");
        Long roomRateId = scanner.nextLong();
        
        try {
            RoomRate roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId); 
            
            System.out.println("\n" + "Room Rate Name: " + roomRate.getName());
            System.out.println("Room Rate - Room Type: " + roomRate.getRoomType().getRoomName());
            System.out.println("Room Rate - Rate Type: " + roomRate.getRateType().toString());
            System.out.println("Room Rate - Rate Per Night: " + roomRate.getRatePerNight());
            System.out.println("Room Rate Validity Start Date: " + roomRate.getValidityStartDate().toString());
            System.out.println("Room Rate Validity End Date: " + roomRate.getValidityEndDate().toString());
            System.out.println("------------------------");
            System.out.println("1: Update Room Rate");
            System.out.println("2: Delete Room Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1) {
                updateRoomRate(roomRate);
            } else if(response == 2) {
                deleteRoomRate(roomRate);
            }
        } catch (RoomRateNotFoundException ex) {
            System.out.println("An error has occurred while retrieving Room Rate details: " + ex.getMessage() + "\n");
            System.out.print("Press any key to continue...> ");
        }
    }
    
    private void updateRoomRate(RoomRate roomRate) {
        
         try {
             
            Scanner scanner = new Scanner(System.in);        
            String input;

            System.out.println("*** HoRS :: Hotel Operations :: Update Room Rate ***\n");
            
            System.out.print("Enter Room Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            
            if (input.length() > 0) {
                roomRate.setName(input);
            }
            
            System.out.print("Select Room Type (blank if no change):> ");
            List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
            
            Integer counter = 1;
            for (RoomType roomType : roomTypes) {
                System.out.println("(" + counter + ") " + roomType.getRoomName());
                counter++;
            }
            
            input = scanner.nextLine();
            if (input.length() > 0) {
                roomRate.setRoomType(roomTypes.get(Integer.parseInt(input)-1));
            }
            
            while (true) {
                System.out.print("Select Room Type (1. Published 2. Normal 3. Peak 4. Promotion) (blank if no change):> ");
                input = scanner.nextLine();

                if (input.length() > 0) {
                    Integer option = Integer.parseInt(input);

                    if (option == 1) {
                        roomRate.setRateType(RateTypeEnum.PUBLISHED);
                        break;
                    } else if (option == 2) {
                        roomRate.setRateType(RateTypeEnum.NORMAL);
                        break;
                    } else if (option == 3) {
                        roomRate.setRateType(RateTypeEnum.PEAK);
                        break;
                    } else if (option == 4) {
                        roomRate.setRateType(RateTypeEnum.PROMOTION);
                        break;
                    } else {
                        System.out.println("Invalid option. Try again!");
                    }
                }
            }
            
            System.out.print("Enter Room Rate Per Night (blank if no change)> ");
            input = scanner.nextLine().trim();
            
            if (input.length() > 0) {
                roomRate.setRatePerNight(Integer.parseInt(input));
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            System.out.print("Enter Room Rate Start Date (yyyy-MM-dd) (blank if no change)> ");
            input = scanner.nextLine().trim();
            
            if (input.length() > 0) {
                roomRate.setValidityStartDate(sdf.parse(input));
            }
            
            System.out.print("Enter Room Rate End Date (yyyy-MM-dd) (blank if no change)> ");
            input = scanner.nextLine().trim();
            
            if (input.length() > 0) {
                roomRate.setValidityEndDate(sdf.parse(input));
            }
            
            roomRateSessionBeanRemote.updateRoomRate(roomRate);
            System.out.println("Room Rate updated successfully!\n");
            
        } catch (ParseException ex) {
            System.out.println("Invalid Date Format entered!" + "\n");
        } catch (RoomRateNotFoundException ex) {
            System.out.println("An error has occurred while updating Room Rate: " + ex.getMessage() + "\n");
        }
    }

    private void deleteRoomRate(RoomRate roomRate) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** HoRS :: Hotel Operations :: Delete Room Rate ***\n");
        System.out.printf("Confirm Delete Room Rate %s (Room Rate ID: %d) (Enter 'Y' to Delete)> ", roomRate.getName(), roomRate.getRoomRateId());
        input = scanner.nextLine().trim();
        
        if (input.equals("Y")) {
            
            try {
                roomRateSessionBeanRemote.deleteRoomRate(roomRate.getRoomRateId());
                System.out.println("Room Rate deleted successfully!\n");
            } catch (RoomRateNotFoundException ex) {
                System.out.println("An error has occurred while deleting Room Rate: " + ex.getMessage() + "\n");
            } 
        }
        else {
            System.out.println("Room Rate not deleted!\n");
        }
    }

    private void viewAllRoomRates() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HoRS :: Hotel Operations :: View All Room Rates ***\n");
        
        List<RoomRate> roomRates = roomRateSessionBeanRemote.viewAllRoomRates();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.printf("%12s%30s%30s%18s%20s%18s%18s\n", "RoomRate Id", "Name", "Room Type", "Rate Type", "Rate Per Night", "Start Date", "End Date");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        for (RoomRate roomRate : roomRates) {         
            if (roomRate.getRateType().equals(RateTypeEnum.PEAK) || roomRate.getRateType().equals(RateTypeEnum.PROMOTION)) {
                System.out.printf("%12s%30s%30s%18s%20s%18s%18s\n", 
                        roomRate.getRoomRateId().toString(), 
                        roomRate.getName(), 
                        roomRate.getRoomType().getRoomName(), 
                        roomRate.getRateType().toString(), 
                        roomRate.getRatePerNight().toString(), 
                        formatter.format(roomRate.getValidityStartDate()), 
                        formatter.format(roomRate.getValidityEndDate()));
            } else {
                System.out.printf("%12s%30s%30s%18s%20s%18s%18s\n", 
                        roomRate.getRoomRateId().toString(), 
                        roomRate.getName(), 
                        roomRate.getRoomType().getRoomName(), 
                        roomRate.getRateType().toString(), 
                        roomRate.getRatePerNight().toString(), 
                        "NIL", "NIL");
            }
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void allocateRoom() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HoRS :: Hotel Operations :: Allocate Room ***\n");
        
        System.out.print("Allocate rooms to current day reservations? (Enter 'Y' to confirm, 'N' to allocate for a specific date)> \n");
        String input = scanner.nextLine().trim();
        Date date = new Date();
        
        while (true) {
            if (input.equals("Y")) {
                List<Room> roomsReserved = reservationSessionBeanRemote.allocateRoomToCurrentDayReservations(date);
                if(!roomsReserved.isEmpty()){
                    System.out.println("The following rooms have been allocated: ");
                    for(Room room : roomsReserved){
                        System.out.println("Room Number: " + room.getRoomNumber()); 
                    }
                }
                else {
                    System.out.println("No rooms available for allocation, refer to exception reports for more information.");
                }
                break;
            } else if (input.equals("N")) {
                
                try {
                    System.out.print("Enter a specific future date (DD/MM/YYY)> \n");

                    String dateInput = scanner.nextLine().trim();
                    Date futureDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInput); 
                    
                    List<Room> roomsReserved = reservationSessionBeanRemote.allocateRoomToCurrentDayReservations(futureDate);
                    if(!roomsReserved.isEmpty()){
                        System.out.println("The following rooms have been allocated: ");
                        for(Room room : roomsReserved){
                            System.out.println("Room Number: " + room.getRoomNumber()); 
                        }
                    }
                    else {
                        System.out.println("No rooms available for allocation, refer to exception reports for more information.");
                    }
                    break;
                    
                    
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                
            }
        } 
        
        
        
       
        
    }

    

    
}
