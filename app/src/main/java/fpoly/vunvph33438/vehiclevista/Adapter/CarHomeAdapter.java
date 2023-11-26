package fpoly.vunvph33438.vehiclevista.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class CarHomeAdapter extends RecyclerView.Adapter<CarHomeAdapter.CarHomeViewHolder> {
    Context context;
    ArrayList<Car> list;
    CarDAO carDAO;
    public CarHomeAdapter(Context context, ArrayList<Car> list) {
        this.context = context;
        this.list = list;
        carDAO = new CarDAO(context);
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
        String imagePath = Arrays.toString(car.getImage());
        if (imagePath != null && !imagePath.isEmpty() && holder.imgCarHome != null) {
            // Check if holder.imgCarHome is not null before loading the image
            Picasso.get()
                    .load(imagePath)
                    .error(R.drawable.car) // Set an error image
                    .into(holder.imgCarHome);
        } else {
            // Handle the case where imagePath is null or empty, or holder.imgCarHome is null
            holder.imgCarHome.setImageResource(R.drawable.car);
            // Set a default image or handle accordingly
        }
        holder.tvCarHome.setText(car.getModel());


    }


    @Override
    public int getItemCount() {
        return list.size();
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
