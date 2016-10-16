package com.mycompany.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;
import org.glassfish.jersey.internal.util.Base64;

@XmlRootElement()
public class UserRepository
{
    public enum AUTHRESULT
    {
        NO_USER,
        SUCCESS,
        INVALID_ROLE,
        INVALID_AUTH,
        AUTH_BAD_FORM
    }
    
    private static List<User> users;
    
    static
    {
        users = new ArrayList<User>();
        
        users.add(new User("creator", 
                "creator", 
                "creator", 
                "creatorpsw", 
                "creator@creator.com", 
                new ArrayList<User.ROLE>() 
                {{
                    add(User.ROLE.USER);                   
                    add(User.ROLE.ADMIN);
                    add(User.ROLE.CREATOR);
                }}));
        
        users.add(new User("useradmin", 
                "useradmin", 
                "useradmin", 
                "useradminpsw", 
                "useradmin@useradmin.com", 
                new ArrayList<User.ROLE>() 
                {{
                    add(User.ROLE.USER);                   
                    add(User.ROLE.ADMIN);
                }}));
        
        users.add(new User("admin", 
                "admin", 
                "admin", 
                "adminpsw", 
                "admin@admin.com", 
                new ArrayList<User.ROLE>() 
                {{
                    add(User.ROLE.ADMIN);
                }}));
        
        users.add(new User("user", 
                "user", 
                "user", 
                "userpsw", 
                "user@user.com", 
                new ArrayList<User.ROLE>() 
                {{
                    add(User.ROLE.USER);
                }}));
        
        users.add(new User("nobody", 
                "nobody", 
                "nobody", 
                "nobodypsw", 
                "nobody@nobody.com"));
    }
    
    public static List<User> getUsers()
    {
        return users;
    }
    
    public static User getUserByUUID(UUID userid)
    {
        for (User u : users) 
        {
            if(u.getUserid().compareTo(userid) == 0)
            {
                return u;
            }
        }
        
        return null;
    }
    
    public static User getUserByUsername(String username)
    {
        for (User u : users) 
        {
            if(u.getUsername().compareTo(username) == 0)
            {
                return u;
            }
        }
        
        return null;
    }
    
    
    // Bad implementation, but should work for small amount of users just fine
    public static AUTHRESULT authenticateUser(User.ROLE authtype, String authorization)
    {
        System.out.println("Authentication user as " + authtype.toString());
        
        authorization = authorization.substring("Basic".length()).trim();

        String authdecoded = com.sun.jersey.core.util.Base64.base64Decode(authorization);
        
        if(authdecoded.indexOf(":") == -1 || authdecoded.length() <= 5)
        {
            return AUTHRESULT.AUTH_BAD_FORM;
        }
        
        String username = authdecoded.substring(0, authdecoded.indexOf(":"));            
        String password = authdecoded.substring(authdecoded.indexOf(":") + 1, authdecoded.length());

        System.out.println("Auth Base64: " + authorization);                
        System.out.println("Auth: " + authdecoded);                
        System.out.println("Username: " + username + ", Password: " + password);
        
        for (User u : users) 
        {
            if(u.getUsername().compareTo(username) == 0)
            {
                if(u.getAuthorization().compareTo(authorization) == 0)
                {
                    if(u.getRoles().contains(authtype))
                    {
                        return AUTHRESULT.SUCCESS;
                    }
                    else
                    {
                        return AUTHRESULT.INVALID_ROLE;
                    }
                }
                else
                {
                    return AUTHRESULT.INVALID_AUTH;
                }
            }
        }
        
        return AUTHRESULT.NO_USER;
    }
    
    public static boolean addUser(String firstname, 
                               String lastname, 
                               String username,
                               String password,
                               String email,
                               List<User.ROLE> roles)
    {
        for (User u : users) 
        {
            if(u.getUsername().compareTo(username) == 0 ||
               u.getEmail().compareTo(email) == 0)
            {
                return false;
            }        
        }
        
        users.add(new User(firstname, lastname, username, password, email, roles));
        
        return true;
    }
}
