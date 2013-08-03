package com.darkmagician6.eventapi.data;

import java.lang.reflect.Method;

/**
 * Abstract base class for classes to store data of methods.
 * 
 * @param <O>
 * 		The type of object that we has to be the source object of the method.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public abstract class Data<O> {
	private final O source;
	
	private final Method method;
	
	/**
	 * Sets the method and source of the data.
	 * 
	 * @param source
	 * 		The source object of the method.
	 * @param targetMethod
	 * 		The method we want to store.
	 */
	protected Data(O source, Method targetMethod) {
		this.source = source;
		method = targetMethod;
	}
	
	/**
	 * Get's the source of the stored method.
	 * 
	 * @return
	 *   The stored method's source object.
	 */
	public O getSource() {
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
