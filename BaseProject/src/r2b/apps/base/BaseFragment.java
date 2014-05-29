package r2b.apps.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
	
}
