package com.mycompany.task3;

public class DataResponse extends BasicResponse
{
    public Object data;
    
    public DataResponse(boolean isError, int errorCode, String message, Object data)
    {
        super(isError, errorCode, message);
        
        this.data = data;
    }

    public DataResponse(boolean isError, int errorCode, Object data)
    {
        super(isError, errorCode);
        
        this.data = data;
    }
    
}
