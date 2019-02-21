package com.pingidentity.sample.exception;

public class DropoffException extends Exception
{
    private static final long serialVersionUID = 2L;

    public DropoffException()
    {
    }

    public DropoffException(String message)
    {
        super(message);
    }

    public DropoffException(Throwable cause)
    {
        super(cause);
    }

    public DropoffException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
