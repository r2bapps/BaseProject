package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	/**
	 * Data.
	 */
	private final List<T> items;	
	
	public BaseAdapter(final List<T> items) {
		super();
		
		if (items == null) {
			this.items = new ArrayList<T>(0);
		}
		else {
			this.items = items;
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
	
}
