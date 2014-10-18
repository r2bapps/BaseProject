/*
 * BaseAbsListFragment
 * 
 * 0.3
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.view.base;

import java.util.List;

import r2b.apps.utils.tracker.BaseTracker;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;	
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Wrapper for AbsList main functionality.
 * @param <T> The item type to show.
 */
public abstract class BaseAbsListFragment<T> extends BaseFragment {
	
	/**
	 * Key to store scroll offset when app goes to background.
	 */
	protected static final String SCROLL_OFFSET_KEY = "SCROLL_OFFSET_KEY";
	
	/**
	 * View. 
	 */ 
	protected AbsListView absListView;
	protected View emptyView;
	protected View errorView;
	protected View loadingView;	
	
	/**
	 * Adapter.
	 */
	protected r2b.apps.view.base.BaseAdapter<T> adapter;
	
	/** 
	 * Scroll received to apply. 
	 */
	protected int scrollY; 
	
	/**
	 * List/Grid item click.
	 */
	protected final OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			
			// XXX TRACKER
			performOnItemClickTracking(adapter, view, position, id);
			
			itemClick(view, position, id);
		}	
	};
	
	/**
	 * The list list/grid item clicked.
	 * @param view The view clicked
	 * @param position the position of this view.
	 * @param id The id of the item on the view.
	 */
	protected abstract void itemClick(View view, int position, long id);
	
	/**
	 * Build and set info on the adapter.
	 * @param list The info list.
	 * @return The adapter populated.
	 */
	protected abstract r2b.apps.view.base.BaseAdapter<T> initAdapter(final List<T> list);
	
	/**
	 * Load the info list to the adapter.
	 * @return The info list.
	 */
	protected abstract List<T> loadData();
	
	/**
	 * The list/grid empty.
	 * @return The empty view.
	 */
	protected abstract View getEmptyView();
	
	/**
	 * The list/grid error.
	 * @return The error view.
	 */
	protected abstract View getErrorView();
	
	/**
	 * The list/grid loading.
	 * @return The loading view.
	 */
	protected abstract View getLoadingView();
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save the scroll
	  savedInstanceState.putInt(SCROLL_OFFSET_KEY, absListView.getFirstVisiblePosition());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(SCROLL_OFFSET_KEY)) {
			scrollY = savedInstanceState.getInt(SCROLL_OFFSET_KEY);	
		}
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.base.BaseFragment#initViews()
	 */
	@Override
	protected void initViews() { 
		absListView = (AbsListView) getView().findViewById(android.R.id.list);
		
		emptyView = getEmptyView();
		errorView = getErrorView();
		loadingView = getLoadingView();
		
		RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		RelativeLayout rl = new RelativeLayout(getActivity());
		rl.setLayoutParams(lp);
		
		if (emptyView != null) {
			rl.addView(emptyView);
		}
		if (errorView != null) {
			rl.addView(errorView);		
		}
		if (loadingView != null) {
			rl.addView(loadingView);
		}

		ViewGroup parent = (ViewGroup) absListView.getParent();
		parent.addView(rl);
		absListView.setEmptyView(rl);
		
		showEmptyView();
	} 
	
	/* (non-Javadoc)
	 * @see r2b.apps.base.BaseFragment#initValues()
	 */
	@Override
	protected void initValues() {
		
		final List<T> list = loadData();
		
		if(adapter == null) {
			adapter = initAdapter(list);
		}			
		else {
			((BaseAdapter<T>) adapter).update(list);
		}
		
		if(absListView instanceof ListView) {
			((ListView) absListView).setAdapter(adapter);	
		}
		else {
			((GridView) absListView).setAdapter(adapter);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.base.BaseFragment#initListeners()
	 */
	@Override
	protected void initListeners() {
		absListView.setOnItemClickListener(itemClickListener);
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.base.BaseFragment#init()
	 */
	@Override
	protected void init() {		
		if(scrollY != 0) {
			absListView.smoothScrollToPosition(scrollY);
			scrollY = 0;
		}		
	}
	
	/**
	 * Show empty view.
	 */
	protected void showEmptyView() {
		if(emptyView != null) {
			emptyView.setVisibility(View.VISIBLE);
		}
		if(errorView != null) {
			errorView.setVisibility(View.GONE);
		}
		if(loadingView != null) {
			loadingView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Show error view.
	 */
	protected void showErrorView() {
		if(emptyView != null) {
			emptyView.setVisibility(View.GONE);
		}
		if(errorView != null) {
			errorView.setVisibility(View.VISIBLE);
		}
		if(loadingView != null) {
			loadingView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Show loading view.
	 */
	protected void showLoadingView() {
		if(emptyView != null) {
			emptyView.setVisibility(View.GONE);
		}
		if(errorView != null) {
			errorView.setVisibility(View.GONE);
		}
		if(loadingView != null) {
			loadingView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Tracker action performed: onItemClick.
	 * @param adapter
	 * @param view
	 * @param position
	 * @param id
	 */
	private void performOnItemClickTracking(AdapterView<?> adapter, View view, int position, long id) {
		String item_click = BaseTracker.ACTION.grid_item_click.name();
		if(absListView instanceof ListView) {
			item_click = BaseTracker.ACTION.list_item_click.name();
		}
		
		String label;
		try {
			label = getResources().getResourceEntryName(view.getId());
		} catch(Resources.NotFoundException e) {
			label = "no_xml_id";
		}
		
		getTracker().sendEvent(
				BaseTracker.CATEGORY.GUI.name(), 
				item_click,
				label,
				position);		
	}
	
}
