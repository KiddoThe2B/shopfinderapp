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
import java.util.List;
import kiddo.android.models.Product;


public class ProductArrayAdapter extends ArrayAdapter<Product> {
	private final Context context;
	private final List<Product> products;
        int button = 0;

	public ProductArrayAdapter(Context context, List<Product> products, int button) {
		super(context, R.layout.list_item, products);
		this.context = context;
		this.products= products;
                this.button = button;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.list_item, parent, false);
                TextView tv = (TextView) rowView.findViewById(R.id.product_id);
                TextView tv2 = (TextView) rowView.findViewById(R.id.product_name);
		Button button1 = (Button) rowView.findViewById(R.id.button_item);
                tv.setText(Integer.toString(products.get(position).getProductId()));
                tv2.setText(products.get(position).getName());
                tv2.setTag(Integer.valueOf(products.get(position).getProductId()));
                button1.setTag(Integer.valueOf(products.get(position).getProductId()));
                if(button==1){
                    button1.setText("Remove");
                }
                else{
                    button1.setText("Add to Cart");
                }
                        
		return rowView;
	}
        
}