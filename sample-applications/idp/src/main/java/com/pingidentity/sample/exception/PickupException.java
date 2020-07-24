package com.pingidentity.sample.exception;

public class PickupException extends Exception
{
    private static final long serialVersionUID = 2L;

    public PickupException()
    {
    }

    public PickupException(String message)
    {
        super(message);
    }

    public PickupException(Throwable cause)
    {
        super(cause);
    }

    public PickupException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
