package r2b.apps.base;

import java.util.List;

import r2b.apps.R;
import r2b.apps.base.BaseDialog.BaseDialogListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestFrg extends BaseFragment {

	private Button btn;
	private TextView txt;

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
		
		getTracker().sendEvent("GUI", "click", "R.id.btn", 0);
		
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
	}

	@Override
	protected int getLayout() {
		return R.layout.support_frg_test;
	}

	@Override
	protected void initViews() {
		btn = (Button) getView().findViewById(R.id.btn);
		txt = (TextView) getView().findViewById(R.id.textView1);
	}

	@Override
	protected void initValues() {
		btn.setText("Change text");
	}

	@Override
	protected void initListeners() {
		btn.setOnClickListener(clickListener);
	}

	@Override
	protected void init() {
		showToast("Init fragment, current time:" + String.valueOf(currentTime));
	}

	@Override
	protected void removeListeners() {
		btn.setOnClickListener(null);
	}

	@Override
	protected void close() {
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
