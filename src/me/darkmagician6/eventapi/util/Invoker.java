package me.darkmagician6.eventapi.util;

import java.lang.reflect.InvocationTargetException;

import me.darkmagician6.eventapi.data.Data;

/**
 * Simple utility class used for invoking single argument methods.
 * 
 * @param <E>
 * 		Type of object we want to use as the argument for the methods to invoke.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public final class Invoker<E> {
	
	/**
	 * Invokes a method with the specified arguments.
	 * 
	 * @param data
	 * 		The data object containing the method and the source of the method we are invoking.
	 * @param arg
	 * 		Argument that is needed to invoke the method. The type of the argument is defined when the Invoker is initialized.
	 */
	public void invoke(final Data data, E arg) {
		try {
			data.getMethod().invoke(data.getSource(), arg);
		} catch(IllegalAccessException e) {
		} catch(IllegalArgumentException e) {
		} catch(InvocationTargetException e) {
		}
	}

}
