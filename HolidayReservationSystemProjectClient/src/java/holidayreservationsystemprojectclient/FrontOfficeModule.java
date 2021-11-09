/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemprojectclient;

import java.util.Scanner;

/**
 *
 * @author raihan
 */
public class FrontOfficeModule {
    
      public void menuFrontOffice(){
        
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
                    
                    walkInSearchRoom();
                    
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

    private void walkInSearchRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkInGuest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkOutGuest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
