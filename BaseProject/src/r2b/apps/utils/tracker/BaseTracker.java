/*
 * BaseTracker
 * 
 * 0.1
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package r2b.apps.utils.tracker;

import r2b.apps.R;
import r2b.apps.utils.Cons;
import r2b.apps.utils.logger.Logger;
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
	 * Categories.
	 */
	public static enum CATEGORY {
		GUI
	};
	
	/**
	 * Actions.
	 */
	public static enum ACTION {
		click, button_click, list_item_click, grid_item_click
	};
	
	/**
	 * Google analytics tracker.
	 */
	private Tracker tracker;
	
	/**
	 * Builder. Only tracks if Cons.TRACKER is available
	 * @param context The application context.
	 */
	public BaseTracker(Context context) {
		if(Cons.TRACKER) {
			tracker = GoogleAnalytics.getInstance(context.getApplicationContext())
					.newTracker(R.xml.analytics_tracker);

			// XXX LOGGER
			Logger.i(this.getClass().getSimpleName(), "Analytics tracker initialized.");
		}
	}

	/**
	 * Set and send the screen name. 
	 * @param screenName The screen name.
	 */
	public void sendScreenName(String screenName) {
		if(tracker == null) {
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), screenName);
		}
		else {
			tracker.setScreenName(screenName);
			tracker.send(new HitBuilders.AppViewBuilder().build());
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), "Sending screen name: " + screenName);
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
			// XXX LOGGER
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
			
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), "Sending event, " + 
					"category : " + category + 
					", action: " + action + 
					", label: " + label + 
					", value: " + String.valueOf(value));
		}
	}
	
}
