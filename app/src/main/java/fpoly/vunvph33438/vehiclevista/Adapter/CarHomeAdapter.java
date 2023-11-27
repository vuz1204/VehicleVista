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

        // Check if the image array is not null
        if (car.getImage() != null && car.getImage().length > 0 && holder.imgCarHome != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
            holder.imgCarHome.setImageBitmap(bitmap);
        } else {
            holder.imgCarHome.setImageResource(R.drawable.logo);
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
