package r2b.apps.base;

import java.util.List;

import r2b.apps.R;
import r2b.apps.view.base.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class PersonAdapter extends BaseAdapter<Person> {

	public PersonAdapter(List<Person> items) {
		super(items);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	protected int getLayout() {
		return R.layout.list_frg_item_test;
	}

	@Override
	protected Object initViewHolder(View convertView) {
		PersonHolder holder = new PersonHolder();
		
		TextView txt = (TextView) convertView.findViewById(R.id.person);
		holder.txt = txt;
			
		return holder;
	}
	
	@Override
	protected void setViewHolderValues(Context context, Person item, int position, Object viewHolder) {
		PersonHolder holder = (PersonHolder) viewHolder;		
		holder.txt.setText(item.getName());
	}
	
	private static class PersonHolder {
		TextView txt;
	};

}
