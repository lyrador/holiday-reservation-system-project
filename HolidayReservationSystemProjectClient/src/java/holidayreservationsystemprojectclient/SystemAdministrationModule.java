/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Partner;
import java.util.List;
import util.exception.EmployeeUsernameExistException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author raihan
 */
public class SystemAdministrationModule {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private Employee currentEmployee;
    
    public SystemAdministrationModule() {
    }
    
    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, Employee currentEmployee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
     public void menuSystemAdministration() throws InvalidAccessRightException {
         
        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.SYSTEM_ADMINISTRATOR) {
            throw new InvalidAccessRightException("You don't have SYSTEM ADMINISTRATOR rights to access the system administration module.");
        }
        
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** HoRS :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("-----------------------");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partner");
            System.out.println("-----------------------");
            System.out.println("5: Back\n");
            response = 0;
            
            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    
                    createEmployee();
                    
                } else if(response == 2) {
                    
                    viewAllEmployees();
                    
                } else if(response == 3) {
                    
                    createPartner();
                    
                } else if(response == 4) {
                    
                    viewAllPartners();
                    
                } else if (response == 5) {
                    
                    break;
                    
                } else {
                    
                    System.out.println("Invalid option, please try again!\n");  
                    
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
         
    }
         
    public void createEmployee() {
        Scanner scanner = new Scanner(System.in);
        Employee newEmployee = new Employee();
        
        System.out.println("*** POS System :: System Administration :: Create New Employee ***\n");
        System.out.print("Enter First Name> ");
        newEmployee.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployee.setLastName(scanner.nextLine().trim());
        
        while(true) {
            System.out.print("Select Access Right (1: Employee, 2: Manager, 3: System Administrator, 4: Operations Manager, 5: Sales Manager, 6: Guest Relations Officer)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 6) {
                newEmployee.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newEmployee.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newEmployee.setPassword(scanner.nextLine().trim());
        
        try {
            Long newEmployeeId = employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("New employee created successfully!: " + newEmployeeId + "\n");
        }
        catch(EmployeeUsernameExistException ex) {
            System.out.println("An error has occurred while creating the new employee!: The user name already exist\n");
        }
        catch(UnknownPersistenceException ex) {
            System.out.println("An unknown error has occurred while creating the new employee!: " + ex.getMessage() + "\n");
        }
    }
    
    public void viewAllEmployees() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Employees ***\n");
        List<Employee> employees = employeeSessionBeanRemote.viewAllEmployees();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for(Employee employee:employees) {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRightEnum().toString(), employee.getUsername(), employee.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    public void createPartner() {
        Scanner scanner = new Scanner(System.in);
        Partner newPartner = new Partner();
        
        System.out.println("*** POS System :: System Administration :: Create New Partner ***\n");
        System.out.print("Enter Partner Name> ");
        newPartner.setPartnerName(scanner.nextLine().trim());
       
        System.out.print("Enter Username> ");
        newPartner.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newPartner.setPassword(scanner.nextLine().trim());
        
        try {
            Long newPartnerId = partnerSessionBeanRemote.createNewPartner(newPartner);
            System.out.println("New partner created successfully!: " + newPartnerId + "\n");
        }
        catch(PartnerUsernameExistException ex) {
            System.out.println("An error has occurred while creating the new partner!: The partner username already exist\n");
        }
        catch(UnknownPersistenceException ex) {
            System.out.println("An unknown error has occurred while creating the new partner!: " + ex.getMessage() + "\n");
        }
    }
    
    public void viewAllPartners() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Partners ***\n");
        List<Partner> partners = partnerSessionBeanRemote.viewAllPartners();
        System.out.printf("%8s%20s%20s%20s\n", "Partner ID", "Partner Name", "Username", "Password");

        for(Partner partner:partners) {
            System.out.printf("%8s%20s%20s%20s\n", partner.getPartnerId().toString(), partner.getPartnerName(), partner.getUsername(), partner.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
}
