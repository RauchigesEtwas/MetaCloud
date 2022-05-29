package io.metacloud.events;



import io.metacloud.events.bin.EventHandler;
import io.metacloud.events.bin.ICloudEvent;
import io.metacloud.events.bin.IEventStack;

import java.lang.reflect.Method;
import java.util.*;

public class EventDriver {

    public static final int PRE = -1;
    public static final int ALL = 0;
    public static final int POST = 1;

    private final Map<Class<? extends IEventStack>, Collection<EventProcess>> bindings;
    private final Set<ICloudEvent> registeredListeners;

    public EventDriver() {
        this.bindings = new HashMap<>();
        this.registeredListeners = new HashSet<>();
    }

    public List<EventProcess> getListenersFor(Class<? extends IEventStack> clazz) {
        if (!this.bindings.containsKey(clazz))
            return new ArrayList<>(); // No handlers so we return an empty list
        return new ArrayList<>(this.bindings.get(clazz));
    }

    public <T extends IEventStack> T executeEvent(T event, int i) {
        Collection<EventProcess> handlers = this.bindings.get(event.getClass());
        if (handlers == null) {
            return event;
        }
        for (EventProcess handler : handlers) {
            if (i == PRE && handler.getPriority() >= 0)
                continue;
            if (i == POST && handler.getPriority() < 0)
                continue;
            handler.execute(event);
        }
        return event;
    }
    public <T extends IEventStack> T executeEvent(T event) {
        return this.executeEvent(event, ALL);
    }

    public void registerListener(final ICloudEvent listener) {
        if (registeredListeners.contains(listener)) {
            return;
        }

        Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (final Method method : methods) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null)
                continue;

            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) // all listener methods should only have one parameter
                continue;

            Class<?> param = parameters[0];

            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            if (IEventStack.class.isAssignableFrom(param)) {
                @SuppressWarnings("unchecked") // Java just doesn't understand that this actually is a safe cast because of the above if-statement
                Class<? extends IEventStack> realParam = (Class<? extends IEventStack>) param;

                if (!this.bindings.containsKey(realParam)) {
                    this.bindings.put(realParam, new TreeSet<>());
                }
                Collection<EventProcess> eventHandlersForEvent = this.bindings.get(realParam);
                eventHandlersForEvent.add(createEventHandler(listener, method, annotation));
            }
        }
    }

    private EventProcess createEventHandler(final ICloudEvent listener, final Method method, final EventHandler annotation) {
        return new EventProcess(listener, method, annotation);
    }

    public void clearListeners() {
        this.bindings.clear();
        this.registeredListeners.clear();
    }

    public void removeListener(ICloudEvent listener) {
        for (Map.Entry<Class<? extends IEventStack>, Collection<EventProcess>> ee : bindings.entrySet()) {
            Iterator<EventProcess> it = ee.getValue().iterator();
            while (it.hasNext()) {
                EventProcess curr = it.next();
                if (curr.getListener() == listener)
                    it.remove();
            }
        }
        this.registeredListeners.remove(listener);
    }
    public Map<Class<? extends IEventStack>, Collection<EventProcess>> getBindings() {
        return new HashMap<>(bindings);
    }
    public Set<ICloudEvent> getRegisteredListeners() {
        return new HashSet<>(registeredListeners);
    }
}
