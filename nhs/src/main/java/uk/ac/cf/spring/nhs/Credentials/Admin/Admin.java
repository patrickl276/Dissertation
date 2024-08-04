package uk.ac.cf.spring.nhs.Credentials.Admin;

import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.*;

@Entity
@DynamicUpdate
@Table (name = "Admin")
public class Admin {

    @Id
    @Column(name = "AdminName")
    private String adminName;

    @Column(name = "AdminPassword")
    private String adminPassword;

    //Getters and setters
    public String getAdminName(){
        return adminName;
    }
    public void setAdminName(String name){
        this.adminName = name;
    }
    public String getAdminPassword(){
        return adminPassword;
    }
    public void setAdminPassword(String pass){
        this.adminName = pass;
    }

}
