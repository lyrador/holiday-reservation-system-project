/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.RoomRateSessionBeanLocal;
import entity.Employee;
import entity.Guest;
import entity.Partner;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;

/**
 *
 * @author raihan
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

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
        
        if (em.find(RoomType.class, 1l) == null) {
            RoomType deluxe = new RoomType("Deluxe Room", "deluxe", 10, "double", 100, "table", 1);
            em.persist(deluxe);
            em.flush();

            RoomType premier = new RoomType("Premier Room", "premier", 10, "double", 100, "table", 2);
            em.persist(premier);
            em.flush();
            
            RoomType family = new RoomType("Family Room", "family", 10, "double", 100, "table", 3);
            em.persist(family);
            em.flush();
            
            RoomType junior = new RoomType("Junior Suite", "junior", 10, "double", 100, "table", 4);
            em.persist(junior);
            em.flush();
            
            RoomType grand = new RoomType("Grand Suite", "grand", 10, "double", 100, "table", 5);
            em.persist(grand);
            em.flush();
        }
        
        if (em.find(RoomRate.class, 1l) == null) {
            RoomType deluxe = em.find(RoomType.class, 1l);
            RoomType premier = em.find(RoomType.class, 2l);
            RoomType family = em.find(RoomType.class, 3l);
            RoomType junior = em.find(RoomType.class, 4l);
            RoomType grand = em.find(RoomType.class, 5l);
            
            RoomRate deluxeRoomPublished = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Deluxe Room Published", RateTypeEnum.PUBLISHED, 100), deluxe);
            em.persist(deluxeRoomPublished);
            em.flush();

            RoomRate deluxeRoomNormal = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Deluxe Room Normal", RateTypeEnum.NORMAL, 50), deluxe);
            em.persist(deluxeRoomNormal);
            em.flush();
            
            RoomRate premierRoomPublished = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Premier Room Published", RateTypeEnum.PUBLISHED, 200), premier);
            em.persist(premierRoomPublished);
            em.flush();

            RoomRate premierRoomNormal = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Premier Room Normal", RateTypeEnum.NORMAL, 100), premier);
            em.persist(premierRoomNormal);
            em.flush();
            
            RoomRate familyRoomPublished = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Family Room Published", RateTypeEnum.PUBLISHED, 300), family);
            em.persist(familyRoomPublished);
            em.flush();

            RoomRate familyRoomNormal = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Family Room Normal", RateTypeEnum.NORMAL, 150), family);
            em.persist(familyRoomNormal);
            em.flush();
            
            RoomRate juniorSuitePublished = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Junior Suite Published", RateTypeEnum.PUBLISHED, 400), junior);
            em.persist(juniorSuitePublished);
            em.flush();

            RoomRate juniorSuiteNormal = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Junior Suite Normal", RateTypeEnum.NORMAL, 200), junior);
            em.persist(juniorSuiteNormal);
            em.flush();
            
            RoomRate grandSuitePublished = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Grand Suite Published", RateTypeEnum.PUBLISHED, 500), grand);
            em.persist(grandSuitePublished);
            em.flush();

            RoomRate grandSuiteNormal = roomRateSessionBeanLocal.createRoomRate(new RoomRate("Grand Suite Normal", RateTypeEnum.NORMAL, 250), grand);
            em.persist(grandSuiteNormal);
            em.flush();
        }
        
        if (em.find(Room.class, 1l) == null) {
            RoomType deluxe = em.find(RoomType.class, 1l);
            RoomType premier = em.find(RoomType.class, 2l);
            RoomType family = em.find(RoomType.class, 3l);
            RoomType junior = em.find(RoomType.class, 4l);
            RoomType grand = em.find(RoomType.class, 5l);
            
            Room room0101 = new Room("0101", RoomStatusEnum.AVAILABLE, null, deluxe);
            em.persist(room0101);
            em.flush();
            
            Room room0201 = new Room("0201", RoomStatusEnum.AVAILABLE, null, deluxe);
            em.persist(room0201);
            em.flush();
            
            Room room0301 = new Room("0301", RoomStatusEnum.AVAILABLE, null, deluxe);
            em.persist(room0301);
            em.flush();
            
            Room room0401 = new Room("0401", RoomStatusEnum.AVAILABLE, null, deluxe);
            em.persist(room0401);
            em.flush();
            
            Room room0501 = new Room("0501", RoomStatusEnum.AVAILABLE, null, deluxe);
            em.persist(room0501);
            em.flush();
            
            Room room0102 = new Room("0102", RoomStatusEnum.AVAILABLE, null, premier);
            em.persist(room0102);
            em.flush();
            
            Room room0202 = new Room("0202", RoomStatusEnum.AVAILABLE, null, premier);
            em.persist(room0202);
            em.flush();
            
            Room room0302 = new Room("0302", RoomStatusEnum.AVAILABLE, null, premier);
            em.persist(room0302);
            em.flush();
            
            Room room0402 = new Room("0402", RoomStatusEnum.AVAILABLE, null, premier);
            em.persist(room0402);
            em.flush();
            
            Room room0502 = new Room("0502", RoomStatusEnum.AVAILABLE, null, premier);
            em.persist(room0502);
            em.flush();
            
            Room room0103 = new Room("0103", RoomStatusEnum.AVAILABLE, null, family);
            em.persist(room0103);
            em.flush();
            
            Room room0203 = new Room("0203", RoomStatusEnum.AVAILABLE, null, family);
            em.persist(room0203);
            em.flush();
            
            Room room0303 = new Room("0303", RoomStatusEnum.AVAILABLE, null, family);
            em.persist(room0303);
            em.flush();
            
            Room room0403 = new Room("0403", RoomStatusEnum.AVAILABLE, null, family);
            em.persist(room0403);
            em.flush();
            
            Room room0503 = new Room("0503", RoomStatusEnum.AVAILABLE, null, family);
            em.persist(room0503);
            em.flush();
            
            Room room0104 = new Room("0104", RoomStatusEnum.AVAILABLE, null, junior);
            em.persist(room0104);
            em.flush();
            
            Room room0204 = new Room("0204", RoomStatusEnum.AVAILABLE, null, junior);
            em.persist(room0204);
            em.flush();
            
            Room room0304 = new Room("0304", RoomStatusEnum.AVAILABLE, null, junior);
            em.persist(room0304);
            em.flush();
            
            Room room0404 = new Room("0404", RoomStatusEnum.AVAILABLE, null, junior);
            em.persist(room0404);
            em.flush();
            
            Room room0504 = new Room("0504", RoomStatusEnum.AVAILABLE, null, junior);
            em.persist(room0504);
            em.flush();
            
            Room room0105 = new Room("0105", RoomStatusEnum.AVAILABLE, null, grand);
            em.persist(room0105);
            em.flush();
            
            Room room0205 = new Room("0205", RoomStatusEnum.AVAILABLE, null, grand);
            em.persist(room0205);
            em.flush();
            
            Room room0305 = new Room("0305", RoomStatusEnum.AVAILABLE, null, grand);
            em.persist(room0305);
            em.flush();
            
            Room room0405 = new Room("0405", RoomStatusEnum.AVAILABLE, null, grand);
            em.persist(room0405);
            em.flush();
            
            Room room0505 = new Room("0505", RoomStatusEnum.AVAILABLE, null, grand);
            em.persist(room0505);
            em.flush();
            
        }
        
        //Delete this before the demo cos its our own data
        if (em.find(Guest.class, 1l) == null) {
            Guest guest1 = new Guest("guest", "one", "guest1@gmail.com", "password");
            em.persist(guest1);
            em.flush();
            
        }
        
        if (em.find(Partner.class, 1l) == null) {
            Partner partner1 = new Partner("partner1", "partner1", "password");
            em.persist(partner1);
            em.flush();
            
        }
    
    }
    
}
