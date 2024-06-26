package fpoly.vunvph33438.vehiclevista.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    Context context;
    ArrayList<Car> list;
    ArrayList<Car> listOld;
    CarDAO carDAO;
    BrandDAO brandDAO;

    private ItemClickListener itemClickListener;

    public CarAdapter(Context context, ArrayList<Car> list) {
        this.context = context;
        this.list = list;
        this.listOld = list;
        carDAO = new CarDAO(context);
        brandDAO = new BrandDAO(context);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = list.get(position);

        if (car != null) {
            int idBrand = car.getIdBrand();
            String brandName = brandDAO.getBrandNameById(idBrand);
            holder.tvIdCar.setText("ID Car: " + car.getIdCar());
            holder.tvIdBrand.setText("ID Brand: " + brandName);
            holder.tvModel.setText("Model: " + car.getModel());
            String formattedPrice = car.getPriceFormatted();
            holder.tvPrice.setText("Price: " + formattedPrice + " ₫");
            holder.tvDescription.setText("Description: " + car.getDescription());
            if (car.isAvailable() == 0) {
                holder.tvAvailable.setTextColor(Color.BLUE);
                holder.tvAvailable.setText("Available");
            } else {
                holder.tvAvailable.setTextColor(Color.RED);
                holder.tvAvailable.setText("UnAvailable");
            }

            byte[] imageBytes = car.getImage();
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.imgCar.setImageBitmap(bitmap);
            } else {
                holder.imgCar.setImageResource(R.drawable.banner);
            }
        }

        holder.imgdelete.setOnClickListener(v -> {
            showDeleteDialog(position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.UpdateItem(position);
            }
            return false;
        });
    }

    public void showDeleteDialog(int position) {
        if (position >= 0 && position < list.size()) {
            Car car = list.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_warning);
            builder.setTitle("Notification");
            builder.setMessage("Are you sure you want to delete " + car.getModel() + "?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (carDAO.delete(car)) {
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            list.remove(car);
                            notifyItemChanged(position);
                            notifyItemRemoved(position);
                        } else {
                            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else {
            Log.e(TAG, "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdCar, tvIdBrand, tvModel, tvPrice, tvDescription, tvAvailable;
        ImageView imgCar, imgdelete;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdCar = itemView.findViewById(R.id.tvIdCar);
            tvIdBrand = itemView.findViewById(R.id.tvIdBrandCar);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAvailable = itemView.findViewById(R.id.tvAvailable);
            imgCar = itemView.findViewById(R.id.imgCar);
            imgdelete = itemView.findViewById(R.id.imgDeleteCar);
        }
    }
}
