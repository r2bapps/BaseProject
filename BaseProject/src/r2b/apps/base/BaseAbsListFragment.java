package r2b.apps.base;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;


public abstract class BaseAbsListFragment<T> extends BaseFragment {
	
	/**
	 * Key to store scroll offset when app goes to background.
	 */
	protected static final String SCROLL_OFFSET_KEY = "SCROLL_OFFSET_KEY";
	
	/**
	 * View. 
	 */ 
	protected AbsListView absListView;
	protected TextView empty;
	
	/**
	 * Adapter.
	 */
	protected r2b.apps.base.BaseAdapter<T> adapter;
	
	/** 
	 * Scroll received to apply. 
	 */
	protected int scrollY;
	
	protected final OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			itemClick(view, position, id);
		}	
	};
	
	protected abstract void itemClick(View view, int position, long id);
	
	protected abstract r2b.apps.base.BaseAdapter<T> initAdapter(final List<T> list);
	
	protected abstract List<T> loadData();
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save the scroll
	  savedInstanceState.putInt(SCROLL_OFFSET_KEY, absListView.getFirstVisiblePosition());
	}

	/**
	 * Data returned from background needed to restore.
	 * @param savedInstanceState
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(SCROLL_OFFSET_KEY)) {
			scrollY = savedInstanceState.getInt(SCROLL_OFFSET_KEY);	
		}
	}
	
	protected void initViews() { 
		absListView = (AbsListView) getView().findViewById(android.R.id.list);
		empty = (TextView) getView().findViewById(android.R.id.empty);
		absListView.setEmptyView(empty);
	} 
	
	protected void initValues() {
		
		final List<T> list = loadData();
		
		if(adapter == null) {
			adapter = initAdapter(list);
		}			
		
		if(absListView instanceof ListView) {
			((ListView) absListView).setAdapter(adapter);	
		}
		else {
			((GridView) absListView).setAdapter(adapter);
		}
		
	}
	
	protected void initListeners() {
		absListView.setOnItemClickListener(itemClickListener);
	}
	
	protected void init() {		
		if(scrollY != 0) {
			absListView.smoothScrollToPosition(scrollY);
			scrollY = 0;
		}		
	}
	
}
