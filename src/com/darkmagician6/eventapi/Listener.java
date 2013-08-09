package com.darkmagician6.eventapi;

/**
 * Helps with registering methods to the registry.
 * This interface needs to be implemented in every class with methods that should be registered.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public interface Listener {
	/**
	 * The registry instance used by the Dispatcher and the classes that implement the Listener interface.
	 * Fields in interfaces are public, static and final by default so there is no need to add those keywords manually.
	 * @see Registry
	 */
	RegistryMap registry = new RegistryMap();
}
