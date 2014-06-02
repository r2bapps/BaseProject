package r2b.apps.base.support;

import r2b.apps.base.ClickableFragment;
import r2b.apps.utils.Logger;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public abstract class SupportBaseFragment extends Fragment implements ClickableFragment {
	
	protected OnClickListener clickListener;
	
	protected abstract int getLayout();
	
	protected abstract void initViews();
	
	protected abstract void initValues();
	
	protected abstract void initListeners();
	
	protected abstract void init();

	protected abstract void removeListeners();
	
	protected abstract void close();
	
	protected abstract void onRestoreInstanceState(Bundle savedInstanceState);
	
	public abstract void click(View view);
		
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
		
		this.clickListener = ((SupportBaseActivity) getActivity()).clickListener;
		
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
	
	protected void switchFragment(Fragment fragment, String tag, boolean addToStack) {
		((SupportBaseActivity) getActivity()).switchFragment(fragment, tag, addToStack);
	}
	
	/**
	 * Switch between child fragments inner the base fragment.
	 * @param fragment
	 * @param tag
	 * @param addToStack
	 * @param frgContainerId
	 */
	protected void switchChildFragment(Fragment fragment, String tag, boolean addToStack, int frgContainerId) {

		if (addToStack) {
			getChildFragmentManager().beginTransaction()
					.add(frgContainerId, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(SupportBaseActivity.class.getSimpleName(), "Add child: " + tag + ", saving to stack");
		} else {
			getChildFragmentManager().popBackStack();
			getChildFragmentManager().beginTransaction()
					.add(frgContainerId, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(SupportBaseActivity.class.getSimpleName(), "Add child: " + tag + ", without saving to stack");
		}

	}	
	
	protected void showToast(final String msg) {
		((SupportBaseActivity) getActivity()).showToast(msg);
	}
	
	protected void showDialog(DialogFragment dialog) {
		((SupportBaseActivity) getActivity()).showDialog(dialog);
	}

	protected void hideDialog() {	    
		((SupportBaseActivity) getActivity()).hideDialog();
	}	
	
}
