package r2b.apps.base;

import android.os.Bundle;

public class TestAct extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			switchFragment(new TestFrg(), "main", true);
		}

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
