package r2b.apps.base;

import r2b.apps.utils.Logger;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


@TargetApi(11/*Build.VERSION_CODES.HONEYCOMB*/)
public abstract class BaseFragment extends Fragment {
	
	protected final OnClickListener clickListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			click(v.getId());
		}
	};
	
	protected abstract int getLayout();
	
	protected abstract void initViews();
	
	protected abstract void initValues();
	
	protected abstract void initListeners();
	
	protected abstract void init();

	protected abstract void removeListeners();
	
	protected abstract void close();
	
	protected abstract void onRestoreInstanceState(Bundle savedInstanceState);
	
	protected abstract void click(int id);
		
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
		
		initViews();
		initValues();
		initListeners();
		init();
	}
	
	@Override
	public void onPause() {
		removeListeners();
		close();
		
		super.onPause();
	}
	
	protected void switchFragment(Fragment fragment, String tag, boolean addToStack) {
		((BaseActivity) getActivity()).switchFragment(fragment, tag, addToStack);
	}
	
	/**
	 * Switch between child fragments inner the base fragment.
	 * @param fragment
	 * @param tag
	 * @param addToStack
	 * @param frgContainerId
	 */
	@TargetApi(17/*Build.VERSION_CODES.JELLY_BEAN_MR1*/)
	protected void switchChildFragment(Fragment fragment, String tag, boolean addToStack, int frgContainerId) {

		if (Build.VERSION.SDK_INT >= 17/*Build.VERSION_CODES.JELLY_BEAN_MR1*/) {
			if (addToStack) {
				getChildFragmentManager().beginTransaction()
						.add(frgContainerId, fragment, tag)
						.addToBackStack(fragment.getClass().getName()).commit();
				Logger.i(BaseActivity.class.getSimpleName(), "Add child: " + tag + ", saving to stack");
			} else {
				getChildFragmentManager().popBackStack();
				getChildFragmentManager().beginTransaction()
						.add(frgContainerId, fragment, tag)
						.addToBackStack(fragment.getClass().getName()).commit();
				Logger.i(BaseActivity.class.getSimpleName(), "Add child: " + tag + ", without saving to stack");
			}			
		}
		else {
			if (addToStack) {
				getFragmentManager().beginTransaction()
						.add(frgContainerId, fragment, tag)
						.addToBackStack(fragment.getClass().getName()).commit();
				Logger.i(BaseActivity.class.getSimpleName(), "Add: " + tag + ", saving to stack");
			} else {
				getFragmentManager().popBackStack();
				getFragmentManager().beginTransaction()
						.add(frgContainerId, fragment, tag)
						.addToBackStack(fragment.getClass().getName()).commit();
				Logger.i(BaseActivity.class.getSimpleName(), "Add: " + tag + ", without saving to stack");
			}			
		}

	}	
	
}
