package r2b.apps.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import r2b.apps.R;
import r2b.apps.utils.BaseTracker;
import r2b.apps.utils.Cons;
import r2b.apps.utils.ITracker;
import r2b.apps.utils.Logger;
import android.app.Application;
import android.content.res.Resources.NotFoundException;

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

	}

	public ITracker getTracker() {
		return tracker;
	}
	
	/**
	 * Init Cons constants from base_config properties file.
	 */
	private void initConfig() {
		final Properties config = loadProperties(R.raw.base_config);
				
		Cons.DEBUG = Boolean.parseBoolean(config.getProperty("baseDebug"));
		Cons.SHOW_LOGS = Cons.DEBUG && Boolean.parseBoolean(config.getProperty("baseLogInfo"));
		Cons.FAKE_DATA = Cons.DEBUG && Boolean.parseBoolean(config.getProperty("baseFakeData"));
		Cons.TRACKER = Boolean.parseBoolean(config.getProperty("baseTrackApp"));
		
		Logger.i(this.getClass().getSimpleName(), "Config DEBUG: " + String.valueOf(Cons.DEBUG));
		Logger.i(this.getClass().getSimpleName(), "Config SHOW_LOGS: " + String.valueOf(Cons.SHOW_LOGS));
		Logger.i(this.getClass().getSimpleName(), "Config FAKE_DATA: " + String.valueOf(Cons.FAKE_DATA));
		Logger.i(this.getClass().getSimpleName(), "Config TRACKER: " + String.valueOf(Cons.TRACKER));
		
		config.clear();
		
	}
	
	/**
	 * Read from the /res/raw directory
	 * @param propertiesFileResId
	 * @return
	 */
	private Properties loadProperties(final int propertiesFileResId) {	
		Properties properties = new Properties();
		InputStream rawResource = null;
		try {
		    rawResource = getResources().openRawResource(propertiesFileResId);		    
		    properties.load(rawResource);		    
		} catch (NotFoundException | IOException e) {
			Logger.e(this.getClass().getSimpleName(), "Can't read app config properties", e);
			throw new RuntimeException(e);
		} finally {
			if(rawResource != null) {
				try {
					rawResource.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
}
