package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private static final String TAG = "ReceiptAdminAdapter";
    Context context;
    ArrayList<Receipt> list;
    ReceiptDAO receiptDAO;
    CarDAO carDAO;
    private ItemClickListener itemClickListener;

    public ReceiptAdapter(Context context, ArrayList<Receipt> list) {
        this.context = context;
        this.list = list;
        receiptDAO = new ReceiptDAO(context);
        carDAO = new CarDAO(context);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ReceiptAdapter.ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt, parent, false);
        return new ReceiptAdapter.ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptAdapter.ReceiptViewHolder holder, int position) {
        Receipt receipt = list.get(position);
        if (receipt != null) {
            int carId = receipt.getId_Car();
            String carName = carDAO.getCarNameById(carId);
            holder.tvIdReceipt.setText("ID Receipt: " + receipt.getId_Receipt());
            holder.tvIdCar.setText("Model: " + carName);
            holder.tvIdUser.setText("ID User: " + receipt.getId_User());
            holder.tvStartDate.setText("Start Date: " + receipt.getRentalStartDate());
            holder.tvEndDate.setText("End Date: " + receipt.getRentalEndDate());
            holder.tvDate.setText("Receipt creation date: " + receipt.getDate());
            if (receipt.getPaymentMethod() == 0) {
                holder.tvPayment.setTextColor(Color.GREEN);
                holder.tvPayment.setText("Direct Payment");
            } else {
                holder.tvPayment.setTextColor(Color.GREEN);
                holder.tvPayment.setText("Internet Banking");
                byte[] imageBytes = receipt.getImagePayment();
                if (imageBytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    holder.imgCheckPaymentAdmin.setImageBitmap(bitmap);
                } else {
                    holder.imgCheckPaymentAdmin.setImageResource(R.drawable.img_camera);
                }
            }
            if (receipt.getStatus() == 0) {
                holder.tvStatus.setTextColor(Color.RED);
                holder.tvStatus.setText("Unpaid");
            } else {
                holder.tvStatus.setTextColor(Color.BLUE);
                holder.tvStatus.setText("Paid");
            }
            String formattedPrice = receipt.getPriceFormatted();
            holder.tvPrice.setText(formattedPrice + " ₫");
        }
        setupLongClickDialog(holder.itemView, receipt);

    }

    private void setupLongClickDialog(View view, Receipt receipt) {
        view.setOnLongClickListener(v -> {
            showReceiptDialog(receipt);
            return true;
        });
    }

    private void showReceiptDialog(Receipt receipt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_receipt, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        CheckBox cboReceipt = view.findViewById(R.id.cboPaymentReceipt);
        cboReceipt.setChecked(receipt.getStatus() == 1);
        view.findViewById(R.id.btnSaveReceipt).setOnClickListener(v -> {
            boolean isStatus = cboReceipt.isChecked();
            if (receiptDAO.update(receipt, isStatus)) {
                Toast.makeText(context, "Payment method updated successfully", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Failed to update payment method", Toast.LENGTH_SHORT).show();
            }

            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnCancleReceipt).setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdReceipt, tvIdCar, tvIdUser, tvStartDate, tvEndDate, tvPayment, tvPrice, tvDate,tvStatus;
        ImageView imgCheckPaymentAdmin;
        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdReceipt = itemView.findViewById(R.id.tvIdReceiptAdmin);
            tvIdCar = itemView.findViewById(R.id.tvIdCarReceiptAdmin);
            tvIdUser = itemView.findViewById(R.id.tvIdUserReceiptAdmin);
            tvStartDate = itemView.findViewById(R.id.tvRentalStartDateAdmin);
            tvEndDate = itemView.findViewById(R.id.tvRentalEndDateAdmin);
            tvPayment = itemView.findViewById(R.id.tvPaymentMethodAdmin);
            tvPrice = itemView.findViewById(R.id.tvPriceReceiptAdmin);
            tvDate = itemView.findViewById(R.id.tvDateReceipt);
            tvStatus = itemView.findViewById(R.id.tvStatusReceipt);
            imgCheckPaymentAdmin = itemView.findViewById(R.id.imgCheckPaymentAdmin);
        }

    }
}
