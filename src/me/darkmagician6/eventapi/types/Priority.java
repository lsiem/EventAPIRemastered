package me.darkmagician6.eventapi.types;

/**
 * The priority for the dispatcher to determine what method should be invoked first.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public enum Priority {
	/**
	 * Highest priority, called first.
	 */
	HIGHEST,
	/**
	 * High priority, called after the highest priority.
	 */
	HIGH,
	/**
	 * Medium priority, called after the high priority.
	 */
	MEDIUM,
	/**
	 * Low priority, called after the medium priority.
	 */
	LOW,
	/**
	 * Lowest priority, called after all the other priorities.
	 */
	LOWEST
}
