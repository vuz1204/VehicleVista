package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class ReceiptUserAdapter extends RecyclerView.Adapter<ReceiptUserAdapter.ReceiptViewHolder> {

    Context context;
    ArrayList<Receipt> list;
    ReceiptDAO receiptDAO;
    CarDAO carDAO;
    private ItemClickListener itemClickListener;
    private int loggedInUserId;

    public ReceiptUserAdapter(Context context, ArrayList<Receipt> list, int loggedInUserId) {
        this.context = context;
        this.list = list;
        this.loggedInUserId = loggedInUserId;
        receiptDAO = new ReceiptDAO(context);
        carDAO = new CarDAO(context);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ReceiptUserAdapter.ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt_user, parent, false);
        return new ReceiptUserAdapter.ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptUserAdapter.ReceiptViewHolder holder, int position) {
        Receipt receipt = list.get(position);

        if (receipt != null) {
            if (receipt.getId_User() == loggedInUserId) {

                int carId = receipt.getId_Car();
                String carName = carDAO.getCarNameById(carId);
                holder.tvIdReceipt.setText("ID Receipt: " + receipt.getId_Receipt());
                holder.tvIdCar.setText("Model: " + carName);
                holder.tvStartDate.setText("Start Date: " + receipt.getRentalStartDate());
                holder.tvEndDate.setText("End Date: " + receipt.getRentalEndDate());
                holder.tvDate.setText("Receipt creation date: " + receipt.getDate());
                if (receipt.getPaymentMethod() == 0) {
                    holder.tvPayment.setTextColor(Color.RED);
                    holder.tvPayment.setText("Unpaid");
                } else {
                    holder.tvPayment.setTextColor(Color.BLUE);
                    holder.tvPayment.setText("Paid");
                }
                String formattedPrice = receipt.getPriceFormatted();
                holder.tvPrice.setText(formattedPrice + " ₫");
            } else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdReceipt, tvIdCar, tvStartDate, tvEndDate, tvPayment, tvPrice, tvDate;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdReceipt = itemView.findViewById(R.id.tvIdReceiptUser);
            tvIdCar = itemView.findViewById(R.id.tvIdCarReceiptUser);
            tvStartDate = itemView.findViewById(R.id.tvRentalStartDateUser);
            tvEndDate = itemView.findViewById(R.id.tvRentalEndDateUser);
            tvPayment = itemView.findViewById(R.id.tvPaymentMethodUser);
            tvPrice = itemView.findViewById(R.id.tvPriceReceiptUser);
            tvDate = itemView.findViewById(R.id.tvDateReceiptUser);
        }
    }
}
