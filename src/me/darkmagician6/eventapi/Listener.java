package me.darkmagician6.eventapi;

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
	 * @see Registry
	 */
	Registry registry = new Registry();
}
