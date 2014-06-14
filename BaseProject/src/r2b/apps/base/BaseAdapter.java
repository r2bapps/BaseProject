/*
 * BaseAdapter
 * 
 * 0.1
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

package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	/**
	 * Data.
	 */
	private List<T> items;	
	
	
	protected abstract int getLayout();
	protected abstract void initViewsAndValues(T item, int position, View convertView);
	
	/**
	 * Build adapter with the list items.
	 * @param items The items list.
	 */
	public BaseAdapter(final List<T> items) {
		super();
		
		if (items == null) {
			this.items = new ArrayList<T>(0);
		}
		else {
			this.items = new ArrayList<T>(items.size());
			this.items.addAll(items);
		}
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		T item = getItem(position);
		
		if (convertView == null) {
			convertView = LayoutInflater.
					from(parent.getContext()).inflate(getLayout(), parent, false);
		}
		
		initViewsAndValues(item, position, convertView);
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public T getItem(int position) {
		return items.get(position);
	}
	
	/**
	 * Update adapter items.
	 * @param items the items list.
	 */
	public void update(final List<T> items) {
		this.items.clear();
		
		if (items != null) {
			this.items.addAll(items);
		}
		
		notifyDataSetChanged();		
	}
	
}
