package r2b.apps.base;

import java.util.List;

import r2b.apps.R;
import r2b.apps.view.base.BaseAdapter;
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
	protected void initViewsAndValues(Person item, int position,View convertView) {
		TextView txt = (TextView) convertView.findViewById(R.id.person);
		txt.setText(item.getName());
	}

}
