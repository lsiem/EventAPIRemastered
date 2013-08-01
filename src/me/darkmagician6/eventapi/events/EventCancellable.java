package me.darkmagician6.eventapi.events;

/**
 * The abstract base of a cancellable event.
 * Extend this event in order to use cancellable events.
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
public abstract class EventCancellable implements Event {
	private boolean cancelled;
	
	/**
	 * No need for the constructor to be public.
	 */
	protected EventCancellable() {}

	/**
	 * Get's the current cancelled state of the event.
	 * 
	 * @return
	 * 		True if the event is cancelled.
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Set's the cancelled state of the event.
	 * 
	 * @param state
	 * 		Whether the event should be cancelled or not.
	 */
	public void setCancelled(final boolean state) {
		cancelled = state;
	}
	
}
