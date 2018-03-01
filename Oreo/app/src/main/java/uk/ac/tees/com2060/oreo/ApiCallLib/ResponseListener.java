package uk.ac.tees.com2060.oreo.ApiCallLib;

/**
 * ResponseListener.java (ApiCallLib)
 * An abstract class to allow anonymous functions to be used as response listeners
 */
public abstract class ResponseListener
{
    public abstract void responseReceived(ApiResponse response);
}