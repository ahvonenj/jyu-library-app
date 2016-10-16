package com.mycompany.task3;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BasicResponse
{
    public boolean isError;
    public int errorCode;
    public String errorMessage;
    public String message;
    
    private static final Map<Integer, String> errorMessages;
    static
    {
        errorMessages = new HashMap<Integer, String>();
        
        errorMessages.put(0, "Success"); 
        errorMessages.put(100, "Check request parameters");        
        errorMessages.put(200, "(Operation failed) Unknown");
        errorMessages.put(210, "(Operation failed) entity already exists");
        errorMessages.put(220, "(Operation failed) requested entity is empty");
        errorMessages.put(230, "(Operation failed) entity does not exist");
    }
    
    public BasicResponse(boolean isError, int errorCode, String message)
    {
        this.isError = isError;
        this.errorCode = errorCode;
        this.message = message;
        
        if(!errorMessages.containsKey(errorCode))
        {
            throw new IllegalArgumentException("Error: No error message found for code: " + errorCode);
        }
        else
        {
            this.errorMessage = this.errorMessages.get(errorCode);
        }
    }
    
    public BasicResponse(boolean isError, int errorCode)
    {
        this.isError = isError;
        this.errorCode = errorCode;
        this.message = null;
        
        if(!errorMessages.containsKey(errorCode))
        {
            throw new IllegalArgumentException("Error: No error message found for code: " + errorCode);
        }
        else
        {
            this.errorMessage = this.errorMessages.get(errorCode);
        }
    }
}
