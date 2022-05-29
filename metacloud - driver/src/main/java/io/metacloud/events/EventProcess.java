package io.metacloud.events;


import io.metacloud.events.bin.EventHandler;
import io.metacloud.events.bin.ICloudEvent;
import io.metacloud.events.bin.IEventStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class EventProcess implements  Comparable<EventProcess>{
    private final ICloudEvent listener;
    private final Method method;
    private final EventHandler annotation;

    public EventProcess(ICloudEvent listener, Method method, EventHandler annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }
    public EventHandler getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
    public ICloudEvent getListener() {
        return listener;
    }

    public void execute(IEventStack event) {
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException e1) {
        } catch (IllegalArgumentException e1) {
        } catch (InvocationTargetException e1) {
        }
    }
    @Override
    public String toString() {
        return "(EventHandler " + this.listener + ": " + method.getName() + ")";
    }

    public int getPriority() {
        return annotation.priority();
    }

    @Override
    public int compareTo(EventProcess other) {
        int annotation = this.annotation.priority() - other.annotation.priority();
        if (annotation == 0)
            annotation = this.listener.hashCode() - other.listener.hashCode();
        return annotation == 0 ? this.hashCode() - other.hashCode() : annotation;
    }
}
