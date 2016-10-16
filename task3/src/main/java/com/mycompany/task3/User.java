package com.mycompany.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;
import org.glassfish.jersey.internal.util.Base64;

@XmlRootElement()
public class User
{
    public enum ROLE 
    {
        CREATOR,
        ADMIN,
        USER
    }
    
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    
    private String authorization;
    private UUID userid;
    private List<ROLE> roles;
    
    public User(String firstname, 
                String lastname, 
                String username,
                String password,
                String email,
                List<ROLE> roles)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        
        this.authorization = Base64.encodeAsString(this.username + ":" + password);
        this.userid = UUID.randomUUID();
        this.roles = roles;
    }
    
    public User(String firstname, 
                String lastname, 
                String username,
                String password,
                String email)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        
        this.authorization = Base64.encodeAsString(this.username + ":" + password);
        this.userid = UUID.randomUUID();
        this.roles = new ArrayList<ROLE>();
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAuthorization()
    {
        return authorization;
    }

    public UUID getUserid()
    {
        return userid;
    }  

    public List<ROLE> getRoles()
    {
        return roles;
    }
    
    
}
