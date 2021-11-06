/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.StaffSessionBeanRemote;
import entity.Staff;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author raihan
 */
public class SystemAdministrationModule {
    
    private StaffSessionBeanRemote staffSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private Staff currentStaff;
    
    public SystemAdministrationModule() {
    }
    
    public SystemAdministrationModule(StaffSessionBeanRemote staffSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, Staff currentStaff) {
        this.staffSessionBeanRemote = staffSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.currentStaff = currentStaff;
    }
    
     public void menuSystemAdministration() throws InvalidAccessRightException {
         
        if (currentStaff.getAccessRightEnum() != AccessRightEnum.SYSTEM_ADMINISTRATOR) {
            throw new InvalidAccessRightException("You don't have SYSTEM ADMINISTRATOR rights to access the system administration module.");
        }
        
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("-----------------------");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partner");
            System.out.println("-----------------------");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {
                    createEmployee();
                }
                else if(response == 2) {
                    viewAllEmployees();
                }
                else if(response == 3)
                {
                    createPartner();
                }
                else if(response == 4)
                {
                    viewAllPartners();
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
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

    }
    
    public void viewAllEmployees() {

    }
    
    public void createPartner() {

    }
    
    public void viewAllPartners() {

    }
    
}
