/*
 * BaseFragment
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

package r2b.apps.view.base;

import r2b.apps.utils.Cons;
import r2b.apps.utils.cipher.SecurePreferences;
import r2b.apps.utils.logger.Logger;
import r2b.apps.utils.tracker.ITracker;
import r2b.apps.view.base.BaseDialog.BaseDialogListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wrapper for the main functionality of the fragment.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment 
	implements View.OnClickListener, BaseActivity.CallableBackFragment {
	
	/**
	 * Get the layout to show.
	 * @return The fragment layout.
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
	 * Clear or closes all resources needed to close the fragment.
	 */
	protected abstract void clear();
	
	/**
	 * Restore state when came from background.
	 * @param savedInstanceState The state bundle.
	 */
	protected void onRestoreInstanceState(Bundle savedInstanceState) { }

	/**
	 * Receive calls for click listeners and main click listener from activity.
	 */
	@Override
	public void onClick(View view) { }
		
	/**
	 * Receive calls for back event from activity.
	 */
	@Override
	public void onBackPressed() { }

	/**
	 * Get the shared preferences.
	 * @param context The activity.
	 * @param name The name of the file.
	 * @param mode The mode.
	 * @return Shared preferences or encrypted shared preferences.
	 */
	private SharedPreferences getSharedPreferences(Context context, String name, int mode) {
		
		final SharedPreferences exit;
		
		if(Cons.ENCRYPT) {
			exit = SecurePreferences.getSecurePreferences(
					context,
					name);
			
		}
		else {
			exit = context.getSharedPreferences(name, mode);
			
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), "Init fragment shared preferences on private mode.");
		}
		
		return exit;
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		
		if(savedInstanceState != null) {
			onRestoreInstanceState(savedInstanceState);
		}
		
		// Inflate the layout for this fragment
        final View v = inflater.inflate(getLayout(), container, false);	
		
		return v;
    } 
	
	@Override
	public void onResume() {
		super.onResume();
		
		((BaseActivity) getActivity()).setClickListener(this);
		((BaseActivity) getActivity()).setBackListener(this);

		getTracker().sendScreenName(this.getClass().getSimpleName());
		
		initViews();
		initValues();
		initListeners();
		init();
	}
	
	@Override
	public void onPause() {
		
		((BaseActivity) getActivity()).setClickListener(null);
		((BaseActivity) getActivity()).setBackListener(null);
		
		removeListeners();
		clear();
		
		super.onPause();
	}	
	
	/**
	 * Switch between fragments.
	 * @param fragment The new fragment.
	 * @param tag The tag to identify the fragment.
	 * @param addToStack True to add to back stack, false otherwise.
	 */
	protected void switchFragment(android.support.v4.app.Fragment fragment, String tag, boolean addToStack) {
		((BaseActivity) getActivity()).switchFragment(fragment, tag, addToStack);
	}
	
	/***
	 * Clear the back stack to its initial state.
	 * Normally with the first fragment setted when activity is created firstly.
	 */
	protected void clearBackStack() {
		((BaseActivity) getActivity()).clearBackStack();
	}
	
	/**
	 * Switch between child fragments inner the base fragment.
	 * WARNING: NOT TESTED!!
	 * @param fragment
	 * @param tag
	 * @param addToStack
	 * @param frgContainerId
	 */
	protected void switchChildFragment(android.support.v4.app.Fragment fragment, String tag, boolean addToStack, int frgContainerId) {

		if (addToStack) {
			getChildFragmentManager().beginTransaction()
					.add(frgContainerId, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), "Add child: " + tag + ", saving to stack");
		} else {
			getChildFragmentManager().popBackStack();
			getChildFragmentManager().beginTransaction()
					.add(frgContainerId, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), "Add child: " + tag + ", without saving to stack");
		}

	}	
	
	/**
	 * Show a simple toast with short length.
	 * @param msg The message to show.
	 */
	protected void showToast(final String msg) {
		((BaseActivity) getActivity()).showToast(msg);
	}
	
	/**
	 * Show a dialog fragment.
	 * @param dialog The dialog.
	 * @param listener The dialog listener if it is needed, null for not needed.
	 */
	protected void showDialog(android.support.v4.app.DialogFragment dialog, BaseDialogListener listener) {
		((BaseActivity) getActivity()).showDialog(dialog, listener);
	}	
	
	/**
	 * Get the fragment private shared preferences.
	 * If ENCRYPT mode is enable the preferences are encrypted.
	 * @return The fragment shared preferences.
	 */
	protected SharedPreferences getFragmentPreferences() {
		return getSharedPreferences(getActivity(), this.getClass().getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * Get the activity private shared preferences.
	 * If ENCRYPT mode is enable the preferences are encrypted.
	 * @return The activity shared preferences.
	 */
	protected SharedPreferences getActivityPreferences() {
		return ((BaseActivity) getActivity()).getPreferences();
	}
	
	/**
	 * Get the application tracker.
	 * @return The tracker.
	 */
	public ITracker getTracker() {
		return ((BaseActivity) getActivity()).getTracker();
	}
	
}
