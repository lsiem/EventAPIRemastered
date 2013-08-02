package com.darkmagician6.eventapi;

import java.util.List;

import com.darkmagician6.eventapi.data.MethodData;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.util.Invoker;

/**
 * Used to call Events and to send them to the methods that are listening for them.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public class Dispatcher {
	protected static volatile Dispatcher instance;
	
	/**
	 * The invoker instance is used to invoke MethodData.
	 * The type for the invoker is Event because we want it to use Events as arguments when invoking methods.
	 * @see Invoker
	 */
	protected final Invoker<Event> invoker;
	/**
	 * @see Registry
	 */
	protected final Registry registry;
	
	/**
	 * Set's up the Invoker and Registry instances when the Dispatcher instance is created.
	 */
	protected Dispatcher() {
		invoker = new Invoker<Event>();
		registry = Listener.registry;
	}
	
	/**
	 * Call's an event and invokes the right methods that are listening to the event call.
	 * First get's the matching list from the registry based on the class of the event.
	 * Then it checks if the list is not null and starts looping trough it.
	 * For every MethodData in the list it will invoke the Data's method with the Event as the argument.
	 * After that is all done it will return the Event.
	 * 
	 * @param event
	 * 		Event to dispatch.
	 * @return
	 * 		Event in the state after dispatching it.
	 */
	public Event call(final Event event) {
		List<MethodData> dataList = registry.getMatchingData(event);
		
		if(dataList != null) {
			for(final MethodData data : dataList)
				invoker.invoke(data, event);
		}
		return event;
	}
	
	/**
	 * Public singleton of the Dispatcher.
	 * 
	 * @return
	 * 		Singleton instance of the Dispatcher object. Also set's the singleton if it's null.
	 */
	public static Dispatcher getInstance() {
		return instance == null ? instance = new Dispatcher() : instance;
	}

}
