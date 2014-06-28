/*
 * BaseApplication
 * 
 * 0.1.1
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

package r2b.apps.base;

import r2b.apps.R;
import r2b.apps.utils.AESCipher;
import r2b.apps.utils.BaseTracker;
import r2b.apps.utils.Cons;
import r2b.apps.utils.ITracker;
import r2b.apps.utils.Logger;
import android.app.Application;

/**
 * Used as singleton class to initialize managers and utilities.
 */
public class BaseApplication extends Application {

	/**
	 * Tracker wrapper.
	 */
	private ITracker tracker;

	@Override
	public void onCreate() {
		super.onCreate();		
		
		initConfig();
		
		tracker = new BaseTracker(this);
		
		if(Cons.ENCRYPT) {
			AESCipher.init(getApplicationContext());
		}

	}

	/**
	 * Get the events tracker.
	 * @return The events tracker.
	 */
	public ITracker getTracker() {
		return tracker;
	}
	
	/**
	 * Init Cons constants from base_config xml file.
	 */
	private void initConfig() {			
		
		Cons.DEBUG = getResources().getBoolean(R.bool.debug);
		Cons.SHOW_LOGS = Cons.DEBUG && getResources().getBoolean(R.bool.log);
		Cons.FAKE_DATA = Cons.DEBUG && getResources().getBoolean(R.bool.fake);
		Cons.TRACKER = getResources().getBoolean(R.bool.track);
		Cons.ENCRYPT = getResources().getBoolean(R.bool.encrypt);
		
		Logger.i(this.getClass().getSimpleName(), "Config DEBUG: " + String.valueOf(Cons.DEBUG));
		Logger.i(this.getClass().getSimpleName(), "Config SHOW_LOGS: " + String.valueOf(Cons.SHOW_LOGS));
		Logger.i(this.getClass().getSimpleName(), "Config FAKE_DATA: " + String.valueOf(Cons.FAKE_DATA));
		Logger.i(this.getClass().getSimpleName(), "Config TRACKER: " + String.valueOf(Cons.TRACKER));
		Logger.i(this.getClass().getSimpleName(), "Config ENCRYPT: " + String.valueOf(Cons.ENCRYPT));

		
	}
	
}
