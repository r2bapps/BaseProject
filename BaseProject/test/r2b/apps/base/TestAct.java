package r2b.apps.base;

import r2b.apps.R;
import r2b.apps.view.base.BaseActivity;
import android.os.Bundle;

public class TestAct extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			switchFragment(new TestFrg(), "main", false, true);
		}

	}	
	
	@Override
	protected int getLayout() {
		return R.layout.act_base;
	}
	
	@Override
	protected void initViews() {
		
	}

	@Override
	protected void initValues() {
		
	}

	@Override
	protected void initListeners() {
		
	}

	@Override
	protected void init() {
	
	}

	@Override
	protected void removeListeners() {
		
	}

	@Override
	protected void clear() {
		
	}



}
