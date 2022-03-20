package io.metacloud.handlers;


import io.metacloud.handlers.bin.IEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;

import java.lang.reflect.Method;
import java.util.*;

public class PacketListenerHandler {

    public static final int PRE = -1;
    public static final int ALL = 0;
    public static final int POST = 1;

    private final Map<Class<? extends IEvent>, Collection<PacketExecutor>> bindings;
    private final Set<PacketListener> registeredListeners;

    public PacketListenerHandler() {
        this.bindings = new HashMap<>();
        this.registeredListeners = new HashSet<>();
    }

    public List<PacketExecutor> getListenersFor(Class<? extends IEvent> clazz) {
        if (!this.bindings.containsKey(clazz))
            return new ArrayList<>(); // No handlers so we return an empty list
        return new ArrayList<>(this.bindings.get(clazz));
    }

    public <T extends IEvent> T executeEvent(T event, int i) {
        Collection<PacketExecutor> handlers = this.bindings.get(event.getClass());
        if (handlers == null) {
            return event;
        }
        for (PacketExecutor handler : handlers) {
            if (i == PRE && handler.getPriority() >= 0)
                continue;
            if (i == POST && handler.getPriority() < 0)
                continue;
            handler.execute(event);
        }
        return event;
    }
    public <T extends IEvent> T executeEvent(T event) {
        return this.executeEvent(event, ALL);
    }

    public void registerListener(final PacketListener listener) {
        if (registeredListeners.contains(listener)) {
            return;
        }

        Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (final Method method : methods) {
            PacketProvideHandler annotation = method.getAnnotation(PacketProvideHandler.class);
            if (annotation == null)
                continue;

            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) // all listener methods should only have one parameter
                continue;

            Class<?> param = parameters[0];

            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            if (IEvent.class.isAssignableFrom(param)) {
                @SuppressWarnings("unchecked") // Java just doesn't understand that this actually is a safe cast because of the above if-statement
                Class<? extends IEvent> realParam = (Class<? extends IEvent>) param;

                if (!this.bindings.containsKey(realParam)) {
                    this.bindings.put(realParam, new TreeSet<>());
                }
                Collection<PacketExecutor> eventHandlersForEvent = this.bindings.get(realParam);
                eventHandlersForEvent.add(createEventHandler(listener, method, annotation));
            }
        }
    }

    private PacketExecutor createEventHandler(final PacketListener listener, final Method method, final PacketProvideHandler annotation) {
        return new PacketExecutor(listener, method, annotation);
    }

    public void clearListeners() {
        this.bindings.clear();
        this.registeredListeners.clear();
    }

    public void removeListener(PacketListener listener) {
        for (Map.Entry<Class<? extends IEvent>, Collection<PacketExecutor>> ee : bindings.entrySet()) {
            Iterator<PacketExecutor> it = ee.getValue().iterator();
            while (it.hasNext()) {
                PacketExecutor curr = it.next();
                if (curr.getListener() == listener)
                    it.remove();
            }
        }
        this.registeredListeners.remove(listener);
    }
    public Map<Class<? extends IEvent>, Collection<PacketExecutor>> getBindings() {
        return new HashMap<>(bindings);
    }
    public Set<PacketListener> getRegisteredListeners() {
        return new HashSet<>(registeredListeners);
    }
}
