package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import r2b.apps.R;
import r2b.apps.view.base.BaseAbsListFragment;
import r2b.apps.view.base.BaseAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple list view.
 */
public class TestFrg2 extends BaseAbsListFragment<Person> {	
	
	public static final String EMPTY_KEY = "empty";
	
	private TextView empty;
	private TextView error;
	private View loading;
	
	private boolean exit = false;
	
	public static TestFrg2 newInstance(boolean empty) {
		TestFrg2 f = new TestFrg2();
		Bundle b = new Bundle();
		b.putBoolean(EMPTY_KEY, empty);
		f.setArguments(b);
		return f;
	}	

	@Override
	protected int getLayout() {
		return R.layout.list_frg_test;
	}

	@Override
	protected void itemClick(View view, int position, long id) {
		showToast("Item clicked position: " + String.valueOf(position) + ", id: " + String.valueOf(id));
	}

	@Override
	protected BaseAdapter<Person> initAdapter(List<Person> list) {
		return new PersonAdapter(list);
	}

	@Override
	protected List<Person> loadData() {
		
		if(getArguments().containsKey(EMPTY_KEY)
				&& getArguments().getBoolean(EMPTY_KEY)) {
			return new ArrayList<Person>(0);	
		}
		else {
			return Person.getPeople();	
		}
		
	}

	@Override
	protected void removeListeners() {
		this.absListView.setOnItemClickListener(null);
	}

	@Override
	protected void clear() {
		exit = true;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		showToast("Back pressed on fragment.");
	}

	@Override
	protected View getEmptyView() {
		return empty;
	}

	@Override
	protected View getErrorView() {
		return error;
	}

	@Override
	protected View getLoadingView() {
		return loading;
	}

	@Override
	protected void initViews() {
		empty = new TextView(getActivity());	
		empty.setText("Empty");
		
		error = new TextView(getActivity());	
		error.setText("Error");
		
		loading = getActivity().getLayoutInflater().inflate(R.layout.empty_test, null);
		
		super.initViews();
	}

	@Override
	protected void init() {
		super.init();
		
		Thread t = new Thread() {
			@Override
			public void run() {

				while(!exit) {
				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(getActivity() != null) {
						getActivity().runOnUiThread(new Runnable() {						
							@Override
							public void run() {
								if(System.currentTimeMillis() % 2 == 0) {
									showErrorView();
								}
								else {
									//setEmptyView();
									showLoadingView();
								}
							}
						});
					}
					
				}
				
			}
			
		};
		t.start();
	}

	
}
