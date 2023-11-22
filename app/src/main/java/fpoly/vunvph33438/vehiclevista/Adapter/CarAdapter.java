package fpoly.vunvph33438.vehiclevista.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    Context context;
    ArrayList<Car> list;
    CarDAO carDAO;
    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public CarAdapter(Context context, ArrayList<Car> list) {
        this.context = context;
        this.list = list;
        carDAO = new CarDAO(context);
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
            holder.tvIdCar.setText("ID Car :" + car.getIdCar());
            holder.tvIdBrand.setText("ID Brand :" + car.getIdBrand());
            holder.tvModel.setText("Model :" + car.getModel());
            holder.tvPrice.setText("Price :" + car.getPrice());
            holder.tvDescription.setText("Description :" + car.getDescription());
            holder.tvAvailable.setText("Available: " + car.isAvailable());
            String imagePath = car.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                // Use Picasso to load the image asynchronously
                Picasso.get().load(Uri.parse(imagePath)).into(holder.imgCar);
            } else {
                holder.imgCar.setImageResource(R.drawable.car); // Set a default image or handle accordingly
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
            builder.setMessage("Are you sure you want to delete " + car.getIdBrand() + " ?");
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

        TextView tvIdCar, tvIdBrand, tvModel, tvPrice,tvDescription, tvAvailable;
        ImageView imgCar,imgdelete;

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
    private Bitmap convertFileToBitmap(String filePath) {
        try {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                Log.e(TAG, "Image file does not exist: " + filePath);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error converting file to Bitmap: " + e.getMessage());
            return null;
        }
    }
}
