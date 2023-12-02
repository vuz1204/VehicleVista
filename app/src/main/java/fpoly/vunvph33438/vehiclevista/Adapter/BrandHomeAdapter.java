package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.R;

public class BrandHomeAdapter extends RecyclerView.Adapter<BrandHomeAdapter.BrandHomeViewHolder> {

    private static final String TAG = "BrandAdapter";
    Context context;
    ArrayList<Brand> list;
    BrandDAO brandDAO;

    public BrandHomeAdapter(Context context, ArrayList<Brand> list) {
        this.context = context;
        this.list = list;
        brandDAO = new BrandDAO(context);
    }

    @NonNull
    @Override
    public BrandHomeAdapter.BrandHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brand_home, parent, false);
        return new BrandHomeAdapter.BrandHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandHomeAdapter.BrandHomeViewHolder holder, int position) {
        holder.tvBrandHome.setText(list.get(position).getBrand());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BrandHomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvBrandHome;

        public BrandHomeViewHolder(View itemView) {
            super(itemView);
            tvBrandHome = itemView.findViewById(R.id.tvBrandHome);
        }
    }

}
