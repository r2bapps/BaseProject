package r2b.apps.base;

import r2b.apps.R;
import r2b.apps.utils.Logger;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {	
	
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
		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
			getSupportFragmentManager().popBackStack();
			Logger.i(BaseActivity.class.getSimpleName(), "PopBackStack");
		} else {
			super.onBackPressed();
		}
	}

	protected void switchFragment(Fragment fragment, String tag, boolean addToStack) {

		if (addToStack) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(BaseActivity.class.getSimpleName(), "Add: " + tag + ", saving to stack");
		} else {
			getSupportFragmentManager().popBackStack();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(BaseActivity.class.getSimpleName(), "Add: " + tag + ", without saving to stack");
		}

	}
	
}
