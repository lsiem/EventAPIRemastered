package com.darkmagician6.eventapi;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.darkmagician6.eventapi.annotation.EventTarget;
import com.darkmagician6.eventapi.data.MethodData;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.Priority;

/**
 * HashMap containing all the registered MethodData sorted on the event parameters of the methods.
 * Also contains the methods for registering/unregistering methods marked with the EventTarget annotation.
 * @see EventTarget
 * 
 * @author DarkMagician6
 * @since August 3, 2013
 */
public final class RegistryMap extends HashMap<Class<?>, List<MethodData>> {
	
	/**
	 * Because Eclipse wanted it.
	 */
	private static final long serialVersionUID = 666L;

	/**
	 * Set's up the HashMap with a custom initial size and load factor.
	 */
	public RegistryMap() {
		/**
		 * Slightly lower load factor should improve the reading performance as we read way more then that we write.
		 * If you have more then 16 events it might be smart to choose a higher initial size to save a tiny bit of performance.
		 */
		super(16, 0.7F);
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
		for(final List<MethodData> dataList : values()) {
			for(final MethodData data : dataList) {
				if(data.getSource().equals(listener))
					dataList.remove(data);
			}
		}
		
		clearEmptyEntries();
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
		if(containsKey(eventClass)) {
			for(final MethodData data : get(eventClass)) {
				if(data.getSource().equals(listener))
					get(eventClass).remove(data);
			}
			
			clearEmptyEntries();
		}
	}
	
	/**
	 * Registers a new MethodData to the HashMap.
	 * If the HashMap already contains the key of the Method's first argument it will add
	 * a new MethodData to key's matching list and sorts it based on Priority. @see Priority
	 * Otherwise it will put a new entry in the HashMap with a the first argument's class
	 * and a new CopyOnWriteArrayList containing the new MethodData.
	 * 
	 * @param method
	 * 		Method to register to the HashMap.
	 * @param listener
	 * 		Source listener of the method.
	 */
	private void register(Method method, Listener listener) {
		Class<?> indexClass = method.getParameterTypes()[0];
		final MethodData data = new MethodData(listener, method, method.getAnnotation(EventTarget.class).value());
	
		if(containsKey(indexClass)) {
			get(indexClass).add(data);
			sortListValue(indexClass);
		} else {
			put(indexClass, new CopyOnWriteArrayList<MethodData>() {
				/**
				 * Eclipse wanted me to add the UID. :/
				 */
				private static final long serialVersionUID = 69L; {
					add(data);
			}});
		}
	}
	
	/**
	 * Clears out all entries with an empty List.
	 * Uses an iterator to make sure that the entry is completely removed.
	 */
	private void clearEmptyEntries() {
		Iterator<Map.Entry<Class<?>, List<MethodData>>> iterator = entrySet().iterator();
		
		while(iterator.hasNext()) {
			if(iterator.next().getValue().isEmpty())
				iterator.remove();
		}
	}
	
	/**
	 * Sorts the List that matches the corresponding Event class based on priority value.
	 * 
	 * @param indexClass
	 * 		The Event class index in the HashMap of the List to sort.
	 */
	private void sortListValue(Class<?> indexClass) {
		List<MethodData> sortedList = new CopyOnWriteArrayList<MethodData>();
		
		for(final byte priority : Priority.VALUE_ARRAY) {
			for(final MethodData data : get(indexClass)) {
				if(data.getPriority() == priority)
					sortedList.add(data);
			}
		}

		put(indexClass, sortedList);
	}
	
	/**
	 * Checks if the method does not meet the requirements to be used to receive event calls from the Dispatcher.
	 * Performed checks: Checks if the parameter length is not 1 and if the EventTarget annotation is not present.
	 * @see EventTarget
	 * 
	 * @param method
	 * 		Method to check.
	 * @return
	 * 		True if the method should not be used for recieving event calls from the Dispatcher.
	 */
	private boolean isMethodBad(Method method) {
		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
	}
	
	/**
	 * Checks if the method does not meet the requirements to be used to receive event calls from the Dispatcher.
	 * Performed checks: Checks if the parameter class of the method is the same as the event we want to receive.
	 * @see EventTarget
	 * 
	 * @param method
	 * 		Method to check.
	 * @param
	 * 		Class of the Event we want to find a method for receiving it.
	 * @return
	 * 		True if the method should not be used for receiving event calls from the Dispatcher.
	 */
	private boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
		return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
	}
	
	/**
	 * Get's the MethodData list from the HashMap based on the event class.
	 * 
	 * @param event
	 * 		Event of which we want to get the registered MethodData from.
	 * @return
	 * 		List containing the right MethodData.
	 */
	public final List<MethodData> getMatchingData(final Event event) {
		return get(event.getClass());
	}

}
