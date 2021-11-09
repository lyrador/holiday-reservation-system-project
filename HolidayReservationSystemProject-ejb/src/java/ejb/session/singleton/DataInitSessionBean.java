/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author raihan
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "HolidayReservationSystemProject-ejbPU")
    private EntityManager em;
    
    

    @PostConstruct
    public void postConstruct() {
       
    //sysadmin, password, System Administrator
    //opmanager, password, Operation Manager
    //salesmanager, password, Sales Manager
    //guestrelo, password, Guest Relation Officer
    
        if (em.find(Employee.class, 1l) == null) {
            Employee sysadmin = new Employee("Sysadmin", "One", AccessRightEnum.SYSTEM_ADMINISTRATOR, "sysadmin", "password");
            em.persist(sysadmin);
            em.flush();

            Employee opmanager = new Employee("Opmanager", "One", AccessRightEnum.OPERATIONS_MANAGER, "opmanager", "password");
            em.persist(opmanager);
            em.flush();

            Employee salesmanager = new Employee("Salesmanager", "One", AccessRightEnum.SALES_MANAGER, "salesmanager", "password");
            em.persist(salesmanager);
            em.flush();

            Employee guestrelo = new Employee("Guesrelo", "One", AccessRightEnum.GUEST_RELATIONS_OFFICER, "guestrelo", "password");
            em.persist(guestrelo);
            em.flush();
        }
    
    }
    
}
