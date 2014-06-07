package r2b.apps.utils;

/**
 * All tracker objects must implements this interface.
 */
public interface ITracker {
	
	/**
	 * Set and send the screen name.
	 * @param screenName The screen name.
	 */
	public void sendScreenName(String screenName);
	
	/**
	 * Set and send event.
	 * @param category the event category.
	 * @param action The event action.
	 * @param label The label of the element that launch the event.
	 * @param value The value.
	 */
	public void sendEvent(String category, String action, String label, long value);
	
}
