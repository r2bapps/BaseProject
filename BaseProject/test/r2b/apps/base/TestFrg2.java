package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;

import r2b.apps.R;
import android.os.Bundle;
import android.view.View;

/**
 * A placeholder fragment containing a simple list view.
 */
public class TestFrg2 extends BaseAbsListFragment<Person> {	
	
	public static final String EMPTY_KEY = "empty";
	
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
	protected void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void click(View view) {
		// TODO Auto-generated method stub
		
	}
	
}
