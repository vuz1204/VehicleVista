package fpoly.vunvph33438.vehiclevista.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import fpoly.vunvph33438.vehiclevista.Adapter.BrandSpinner;
import fpoly.vunvph33438.vehiclevista.Adapter.ReceiptUserAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class CarBookingHistoryFragment extends Fragment {
    private static final String TAG = "CarBookingHistoryFragment";
    RecyclerView rcvReceipt;
    ReceiptDAO receiptDAO;
    ArrayList<Receipt> list = new ArrayList<>();
    ReceiptUserAdapter receiptUserAdapter;
    int selectedPosition;
    ImageView imgQR;
    private Uri selectedImageUri;
    private final ActivityResultLauncher<String> importImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), this::onImagePicked);

    public CarBookingHistoryFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_booking_history, container, false);
        rcvReceipt = view.findViewById(R.id.rcvCarBookingHistory);
        receiptDAO = new ReceiptDAO(getContext());
        list = receiptDAO.selectID();
        if (!list.isEmpty()) {
            receiptUserAdapter = new ReceiptUserAdapter(getContext(), list, getLoggedInUserId());
            rcvReceipt.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvReceipt.setAdapter(receiptUserAdapter);
        }
        receiptUserAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void UpdateItem(int position) {
                Receipt receipt = list.get(position);
                showUpdateDialog(getContext(), 1, receipt);
            }
        });
        return view;

    }
    private void showUpdateDialog(Context context, int type, Receipt receipt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_checkout, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        imgQR = view1.findViewById(R.id.imgQR);
        imgQR.setOnClickListener(v -> {
            importImageLauncher.launch("image/*");
        });
        String QrImage = Arrays.toString(receipt.getImagePayment());
        if (QrImage != null && !QrImage.isEmpty()) {
            selectedImageUri = Uri.parse(QrImage);
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                imgQR.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imgQR.setImageResource(R.drawable.img_camera);
        }

        view1.findViewById(R.id.btnSaveCheckOut).setOnClickListener(v -> {
                    byte[] imageBytes = convertImageUriToByteArray(selectedImageUri);
                    receipt.setImagePayment(imageBytes);
                    try {
                        if (receiptDAO.updateImage(receipt.getId_Receipt(),imageBytes)) {
                            Toast.makeText(requireContext(), "Edited successfully", Toast.LENGTH_SHORT).show();
                            updateList();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(requireContext(), "Edit failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error while manipulating the database: ", e);
                        Toast.makeText(requireContext(), "Edit failed", Toast.LENGTH_SHORT).show();
                    }
        });
        view1.findViewById(R.id.btnCancelCheckOut).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    public void updateList() {
        list.clear();
        list.addAll(receiptDAO.selectID());
        receiptUserAdapter.notifyDataSetChanged();
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("USER_ID", -1);
    }
    private void onImagePicked(Uri imageUri) {
        if (imageUri != null) {
            selectedImageUri = imageUri;
            byte[] imageBytes = convertImageUriToByteArray(selectedImageUri);
            if (imageBytes != null) {
                imgQR.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            } else {
                Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "You have to choose a picture", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] convertImageUriToByteArray(Uri imageUri) {
        try {
            if (imageUri != null) {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    inputStream.close();
                    return byteArrayOutputStream.toByteArray();
                } else {
                    Log.e(TAG, "InputStream is null");
                }
            } else {
                Log.e(TAG, "ImageUri is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}