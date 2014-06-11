package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	/**
	 * Data.
	 */
	private List<T> items;	
	
	/**
	 * Build adapter with the list items.
	 * The modifying of external list has no effect on adapter.
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
	public int getCount() {
		return items.size();
	}

	@Override
	public T getItem(int position) {
		return items.get(position);
	}
	
	/**
	 * Update adapter items.
	 * The modifying of external list has no effect on adapter.
	 * @param items the items list.
	 */
	public void update(final List<T> items) {
		this.items.clear();
		
		if (items == null) {
			this.items = new ArrayList<T>(0);
		}
		else {
			this.items = new ArrayList<T>(items.size());
			this.items.addAll(items);
		}
		
		notifyDataSetChanged();		
	}
	
}
