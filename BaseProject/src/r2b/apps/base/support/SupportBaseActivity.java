package r2b.apps.base.support;

import java.util.List;

import r2b.apps.R;
import r2b.apps.base.ClickableFragment;
import r2b.apps.base.support.SupportBaseDialog.SupportBaseDialogListener;
import r2b.apps.utils.Logger;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class SupportBaseActivity extends FragmentActivity 
	implements ClickableFragment, SupportBaseDialogListener {	
	
	private ClickableFragment currentFragment;
	
	final OnClickListener clickListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			currentFragment.click(v);
		}
	};
	
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
			Logger.i(SupportBaseActivity.class.getSimpleName(), "PopBackStack");
		} else {
			super.onBackPressed();
		}
	}

	protected void switchFragment(Fragment fragment, String tag, boolean addToStack) {

		if(fragment instanceof ClickableFragment) {
			currentFragment = (ClickableFragment) fragment;
		}
		
		if (addToStack) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(SupportBaseActivity.class.getSimpleName(), "Add: " + tag + ", saving to stack");
		} else {
			getSupportFragmentManager().popBackStack();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment, tag)
					.addToBackStack(fragment.getClass().getName()).commit();
			Logger.i(SupportBaseActivity.class.getSimpleName(), "Add: " + tag + ", without saving to stack");
		}

	}
	
	protected void showToast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	protected void showDialog(DialogFragment dialog) {
	    // Will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null).commit();
	    
		dialog.show(getSupportFragmentManager(), "dialog");
	}

	protected void hideDialog() {
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev)
	        	.addToBackStack(null)
	        	.commit();
	    }
	    
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onDialogNeutralClick(DialogFragment dialog) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onItemClick(DialogFragment dialog, int which) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onSelectedItems(DialogFragment dialog, List<Integer> selectedItems) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub		
	}
	
}
