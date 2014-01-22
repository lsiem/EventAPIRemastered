package com.darkmagician6.eventapi;

import com.darkmagician6.eventapi.data.ListenerData;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.EventStoppable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Used to call Events and to send them to the methods that are listening for them.
 *
 * @author DarkMagician6
 * @since July 30, 2013
 */
public final class Dispatcher {

    /**
     * Private because there is no need to create an instance of this class.
     */
    private Dispatcher() {
    }

    /**
     * Call's an event and invokes the right methods that are listening to the event call.
     * First get's the matching list from the registry based on the class of the event.
     * Then it checks if the list is not null. After that it will check if the event is an instance of
     * EventStoppable and if so it will add an extra check when loopping trough the data.
     * If the Event was an instance of EventStoppable it will check every loop if the EventStoppable is stopped, and if
     * it is it will break the loop, thus stopping the call.
     * For every ListenerData in the list it will invoke the Data's method with the Event as the argument.
     * After that is all done it will return the Event.
     *
     * @param event
     *         Event to dispatch.
     *
     * @return Event in the state after dispatching it.
     */
    public static final Event call(final Event event) {
        List<ListenerData> dataList = Listener.registry.get(event.getClass());

        if (dataList != null) {
            if (event instanceof EventStoppable) {
                EventStoppable stoppable = (EventStoppable) event;

                for (final ListenerData data : dataList) {
                    invoke(data, event);

                    if (stoppable.isStopped()) {
                        break;
                    }
                }
            } else {
                for (final ListenerData data : dataList) {
                    invoke(data, event);
                }
            }
        }

        return event;
    }

    /**
     * Invokes a ListenerData when an Event call is made.
     *
     * @param data
     *         The data of which the targeted Method should be invoked.
     * @param argument
     *         The called Event which should be used as an argument for the targeted Method.
     *
     * @see com.darkmagician6.eventapi.data.ListenerData
     */
    private static void invoke(ListenerData data, Event argument) {
        try {
            data.getTarget().setAccessible(true);
            data.getTarget().invoke(data.getSource(), argument);
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        }
    }

}
