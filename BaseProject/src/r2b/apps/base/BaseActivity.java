package r2b.apps.base;

import r2b.apps.R;
import r2b.apps.utils.Logger;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

@TargetApi(11/*Build.VERSION_CODES.HONEYCOMB*/)
public abstract class BaseActivity extends Activity {	
	
	protected abstract void initWindowFeatures();
	
	protected abstract void initViews();
			
	protected abstract void initValues();
	
	protected abstract void initListeners();
	
	protected abstract void init();

	protected abstract void removeListeners();
	
	protected abstract void close();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindowFeatures();
		setContentView(R.layout.act_base);
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
		close();
		
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() > 1) {
			getFragmentManager().popBackStack();
			Logger.i(BaseActivity.class.getSimpleName(), "PopBackStack");
		} else {
			super.onBackPressed();
		}
	}

	protected void switchFragment(Fragment fragment, String tag, boolean addToStack) {

		if (addToStack) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(BaseActivity.class.getSimpleName(), "Add: " + tag + ", saving to stack");
		} else {
			getFragmentManager().popBackStack();
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(BaseActivity.class.getSimpleName(), "Add: " + tag + ", without saving to stack");
		}

	}
	
}
