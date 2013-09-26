package com.darkmagician6.eventapi.data;

import com.darkmagician6.eventapi.Listener;

import java.lang.reflect.Method;

/**
 * Simple class used to cache data from methods that are usable for the Dispatcher
 * to dispatch an Event to.
 *
 * @author DarkMagician6
 * @since August 29, 2013
 */
public final class ListenerData {

    private final Listener source;

    private final Method target;

    private final byte priority;

    /**
     * Sets the values of the data.
     *
     * @param source
     *         The source Listener of the data. Used by the VM to
     *         determine to which object it should send the call to.
     * @param target
     *         The targeted Method to which the Event should be send to.
     * @param priority
     *         The priority of this Method. Used by the registry to sort
     *         the data on.
     */
    public ListenerData(Listener source, Method target, byte priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }

    /**
     * Gets the Listener source of the data.
     *
     * @return Source Listener of the targeted Method.
     */
    public Listener getSource() {
        return source;
    }

    /**
     * Gets the targeted Method.
     *
     * @return The Method that is Listening to certain Event calls.
     */
    public Method getTarget() {
        return target;
    }

    /**
     * Gets the priority value of the targeted Method.
     *
     * @return The priority value of the targeted Method.
     */
    public byte getPriority() {
        return priority;
    }

}
