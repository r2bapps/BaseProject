package r2b.apps.base;

import r2b.apps.R;
import r2b.apps.view.base.BaseSplashActivity;
import android.content.pm.ActivityInfo;

public class Splash extends BaseSplashActivity {

	@Override
	protected int getLayout() {
		return R.layout.splash_act_test;
	}

	@Override
	protected Class<TestAct> getNextActivity() {
		return TestAct.class;
	}

	@Override
	protected void initWindowFeatures() {
		super.initWindowFeatures();
		// Set portrait orientation		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);        
	}
	
	

}
