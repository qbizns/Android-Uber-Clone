package com.tatx.userapp.wasocketserver;

/**
 * Created by Sandeep on 01-04-2016.
 */
public interface ServerMessageListener {
    public void messageFromServer(String msgType, String message);
}
