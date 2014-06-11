package r2b.apps.base;

import r2b.apps.base.BaseDialog.BaseDialogListener;
import r2b.apps.utils.ITracker;
import r2b.apps.utils.Logger;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragment extends android.support.v4.app.Fragment implements ClickableFragment {
	
	protected OnClickListener clickListener;
	
	protected SharedPreferences preferences;
	
	protected abstract int getLayout();
	
	protected abstract void initViews();
	
	protected abstract void initValues();
	
	protected abstract void initListeners();
	
	protected abstract void init();

	protected abstract void removeListeners();
	
	protected abstract void close();
	
	protected void onRestoreInstanceState(Bundle savedInstanceState) { }

	public abstract void click(View view);
		
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		preferences = activity.getSharedPreferences(
				this.getClass().getSimpleName(), 
				Activity.MODE_PRIVATE);
		Logger.i(this.getClass().getSimpleName(), "Init fragment shared preferences on private mode.");
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
		
		this.clickListener = ((BaseActivity) getActivity()).getClickListener();

		getTracker().sendScreenName(this.getClass().getSimpleName());
		
		initViews();
		initValues();
		initListeners();
		init();
	}
	
	@Override
	public void onPause() {
		
		this.clickListener = null;
		
		removeListeners();
		close();
		
		super.onPause();
	}	
	
	protected void switchFragment(android.support.v4.app.Fragment fragment, String tag, boolean addToStack) {
		((BaseActivity) getActivity()).switchFragment(fragment, tag, addToStack);
	}
	
	/**
	 * Switch between child fragments inner the base fragment.
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
			Logger.i(this.getClass().getSimpleName(), "Add child: " + tag + ", saving to stack");
		} else {
			getChildFragmentManager().popBackStack();
			getChildFragmentManager().beginTransaction()
					.add(frgContainerId, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(this.getClass().getSimpleName(), "Add child: " + tag + ", without saving to stack");
		}

	}	
	
	protected void showToast(final String msg) {
		((BaseActivity) getActivity()).showToast(msg);
	}
	
	protected void showDialog(DialogFragment dialog, BaseDialogListener listener) {
		((BaseActivity) getActivity()).showDialog(dialog, listener);
	}	
	
	protected SharedPreferences getPreferences() {
		return preferences;
	}

	protected SharedPreferences getActivityPreferences() {
		return ((BaseActivity) getActivity()).getPreferences();
	}
	
	public ITracker getTracker() {
		return ((BaseActivity) getActivity()).getTracker();
	}
	
}
