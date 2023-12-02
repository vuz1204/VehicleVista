package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class CarSpinner extends BaseAdapter {

    Context context;
    ArrayList<Car> list;

    public CarSpinner(Context context, ArrayList<Car> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarSpinner.CarViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_brand_spinner, parent, false);
            viewHolder = new CarSpinner.CarViewHolder();
            viewHolder.tvIdCar = convertView.findViewById(R.id.tvIdCarSpinner);
            viewHolder.tvModel = convertView.findViewById(R.id.tvCarSpinner);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CarSpinner.CarViewHolder) convertView.getTag();
        }
        Car car = list.get(position);
        viewHolder.tvIdCar.setText(String.valueOf(car.getIdCar()));
        viewHolder.tvModel.setText(car.getModel());
        return convertView;
    }

    private static class CarViewHolder {
        TextView tvIdCar;
        TextView tvModel;
    }
}
