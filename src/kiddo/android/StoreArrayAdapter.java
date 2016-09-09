package kiddo.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import kiddo.android.models.Product;
import kiddo.android.models.Store;


public class StoreArrayAdapter extends ArrayAdapter<Store> {
	private final Context context;
	private final List<Store> stores;
        int button = 0;

	public StoreArrayAdapter(Context context, List<Store> stores, int button) {
		super(context, R.layout.store_item, stores);
		this.context = context;
		this.stores= stores;
                this.button = button;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.store_item, parent, false);
                TextView tv = (TextView) rowView.findViewById(R.id.store_id);
                TextView tv2 = (TextView) rowView.findViewById(R.id.store_name);
                TextView tv3 = (TextView) rowView.findViewById(R.id.price);
		Button button1 = (Button) rowView.findViewById(R.id.button_item);
                Button button2 = (Button) rowView.findViewById(R.id.button_item2);
                tv.setText(Integer.toString(stores.get(position).getStoreId()));
                tv2.setText(stores.get(position).getName());
                tv3.setText(Double.toString(stores.get(position).getPrice()));
                tv2.setTag(Integer.valueOf(position));
                button1.setTag(Integer.valueOf(position));
                button2.setTag(Integer.valueOf(position));
                        
		return rowView;
	}
        
        public void sortByPrice(){
            super.sort(new Comparator<Store>() {
            @Override
            public int compare(Store s1, Store s2) {
                return Double.compare(s1.getPrice(),s2.getPrice());
            }
            });
        }
        
        public void sortByDistance(){
            super.sort(new Comparator<Store>() {
            @Override
            public int compare(Store s1, Store s2) {
                return Double.compare(s1.getDistance(),s2.getDistance());
            }
            });
        }
        
}