package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.ActivityAddReceipt.ViewCar;
import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class CarHomeAdapter extends RecyclerView.Adapter<CarHomeAdapter.CarHomeViewHolder> implements Filterable {

    Context context;
    ArrayList<Car> list;
    ArrayList<Car> listOld;
    CarDAO carDAO;

    public CarHomeAdapter(Context context, ArrayList<Car> list) {
        this.context = context;
        this.list = list;
        this.listOld = list;
        carDAO = new CarDAO(context);
    }

    public void setCarList(ArrayList<Car> filteredList) {
        list = filteredList;
    }

    @NonNull
    @Override
    public CarHomeAdapter.CarHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_home, parent, false);
        return new CarHomeAdapter.CarHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarHomeViewHolder holder, int position) {
        Car car = list.get(position);

        if (car.getImage() != null && car.getImage().length > 0 && holder.imgCarHome != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
            holder.imgCarHome.setImageBitmap(bitmap);
        } else {
            holder.imgCarHome.setImageResource(R.drawable.logo);
        }
        holder.tvCarHome.setText(car.getModel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startViewCarActivity(car);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    list = listOld;
                } else {
                    ArrayList<Car> list1 = new ArrayList<>();
                    for (Car car : listOld) {
                        if (car.getModel().toLowerCase().contains(strSearch.toLowerCase())) {
                            list1.add(car);
                        }
                    }

                    list = list1;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Car>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void startViewCarActivity(Car car) {
        Intent intent = new Intent(context, ViewCar.class);
        intent.putExtra("carId", car.getIdCar());
        context.startActivity(intent);
    }

    public class CarHomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCarHome;
        TextView tvCarHome;

        public CarHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCarHome = itemView.findViewById(R.id.imgCarHome);
            tvCarHome = itemView.findViewById(R.id.tvCarHome);
        }
    }
}
