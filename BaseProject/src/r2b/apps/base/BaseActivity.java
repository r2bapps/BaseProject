/*
 * BaseActivity
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

import java.util.List;

import r2b.apps.R;
import r2b.apps.base.BaseDialog.BaseDialogListener;
import r2b.apps.utils.BaseTracker;
import r2b.apps.utils.Cons;
import r2b.apps.utils.ITracker;
import r2b.apps.utils.Logger;
import r2b.apps.utils.SecurePreferences;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Wrapper for activity main functionality.
 * Manage fragment back stack, preferences, main click listener, a singleton dialog fragment.
 * Manage activity life cycle.
 */
public abstract class BaseActivity extends android.support.v4.app.FragmentActivity 
	implements BaseDialogListener {	
	
	/**
	 * Setup the initial back stack size as one fragment on the activity.
	 * Normally you do not need to change this param.
	 */
	private static final int INITIAL_BACK_STACK_SIZE = 1;
	
	/**
	 * The current fragment.
	 */
	private View.OnClickListener currentClickListener;
	/**
	 * The current fragment.
	 */
	private CallableBackFragment currentBackListener;
	/**
	 * The dialog fragment listener.
	 */
	private BaseDialogListener dialogListenerWrapper;
	/**
	 * Main click listener for the activity and all fragments.
	 */
	protected final OnClickListener clickListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			if(currentClickListener != null) {
				
				// XXX TRACKER
				getTracker().sendEvent(
						BaseTracker.CATEGORY.GUI.name(), 
						BaseTracker.ACTION.click.name(), 
						getResources().getResourceEntryName(v.getId()), 
						v.getId());
				
				currentClickListener.onClick(v);
			}
		}
	};
	
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
	 * Initialize all activity views. It is call on onResume.
	 * Used with findViewByid...
	 * Order of call on onResume 1.
	 */
	protected abstract void initViews();
			
	/**
	 * Initialize views values. It is call on onResume.
	 * Order of call on onResume 2.
	 */
	protected abstract void initValues();
	
	/**
	 * Initialize views listeners or other listeners. It is call on onResume.
	 * Order of call on onResume 3.
	 */
	protected abstract void initListeners();
	
	/**
	 * Initialize other things. It is call on onResume.
	 * Order of call on onResume 4.
	 */
	protected abstract void init();

	/**
	 * Remove listeners.
	 */
	protected abstract void removeListeners();
	
	/**
	 * Clear or closes all resources needed to close the activity.
	 */
	protected abstract void clear();
	
	
	/* (non-Javadoc)
	 * @see android.content.ContextWrapper#getSharedPreferences(java.lang.String, int)
	 */
	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		
		final SharedPreferences exit;
		
		if(Cons.ENCRYPT) {
			exit = SecurePreferences.getSecurePreferences(
					this,
					name);

		}
		else {
			exit = super.getSharedPreferences(name, mode);
			
			// XXX LOGGER
			Logger.i(this.getClass().getSimpleName(), "Init activity shared preferences on private mode.");
		}
		
		return exit;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindowFeatures();
		setContentView(getLayout());		
	}	
	
	/**
	 * Set the activity current click listener.
	 */
	public void setClickListener(OnClickListener currentClickListener) {
		this.currentClickListener = currentClickListener;
	}

	/**
	 * Set the activity current back listener.
	 */
	public void setBackListener(CallableBackFragment currentBackListener) {
		this.currentBackListener = currentBackListener;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		initViews();
		initValues();
		initListeners();
		init();
	}
	
	@Override
	protected void onPause() {
		removeListeners();
		clear();
		
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() > INITIAL_BACK_STACK_SIZE) {
			
			if(this.currentBackListener != null) {
				this.currentBackListener.onBackPressed();
			}
			
			super.onBackPressed();										
		} else {			
			clear();
			finish();
		}	
	}
	
	/**
	 * Switch between fragments.
	 * @param fragment The new fragment.
	 * @param tag The tag to identify the fragment.
	 * @param addToStack True to add to back stack, false otherwise.
	 */
	public void switchFragment(android.support.v4.app.Fragment fragment, String tag, boolean addToStack) {
		
		if (addToStack) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName())
					.commit();
			
			// XXX LOGGER
			Logger.i(this.getClass().getSimpleName(), "Add: " + tag + ", saving to stack");
			
		} else {
			getSupportFragmentManager().popBackStack();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName())
					.commit();
			
			// XXX LOGGER
			Logger.i(this.getClass().getSimpleName(), "Add: " + tag + ", without saving to stack");
		}		

	}
	
	/***
	 * Clear the back stack to its initial state.
	 * Normally with the first fragment setted when activity is created firstly.
	 */
	public void clearBackStack() {
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

	    while (fragmentManager.getBackStackEntryCount() > INITIAL_BACK_STACK_SIZE) {
	    	// If you use popBackStack you probably setup an infinite loop.
	        fragmentManager.popBackStackImmediate();
	    }
	    
	}
	
	/**
	 * Show a simple toast with short length.
	 * @param msg The message to show.
	 */
	public void showToast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Show a dialog fragment.
	 * @param dialog The dialog.
	 * @param listener The dialog listener if it is needed, null for not needed.
	 */
	public void showDialog(android.support.v4.app.DialogFragment dialog, BaseDialogListener listener) {
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null).commit();
	    
	    this.dialogListenerWrapper = listener;
	    
		dialog.show(getSupportFragmentManager(), "dialog");
	}
	
	/**
	 * Get the activity private shared preferences.
	 * If ENCRYPT mode is enable the preferences are encrypted.
	 * @return The activity shared preferences.
	 */
	public SharedPreferences getPreferences() {
		return getSharedPreferences(this.getClass().getSimpleName(), Context.MODE_PRIVATE);
	}
	
	/**
	 * Get the application tracker.
	 * @return The tracker.
	 */
	public ITracker getTracker() {
		return ((BaseApplication) getApplication()).getTracker();
	}
	
	@Override
	public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDialogPositiveClick(dialog);
			dialogListenerWrapper = null;
		}		
	}

	@Override
	public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDialogNegativeClick(dialog);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onDialogNeutralClick(android.support.v4.app.DialogFragment dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDialogNeutralClick(dialog);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onItemClick(android.support.v4.app.DialogFragment dialog, int which) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onItemClick(dialog, which);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onSelectedItems(android.support.v4.app.DialogFragment dialog, List<Integer> selectedItems) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onSelectedItems(dialog, selectedItems);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onCancel(dialog);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDismiss(dialog);
			dialogListenerWrapper = null;
		}
	}
	
	/**
	 * Add support to fragments to receive from activity back events.
	 */
	public interface CallableBackFragment {
		/**
		 * Back event.
		 */
		public void onBackPressed();
	};
	
}
