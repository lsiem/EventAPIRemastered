package com.darkmagician6.eventapi;

import com.darkmagician6.eventapi.annotation.EventTarget;
import com.darkmagician6.eventapi.data.ListenerData;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.Priority;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * HashMap containing all the registered ListenerData sorted on the event parameters of the methods.
 * Also contains the methods for registering/unregistering methods marked with the EventTarget annotation.
 *
 * @author DarkMagician6
 * @see com.darkmagician6.eventapi.annotation.EventTarget
 * @since August 3, 2013
 */
public final class RegistryMap extends HashMap<Class<?>, List<ListenerData>> {

    /**
     * Because Eclipse wanted it.
     */
    private static final long serialVersionUID = 666L;

    /**
     * Set's up the HashMap with a custom initial size and load factor.
     */
    public RegistryMap() {
        //Left at default for now to ensure stability, might look more into this later.
        super(16, 0.75F);
    }

    /**
     * Registers all the methods marked with the EventTarget annotation in the class that implements the Listener interface.
     *
     * @param listener
     *         Object that implements the Listener interface.
     */
    public void registerListener(Listener listener) {
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            if (isMethodBad(method)) {
                continue;
            }

            register(method, listener);
        }
    }

    /**
     * Registers the methods marked with the EventTarget annotation and that require
     * the specified Event as the parameter in the class that implements the Listener interface.
     *
     * @param listener
     *         Object that implements the Listener interface.
     * @param Parameter
     *         class for the marked method we are looking for.
     */
    public void registerListener(Listener listener, Class<? extends Event> eventClass) {
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            if (isMethodBad(method, eventClass)) {
                continue;
            }

            register(method, listener);
        }
    }

    /**
     * Unregisters all the methods in the object that implements the Listener interface.
     *
     * @param listener
     *         Object that implements the Listener interface.
     */
    public void unregisterListener(Listener listener) {
        for (final List<ListenerData> dataList : values()) {
            for (final ListenerData data : dataList) {
                if (data.getSource().equals(listener)) {
                    dataList.remove(data);
                }
            }
        }

        cleanMap(true);
    }

    /**
     * Unregisters all the methods in the object that implements the Listener interface
     * and require the specified event as their parameter.
     *
     * @param listener
     *         Object that implements the Listener interface.
     * @param Parameter
     *         class for the method to remove.
     */
    public void unregisterListener(Listener listener, Class<? extends Event> eventClass) {
        if (containsKey(eventClass)) {
            for (final ListenerData data : get(eventClass)) {
                if (data.getSource().equals(listener)) {
                    get(eventClass).remove(data);
                }
            }

            cleanMap(true);
        }
    }

    /**
     * Registers a new ListenerData to the HashMap.
     * If the HashMap already contains the key of the Method's first argument it will add
     * a new ListenerData to key's matching list and sorts it based on Priority. @see com.darkmagician6.eventapi.types.Priority
     * Otherwise it will put a new entry in the HashMap with a the first argument's class
     * and a new CopyOnWriteArrayList containing the new ListenerData.
     *
     * @param method
     *         Method to register to the HashMap.
     * @param listener
     *         Source listener of the method.
     */
    private void register(Method method, Listener listener) {
        Class<?> indexClass = method.getParameterTypes()[0];
        //New ListenerData from the Method we are registering.
        final ListenerData data = new ListenerData(listener, method, method.getAnnotation(EventTarget.class).value());
        //Set's the method to accessible so that we can also invoke it if it's protected or private.
        if (!data.getTarget().isAccessible()) {
            data.getTarget().setAccessible(true);
        }

        if (containsKey(indexClass)) {
            if (!get(indexClass).contains(data)) {
                get(indexClass).add(data);
                sortListValue(indexClass);
            }
        } else {
            put(indexClass, new CopyOnWriteArrayList<ListenerData>() {
                //Eclipse wanted me to add the UID. :/
                private static final long serialVersionUID = 69L; {
                    add(data);
                }
            });
        }
    }

    /**
     * Removes an entry based on the key value in the map.
     *
     * @param indexClass
     *         They index key in the map of which the entry should be removed.
     */
    public void removeEntry(Class<?> indexClass) {
        Iterator<Map.Entry<Class<?>, List<ListenerData>>> mapIterator = entrySet().iterator();

        while (mapIterator.hasNext()) {
            if (mapIterator.next().getKey().equals(indexClass)) {
                mapIterator.remove();
                break;
            }
        }
    }

    /**
     * Cleans up the map entries.
     * Uses an iterator to make sure that the entry is completely removed.
     *
     * @param onlyEmptyEntries
     *         If true only remove the entries with an empty list, otherwise remove all the entries.
     */
    public void cleanMap(boolean onlyEmptyEntries) {
        Iterator<Map.Entry<Class<?>, List<ListenerData>>> mapIterator = entrySet().iterator();

        while (mapIterator.hasNext()) {
            if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
                mapIterator.remove();
            }
        }
    }

    /**
     * Sorts the List that matches the corresponding Event class based on priority value.
     *
     * @param indexClass
     *         The Event class index in the HashMap of the List to sort.
     */
    private void sortListValue(Class<?> indexClass) {
        List<ListenerData> sortedList = new CopyOnWriteArrayList<ListenerData>();

        for (final byte priority : Priority.VALUE_ARRAY) {
            for (final ListenerData data : get(indexClass)) {
                if (data.getPriority() == priority) {
                    sortedList.add(data);
                }
            }
        }

        //Overwriting the existing entry.
        put(indexClass, sortedList);
    }

    /**
     * Checks if the method does not meet the requirements to be used to receive event calls from the Dispatcher.
     * Performed checks: Checks if the parameter length is not 1 and if the EventTarget annotation is not present.
     *
     * @param method
     *         Method to check.
     *
     * @return True if the method should not be used for recieving event calls from the Dispatcher.
     *
     * @see com.darkmagician6.eventapi.annotation.EventTarget
     */
    private boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }

    /**
     * Checks if the method does not meet the requirements to be used to receive event calls from the Dispatcher.
     * Performed checks: Checks if the parameter class of the method is the same as the event we want to receive.
     *
     * @param method
     *         Method to check.
     * @param Class
     *         of the Event we want to find a method for receiving it.
     *
     * @return True if the method should not be used for receiving event calls from the Dispatcher.
     *
     * @see com.darkmagician6.eventapi.annotation.EventTarget
     */
    private boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
        return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
    }

}
