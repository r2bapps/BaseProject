package r2b.apps.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import r2b.apps.R;
import r2b.apps.utils.cipher.AESCipher;
import r2b.apps.view.base.BaseDialog;
import r2b.apps.view.base.BaseDialog.BaseDialogListener;
import r2b.apps.view.base.BaseFragment;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestFrg extends BaseFragment {

	private Button btn, btnListFrg, btnListFrgEmpty;
	private TextView txt;
	private EditText etIn, etOut;

	private static final String TIME_KEY = "TIME_KEY";
	private long currentTime = -1;
	
	private final BaseDialogListener dialogListener = new BaseDialogListener() {

		@Override
		public void onDialogPositiveClick(BaseDialog dialog) {

			
		}

		@Override
		public void onDialogNegativeClick(BaseDialog dialog) {
			
		}

		@Override
		public void onDialogNeutralClick(BaseDialog dialog) {
			
		}

		@Override
		public void onItemClick(BaseDialog dialog, int which) {
			showToast("Item clicked: " + String.valueOf(which));
		}

		@Override
		public void onSelectedItems(BaseDialog dialog,
				List<Integer> selectedItems) {
			
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			
		}
		
	};
	
	@Override
	public void onClick(View view) {
		super.onClick(view);
		
		if(view.getId() == R.id.btnListFrg) {
			switchFragment(TestFrg2.newInstance(false), "test", true);
		} 
		else if(view.getId() == R.id.btnListFrgEmpty) {
			switchFragment(TestFrg2.newInstance(true), "test", true);
		} 
		else {
		
			txt.setText(((Button)view).getText().toString());
			currentTime = System.currentTimeMillis();
			showDialog(BaseDialog.newInstance(
					android.R.drawable.ic_dialog_info, 
					"Title", 
					"Message", 
					true, 
					android.R.string.ok, 
					android.R.string.cancel,  
					android.R.array.postalAddressTypes, 
					2),
					dialogListener);
			
			if(etIn.getText().toString() != null && !"".equals(etIn.getText().toString())) {
				etOut.setText(AESCipher.encrypt(etIn.getText().toString()));
			}
			else if(etOut.getText().toString() != null && !"".equals(etOut.getText().toString())) {
				etIn.setText(AESCipher.decrypt(etOut.getText().toString()));
			}
		
		}
	}

	@Override
	protected int getLayout() {
		return R.layout.frg_test;
	}

	@Override
	protected void initViews() {
		btn = (Button) getView().findViewById(R.id.btn);
		btnListFrg = (Button) getView().findViewById(R.id.btnListFrg);
		btnListFrgEmpty = (Button) getView().findViewById(R.id.btnListFrgEmpty);
		txt = (TextView) getView().findViewById(R.id.textView1);
		etIn = (EditText) getView().findViewById(R.id.etInput);
		etOut = (EditText) getView().findViewById(R.id.etOutput);
	}

	@Override
	protected void initValues() {
		btn.setText("Change text");
	}

	@Override
	protected void initListeners() {
		btn.setOnClickListener(this);
		btnListFrg.setOnClickListener(this);
		btnListFrgEmpty.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	protected void init() {
		showToast("Init fragment, current time:" + String.valueOf(currentTime));

		getFragmentPreferences().edit().putString("test", "test value").commit();
		Assert.assertEquals("test value", getFragmentPreferences().getString("test", "error"));
		
		getFragmentPreferences().edit().putInt("test1", 5).commit();
		Assert.assertEquals(5, getFragmentPreferences().getInt("test1", 0));
		
		getFragmentPreferences().edit().putLong("test2", 5l).commit();
		Assert.assertEquals(5l, getFragmentPreferences().getLong("test2", 0l));
		
		getFragmentPreferences().edit().putFloat("test3", 5.5f).commit();
		Assert.assertEquals(5.5f, getFragmentPreferences().getFloat("test3", 0.0f));
		
		getFragmentPreferences().edit().putBoolean("test4", true).commit();
		Assert.assertEquals(true, getFragmentPreferences().getBoolean("test4", false));
		
		Set<String> set = new HashSet<String>(2);
		set.add("hello");
		set.add("world");
		getFragmentPreferences().edit().putStringSet("test5", set).commit();
		
		set = getFragmentPreferences().getStringSet("test5", null);
		Assert.assertNotNull(set);
		Assert.assertEquals("hello", set.toArray()[0]);
		Assert.assertEquals("world", set.toArray()[1]);
	}

	@Override
	protected void removeListeners() {
		btn.setOnClickListener(null);
		btnListFrg.setOnClickListener(null);
		btnListFrgEmpty.setOnClickListener(null);
	}

	@Override
	protected void clear() {
		showToast("Closing fragment");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		showToast("Restoring currentTime");
		currentTime = savedInstanceState.getLong(TIME_KEY);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(TIME_KEY, currentTime);
		showToast("Saving currentTime");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		showToast("Back pressed on fragment.");
	}
	

}
