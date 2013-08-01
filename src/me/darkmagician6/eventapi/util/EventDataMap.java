package me.darkmagician6.eventapi.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import me.darkmagician6.eventapi.data.MethodData;
import me.darkmagician6.eventapi.types.Priority;

/**
 * An HashMap made for storing lists with MethodData's sorted on the corresponding Event class.
 * 
 * @author DarkMagician6
 * @since July 31,2013
 */
public final class EventDataMap extends HashMap<Class<?>, List<MethodData>> {
	/**
	 * Fuck you eclipse.
	 */
	private static final long serialVersionUID = 666L;

	/**
	 * Clears out all entries with an empty List.
	 * Uses an iterator to make sure that the entry is completely removed.
	 */
	public void clearEmptyEntries() {
		Iterator<Map.Entry<Class<?>, List<MethodData>>> iterator = entrySet().iterator();
		
		while(iterator.hasNext()) {
			if(iterator.next().getValue().isEmpty())
				iterator.remove();
		}
	}
	
	/**
	 * Sorts the List that matches the corresponding Event class based on priority type.
	 * 
	 * @param indexClass
	 * 		The Event class index in the EventDataMap of the List to sort.
	 */
	public void sortListValue(Class<?> indexClass) {
		List<MethodData> sortedList = new CopyOnWriteArrayList<MethodData>();
		
		for(final Priority priority : Priority.values()) {
			for(final MethodData data : get(indexClass)) {
				if(data.getPriority().equals(priority))
					sortedList.add(data);
			}
		}

		put(indexClass, sortedList);
	}
	
}
