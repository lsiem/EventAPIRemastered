package me.darkmagician6.eventapi.data;

import java.lang.reflect.Method;

/**
 * Abstract base class for classes to store data of methods.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public abstract class Data {
	private final Object source;
	
	private final Method method;
	
	/**
	 * Sets the method and source of the data.
	 * 
	 * @param source
	 * 		The source object of the method.
	 * @param targetMethod
	 * 		The method we want to store.
	 */
	protected Data(Object source, Method targetMethod) {
		this.source = source;
		method = targetMethod;
	}
	
	/**
	 * Get's the source of the stored method.
	 * 
	 * @return
	 *   The stored method's source object.
	 */
	public Object getSource() {
		return source;
	}
	
	/**
	 * Get's the stored method.
	 * 
	 * @return
	 * 		The stored method.
	 */
	public Method getMethod() {
		return method;
	}
	
}
