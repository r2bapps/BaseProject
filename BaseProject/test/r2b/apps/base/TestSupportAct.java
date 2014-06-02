package r2b.apps.base;

import r2b.apps.R;
import r2b.apps.base.support.SupportBaseActivity;
import r2b.apps.base.support.SupportBaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TestSupportAct extends SupportBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			switchFragment(new PlaceholderFragment(), "main", true);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends SupportBaseFragment {

		private Button btn;
		private TextView txt;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.support_frg_test,
					container, false);
			return rootView;
		}

		@Override
		public void click(View view) {
			txt.setText(((Button)view).getText().toString());
		}

		@Override
		protected int getLayout() {
			// TODO Auto-generated method stub
			return 0;
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
			showToast("Init fragment");
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
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void click(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initWindowFeatures() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void close() {
		// TODO Auto-generated method stub
		
	}

}
