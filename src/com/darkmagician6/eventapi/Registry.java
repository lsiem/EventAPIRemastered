package com.darkmagician6.eventapi;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.darkmagician6.eventapi.annotation.EventTarget;
import com.darkmagician6.eventapi.data.MethodData;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.util.EventDataMap;

/**
 * Contains all the registered MethodData sorted on the event parameters of the methods.
 * Also contains the methods for registering/unregistering methods marked with the EventTarget annotation.
 * @see EventTarget
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public class Registry {
	/**
	 * Map containing all the registered MethodData sorted on the event classes.
	 * Uses a CopyOnWriteArrayList instead of a normal ArrayList to store the MethodData to ensure stability in multi-threaded enviroments.
	 * @see EventDataMap
	 */
	private final EventDataMap dataMap;
	
	/**
	 * Set's the dataMap to a new, fresh EventDataMap when the Registry is instantiated.
	 */
	public Registry() {
		dataMap = new EventDataMap();
	}
	
	/**
	 * Registers all the methods marked with the EventTarget annotation in the class that implements the Listener interface.
	 * 
	 * @param listener
	 * 		Object that implements the Listener interface.
	 */
	public void registerListener(Listener listener) {
		for(final Method method : listener.getClass().getDeclaredMethods()) {
			if(isMethodBad(method))
				continue;
			
			register(method, listener);
		}
	}
	
	/**
	 * Registers the methods marked with the EventTarget annotation and that require the specified Event as the parameter in the class that implements the Listener interface.
	 * 
	 * @param listener
	 * 		Object that implements the Listener interface.
	 * @param
	 * 		Parameter class for the marked method we are looking for.
	 */
	public void registerListener(Listener listener, Class<? extends Event> eventClass) {
		for(final Method method : listener.getClass().getDeclaredMethods()) {
			if(isMethodBad(method, eventClass))
				continue;
			
			register(method, listener);
		}
	}
	
	/**
	 * Unregisters all the methods in the object that implements the Listener interface.
	 * 
	 * @param listener
	 * 		Object that implements the Listener interface.
	 */
	public void unregisterListener(Listener listener) {
		for(final List<MethodData> dataList : dataMap.values()) {
			for(final MethodData data : dataList) {
				if(data.getSource().equals(listener))
					dataList.remove(data);
			}
		}
		
		dataMap.clearEmptyEntries();
	}
	
	/**
	 * Unregisters all the methods in the object that implements the Listener interface and require the specified event as their parameter.
	 * 
	 * @param listener
	 * 		Object that implements the Listener interface.
	 * @param
	 * 		Parameter class for the method to remove.
	 */
	public void unregisterListener(Listener listener, Class<? extends Event> eventClass) {
		if(dataMap.containsKey(eventClass)) {
			for(final MethodData data : dataMap.get(eventClass)) {
				if(data.getSource().equals(listener))
					dataMap.get(eventClass).remove(data);
			}
			
			dataMap.clearEmptyEntries();
		}
	}
	
	/**
	 * Registers a new MethodData to the dataMap.
	 * If the dataMap already contains the key of the Method's first argument it will add
	 * a new MethodData to key's matching list and sorts it based on Priority. @see Priority
	 * Otherwise it will put a new entry in the dataMap with a the first argument's class
	 * and a new CopyOnWriteArrayList containing the new MethodData.
	 * 
	 * @param method
	 * 		Method to register to the dataMap.
	 * @param listener
	 * 		Source listener of the method.
	 */
	protected void register(Method method, Listener listener) {
		Class<?> indexClass = method.getParameterTypes()[0];
		final MethodData data = new MethodData(listener, method, method.getAnnotation(EventTarget.class).value());
	
		if(dataMap.containsKey(indexClass)) {
			dataMap.get(indexClass).add(data);
			dataMap.sortListValue(indexClass);
		} else {
			dataMap.put(indexClass, new CopyOnWriteArrayList<MethodData>() {
				/**
				 * Eclipse wanted me to add the UID. :/
				 */
				private static final long serialVersionUID = 69L; {
					add(data);
			}});
		}
	}
	
	/**
	 * Checks if the method does not meet the requirements to be used to recieve event calls from the Dispatcher.
	 * Performed checks: Checks if the parameter length is not 1 and if the EventTarget annotation is not present.
	 * @see EventTarget
	 * 
	 * @param method
	 * 		Method to check.
	 * @return
	 * 		True if the method should not be used for recieving event calls from the Dispatcher.
	 */
	protected boolean isMethodBad(Method method) {
		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
	}
	
	/**
	 * Checks if the method does not meet the requirements to be used to recieve event calls from the Dispatcher.
	 * Performed checks: Checks if the parameter class of the method is the same as the event we want to recieve.
	 * @see EventTarget
	 * 
	 * @param method
	 * 		Method to check.
	 * @param
	 * 		Class of the Event we want to find a method for recieving it.
	 * @return
	 * 		True if the method should not be used for recieving event calls from the Dispatcher.
	 */
	protected boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
		return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
	}
	
	/**
	 * Get's the MethodData list from the dataMap based on the event class.
	 * 
	 * @param event
	 * 		Event of which we want to get the registered MethodData from.
	 * @return
	 * 		List containing the right MethodData.
	 */
	public final List<MethodData> getMatchingData(final Event event) {
		return dataMap.get(event.getClass());
	}

}
