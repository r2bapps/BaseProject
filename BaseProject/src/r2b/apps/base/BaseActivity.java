package r2b.apps.base;

import java.util.List;

import r2b.apps.R;
import r2b.apps.base.BaseDialog.BaseDialogListener;
import r2b.apps.utils.Logger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity 
	implements BaseDialogListener {	
	
	private ClickableFragment currentFragment; // TODO UPDATE ON BACK AND ON CAME FROM BACKGROUND??
	
	private BaseDialogListener dialogListenerWrapper;
	
	protected SharedPreferences preferences;
	
	protected final OnClickListener clickListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			if(currentFragment != null) {
				currentFragment.click(v);
			}
		}
	};
	
	protected void initWindowFeatures() { }
	
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
		
		preferences = getSharedPreferences(
				this.getClass().getSimpleName(), 
				Activity.MODE_PRIVATE);
	}	
	
	public OnClickListener getClickListener() {
		return clickListener;
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
	
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
			getSupportFragmentManager().popBackStack();
			Logger.i(BaseActivity.class.getSimpleName(), "PopBackStack");
		} else {
			super.onBackPressed();
		}	
	}
	
	public void switchFragment(android.support.v4.app.Fragment fragment, String tag, boolean addToStack) {

		if(fragment instanceof ClickableFragment) {
			currentFragment = (ClickableFragment) fragment;
		}
		
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
	
	public void showToast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
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
	
	public SharedPreferences getPreferences() {
		return preferences;
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDialogPositiveClick(dialog);
			dialogListenerWrapper = null;
		}		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDialogNegativeClick(dialog);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onDialogNeutralClick(DialogFragment dialog) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onDialogNeutralClick(dialog);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onItemClick(DialogFragment dialog, int which) {
		if(dialogListenerWrapper != null) {
			dialogListenerWrapper.onItemClick(dialog, which);
			dialogListenerWrapper = null;
		}
	}

	@Override
	public void onSelectedItems(DialogFragment dialog, List<Integer> selectedItems) {
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
	
}
