package r2b.apps.base;

import java.util.List;

import r2b.apps.R;
import r2b.apps.base.BaseDialog.BaseDialogListener;
import r2b.apps.utils.AESCipher;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
			showToast("Item clicked: " + String.valueOf(which));
		}

		@Override
		public void onSelectedItems(DialogFragment dialog,
				List<Integer> selectedItems) {
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
		
	};
	
	@Override
	public void click(View view) {
		
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
				0, 
				0, 
				android.R.string.ok, 
				android.R.string.cancel, 
				android.R.string.unknownName, 
				android.R.array.postalAddressTypes, 0, 0),
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
		btn.setOnClickListener(clickListener);
		btnListFrg.setOnClickListener(clickListener);
		btnListFrgEmpty.setOnClickListener(clickListener);
	}

	@Override
	protected void init() {
		showToast("Init fragment, current time:" + String.valueOf(currentTime));
		
		AESCipher.init(getActivity());
	}

	@Override
	protected void removeListeners() {
//		btn.setOnClickListener(null);
//		btnListFrg.setOnClickListener(null);
//		btnListFrgEmpty.setOnClickListener(null);
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
	
}