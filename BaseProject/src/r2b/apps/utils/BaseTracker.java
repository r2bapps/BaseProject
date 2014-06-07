package r2b.apps.utils;

import r2b.apps.R;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * 
 * Tracker object or lib factory.
 * 
 * If tracking is not available send info to logger on verbose mode.
 */
public class BaseTracker implements ITracker {
	
	/**
	 * Google analytics tracker.
	 */
	private Tracker tracker;
	
	public BaseTracker(Context context) {
		if(Cons.TRACKER) {
			tracker = GoogleAnalytics.getInstance(context.getApplicationContext())
					.newTracker(R.xml.analytics_tracker);			
		}
	}

	/**
	 * Set and send the screen name. 
	 * @param screenName The screen name.
	 */
	public void sendScreenName(String screenName) {
		if(tracker == null) {
			Logger.v(this.getClass().getSimpleName(), screenName);
		}
		else {
			tracker.setScreenName(screenName);
			tracker.send(new HitBuilders.AppViewBuilder().build());
		}
	}
	
	/**
	 * Set and send event.
	 * @param category the event category.
	 * @param action The event action.
	 * @param label The label of the element that launch the event.
	 * @param value The value.
	 */
	public void sendEvent(String category, String action, String label, long value) {
		if(tracker == null) {
			Logger.v(this.getClass().getSimpleName(), 
					"category : " + category + 
					", action: " + action + 
					", label: " + label + 
					", value: " + String.valueOf(value));
		}
		else {
			tracker.send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.setLabel(label)
				.setValue(value).build());
		}
	}
	
}
