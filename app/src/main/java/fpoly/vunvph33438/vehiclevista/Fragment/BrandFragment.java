package fpoly.vunvph33438.vehiclevista.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Adapter.BrandAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class BrandFragment extends Fragment {
    RecyclerView recyclerView;
    BrandDAO brandDAO;
    EditText edIdBrand, edBrand;
    ArrayList<Brand> list = new ArrayList<>();
    public static final String TAG = "BrandFragment";
    BrandAdapter brandAdapter;
    public boolean isArray(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }

    public BrandFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand, container, false);
        recyclerView = view.findViewById(R.id.rcvBrand);
        brandDAO = new BrandDAO(getContext());
        list = brandDAO.selectAll();
        brandAdapter = new BrandAdapter(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(brandAdapter);

        view.findViewById(R.id.fabBrand).setOnClickListener(v -> {
            showAddOrEditDialog_Brand(getContext(), 0, null);
        });

        brandAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void UpdateItem(int position) {
                Brand brand = list.get(position);
                showAddOrEditDialog_Brand(getContext(), 1, brand);
            }
        });
        return view;
    }
    protected void showAddOrEditDialog_Brand(Context context, int type, Brand brand) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_brand, null);
        builder.setView(mView);
        AlertDialog alertDialog = builder.create();
        edIdBrand = mView.findViewById(R.id.edIdBrand);
        edBrand = mView.findViewById(R.id.edBrand);
        edIdBrand.setEnabled(false);

        if (type != 0) {
            edIdBrand.setText(String.valueOf(brand.getIdBrand()));
            edBrand.setText(String.valueOf(brand.getBrand()));
        }
        mView.findViewById(R.id.btnSaveBrand).setOnClickListener(v -> {
            String brandName = edBrand.getText().toString();
            if (brandName.isEmpty()) {
                Toast.makeText(context, "Please enter complete information", Toast.LENGTH_SHORT).show();
            } else if (!isArray(brandName)) {
                Toast.makeText(context, "Please enter the correct format", Toast.LENGTH_SHORT).show();
            } else {
                if (type == 0) {
                    Brand brand1 = new Brand();
                    brand1.setBrand(brandName);
                    try {
                        if (brandDAO.insert(brand1)) {
                            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                            list.add(brand1);
                            brandAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Add failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error while manipulating database", e);
                        Toast.makeText(context,"Add failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    brand.setBrand(brandName);
                    try {
                        if (brandDAO.update(brand)) {
                            Toast.makeText(context,"Edited successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            update();
                        } else {
                            Toast.makeText(context, "Edit failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error while manipulating database", e);
                        Toast.makeText(context, "Edit failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mView.findViewById(R.id.btnCancleBrand).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void update() {
        list.clear();
        list.addAll(brandDAO.selectAll());
        brandAdapter.notifyDataSetChanged();
    }
}