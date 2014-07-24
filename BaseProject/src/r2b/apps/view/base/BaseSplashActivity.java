/*
 * BaseSplashActivity
 * 
 * 0.1
 * 
 * 2014/07/04
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

package r2b.apps.view.base;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * Wrapper for splash main functionality.
 * Setup a splash with no title, and a delay to jump to main activity.
 */
public abstract class BaseSplashActivity extends Activity {
	 
    /**
     *  Set the duration of the splash screen.
     */
    private static final int SPLASH_SCREEN_DELAY = 3000; // Milliseconds
 
	/**
	 * Initialize features.
	 * Called previously setContent().
	 */
	protected void initWindowFeatures() { }
	
	/**
	 * Get the layout to show.
	 * @return The activity layout.
	 */
	protected abstract int getLayout();

	/**
	 * Get the next activity to show.
	 * @return The activity class.
	 */
	protected abstract Class<? extends FragmentActivity> getNextActivity();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initWindowFeatures();
        setContentView(getLayout());
        
        final Context context = this;
 
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the next activity
                Intent mainIntent = new Intent().setClass(context, getNextActivity());
                startActivity(mainIntent);                
 
                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };
 
        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
 
}
