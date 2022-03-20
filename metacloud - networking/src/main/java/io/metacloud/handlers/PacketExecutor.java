package io.metacloud.handlers;

import io.metacloud.handlers.bin.IEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketExecutor implements Comparable<PacketExecutor>{
    private final PacketListener listener;
    private final Method method;
    private final PacketProvideHandler annotation;

    public PacketExecutor(PacketListener listener, Method method, PacketProvideHandler annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }

    public PacketProvideHandler getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
    public PacketListener getListener() {
        return listener;
    }

    public void execute(IEvent event) {
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
    public int compareTo(PacketExecutor other) {
        int annotation = this.annotation.priority() - other.annotation.priority();
        if (annotation == 0)
            annotation = this.listener.hashCode() - other.listener.hashCode();
        return annotation == 0 ? this.hashCode() - other.hashCode() : annotation;
    }
}
