package kiddo.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import kiddo.android.models.Product;


public class ProductAdapter {//extends ArrayAdapter<Product> {
//    private Activity activity;
//    private List<Product> lProduct;
//    private static LayoutInflater inflater = null;
//
//    public ProductAdapter (Activity activity, int textViewResourceId,List<Product> _lProduct) {
//        super(activity, textViewResourceId, _lProduct);
//        try {
//            this.activity = activity;
//            this.lProduct = _lProduct;
//
//            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    public int getCount() {
//        return lProduct.size();
//    }
//
//    public Product getItem(Product position) {
//        return position;
//    }
//
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public static class ViewHolder {
//        public TextView display_name;
//        public TextView display_number;             
//
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View vi = convertView;
//        final ViewHolder holder;
//        try {
//            if (convertView == null) {
//                vi = inflater.inflate(R.layout.catalog, null);
//                holder = new ViewHolder();
//
//                holder.display_name = (TextView) vi.findViewById(R.id.display_name);
////                holder.display_number = (TextView) vi.findViewById(R.id.display_number);
//
//
//                vi.setTag(holder);
//            } else {
//                holder = (ViewHolder) vi.getTag();
//            }
//
//
//
//            holder.display_name.setText(lProduct.get(position).getName());
////            holder.display_number.setText(lProduct.get(position).getProductId());
//
//
//        } catch (Exception e) {
//
//
//        }
//        return vi;
//    }
}