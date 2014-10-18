/*
 * BaseAdapter
 * 
 * 0.2
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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wrapper for base adapter main functionality.
 * This class uses View Holder Pattern.
 * @param <T> The item type of the list.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	/**
	 * Data.
	 */
	private List<T> items;	
	
	/**
	 * Get the layout of the item. 
	 * @return The layout.
	 */
	protected abstract int getLayout();
	
	/**
	 * Get new instance of the view holder initializing the views.
	 * @return The view holder.
	 * @param convertView The view where find views.
	 */
	protected abstract Object initViewHolder(View convertView);
	
	/**
	 * Setup the values of the view holder.
	 * @param context Parent context.
	 * @param item The item.
	 * @param position The position of the item.
	 * @param viewHolder The view holder where setup values.
	 */
	protected abstract void setViewHolderValues(Context context, T item, int position, Object viewHolder);
	
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
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		T item = getItem(position);
		
		if (convertView == null) {
			convertView = LayoutInflater.
					from(parent.getContext()).inflate(getLayout(), parent, false);
			// View Holder pattern
			Object viewHolder = initViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		setViewHolderValues(parent.getContext(), item, position, convertView.getTag());
		
		return convertView;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
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
