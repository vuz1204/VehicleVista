package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.R;

public class BrandSpinner extends BaseAdapter {
    Context context;
    ArrayList<Brand> list;

    public BrandSpinner(Context context, ArrayList<Brand> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private static class BrandViewHolder {
        TextView tvIdBrand;
        TextView tvBrand;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_brand_spinner, parent, false);
            viewHolder = new BrandViewHolder();
            viewHolder.tvIdBrand = convertView.findViewById(R.id.tvIdBrandSpinner);
            viewHolder.tvBrand = convertView.findViewById(R.id.tvBrandSpinner);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BrandViewHolder) convertView.getTag();
        }
        Brand brand = list.get(position);
        viewHolder.tvIdBrand.setText(String.valueOf(brand.getIdBrand()));
        viewHolder.tvBrand.setText(brand.getBrand());
        return convertView;
    }
}
