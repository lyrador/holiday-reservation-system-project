package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessRightEnum accessRightEnum;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;

    public Employee() {
    }

    public Employee(String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;     
    }

    @Override
    public void copy(Object object) {
        if(object.getClass().equals(this.getClass())) {
            Employee employeeToCopy = (Employee)object;
            this.setEmployeeId(employeeToCopy.getEmployeeId());
            this.setFirstName(employeeToCopy.getFirstName());
            this.setLastName(employeeToCopy.getLastName());
            this.setAccessRightEnum(employeeToCopy.getAccessRightEnum());
            this.setUsername(employeeToCopy.getUsername());
            this.setPassword(employeeToCopy.getPassword());
        }
    }

    @Override
    public int hashCode() {
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
        return "entity.Employee[ id=" + employeeId + " ]";
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
}