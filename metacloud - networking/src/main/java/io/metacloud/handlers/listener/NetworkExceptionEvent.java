package io.metacloud.handlers.listener;


import io.metacloud.handlers.bin.IEvent;

public class NetworkExceptionEvent implements IEvent {

    private Exception exception;

    public NetworkExceptionEvent(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
