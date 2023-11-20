package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.R;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>{
    Context context;
    ArrayList<Brand> list;
    private ItemClickListener itemClickListener;
    BrandDAO brandDAO;
    private static final String TAG = "BrandAdapter";
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public BrandAdapter(Context context, ArrayList<Brand> list) {
        this.context = context;
        this.list = list;
        brandDAO = new BrandDAO(context);
    }
    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        holder.itemView.setOnLongClickListener(v -> {
            try {
                if (itemClickListener != null) {
                    itemClickListener.UpdateItem(position);
                }
            } catch (Exception e) {
                Log.e(TAG, "onBindViewHolder: " + e);
            }
            return false;
        });
        holder.tvIdBrand.setText("ID Brand :" + list.get(position).getIdBrand());
        holder.tvBrand.setText("Brand :" + list.get(position).getBrand());
        holder.imgDelete.setOnClickListener(v -> {
            showDeleteDialog(position);
        });
    }


    public void showDeleteDialog(int position) {
        if (position >= 0 && position < list.size()) {
            Brand brand = list.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.warning);
            builder.setTitle("Notification");
            builder.setMessage("Are you sure you want to delete " + brand.getIdBrand() + " ?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (brandDAO.delete(brand)) {
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            list.remove(brand);
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

    class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdBrand, tvBrand;
        ImageView imgDelete;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdBrand = itemView.findViewById(R.id.tvIdBrand);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            imgDelete = itemView.findViewById(R.id.imgDeleteBrand);
        }
    }
}
