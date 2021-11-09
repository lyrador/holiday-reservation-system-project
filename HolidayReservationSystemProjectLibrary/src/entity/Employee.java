package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.AccessRightEnum;

import util.providedinterface.ICopyable;


@Entity
public class Employee implements Serializable, ICopyable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String firstName;
    private String lastName;
    private AccessRightEnum accessRightEnum;
    private String username;
    private String password;
    private List<Reservation> reservations;

    public Employee() {
        reservations = new ArrayList<>();
    }

    public Employee(String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password) 
    {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
    }

    public Employee(Long staffId, String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password)
    {
        this();
        
        this.employeeId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;     
    }

    
    public Employee(Long staffId, String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password, List<Reservation> reservations)
    {
        this();
        
        this.employeeId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
        this.reservations = reservations;
    }

    @Override
    public void copy(Object object) 
    {
        if(object.getClass().equals(this.getClass()))
        {
            Employee staffToCopy = (Employee)object;
            this.setEmployeeId(staffToCopy.getEmployeeId());
            this.setFirstName(staffToCopy.getFirstName());
            this.setLastName(staffToCopy.getLastName());
            this.setAccessRightEnum(staffToCopy.getAccessRightEnum());
            this.setUsername(staffToCopy.getUsername());
            this.setPassword(staffToCopy.getPassword());
            this.setReservations(staffToCopy.getReservations());
        }
    }

    @Override
    public int hashCode() 
    {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Staff[ id=" + employeeId + " ]";
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}