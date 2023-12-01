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

public class BrandSpinnerUser extends BaseAdapter {
    Context context;
    ArrayList<Brand> list;

    public BrandSpinnerUser(Context context, ArrayList<Brand> list) {
        this.context = context;

        Brand defaultBrand = new Brand();
        defaultBrand.setIdBrand(0);
        defaultBrand.setBrand("--Choose one brand--");
        list.add(0, defaultBrand);

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
        TextView tvBrand;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_brand_spinner_user, parent, false);
            viewHolder = new BrandViewHolder();
            viewHolder.tvBrand = convertView.findViewById(R.id.tvBrandSpinnerUser);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BrandViewHolder) convertView.getTag();
        }
        Brand brand = list.get(position);
        viewHolder.tvBrand.setText(brand.getBrand());
        return convertView;
    }
}
