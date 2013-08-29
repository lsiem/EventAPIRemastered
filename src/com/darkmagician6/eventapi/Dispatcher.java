package com.darkmagician6.eventapi;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.darkmagician6.eventapi.data.ListenerData;
import com.darkmagician6.eventapi.events.Event;

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
	 * Then it checks if the list is not null and starts looping trough it.
	 * For every ListenerData in the list it will invoke the Data's method with the Event as the argument.
	 * After that is all done it will return the Event.
	 * 
	 * @param event
	 * 		Event to dispatch.
	 * @return
	 * 		Event in the state after dispatching it.
	 */
	public static final Event call(final Event event) {
		List<ListenerData> dataList = Listener.registry.getMatchingData(event);
		
		if (dataList != null) {
			for (final ListenerData data : dataList) {
				invoke(data, event);
			}
		}
		
		return event;
	}
	
	/**
	 * Invokes a ListenerData when an Event call is made.
	 * 
	 * @param data
	 * 		The data of which the targeted Method should be invoked.
	 * 		@see com.darkmagician6.eventapi.data.ListenerData
	 * @param argument
	 * 		The called Event which should be used as an argument for the targeted Method.
	 */
	private static void invoke(ListenerData data, Event argument) {
		try {
			data.getTarget().invoke(data.getSource(), argument);
		} catch(IllegalAccessException e) {
		} catch(IllegalArgumentException e) {
		} catch(InvocationTargetException e) {
		}
	}

}
