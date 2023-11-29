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

import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class ReceiptUserAdapter extends RecyclerView.Adapter<ReceiptUserAdapter.ReceiptViewHolder> {
    Context context;
    ArrayList<Receipt> list;
    private ItemClickListener itemClickListener;
    ReceiptDAO receiptDAO;
    private int loggedInUserId;
    private static final String TAG = "ReceiptAdminAdapter";
    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        filterReceipts();
    }
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public ReceiptUserAdapter(Context context, ArrayList<Receipt> list) {
        this.context = context;
        this.list = list;
        receiptDAO = new ReceiptDAO(context);
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
            // Only display receipts for the logged-in user
            if (receipt.getId_User() == loggedInUserId) {
                holder.tvIdReceipt.setText("ID Receipt :" + receipt.getId_Receipt());
                holder.tvIdCar.setText("ID Car :" + receipt.getId_Car());
                holder.tvStartDate.setText("Start Date :" + receipt.getRentalStartDate());
                holder.tvEndDate.setText("End Date :" + receipt.getRentalEndDate());

                if (receipt.getPaymentMethod() == 0) {
                    holder.tvPayment.setTextColor(Color.RED);
                    holder.tvPayment.setText("UnPayment");
                } else {
                    holder.tvPayment.setTextColor(Color.BLUE);
                    holder.tvPayment.setText("Payment");
                }
                holder.tvPrice.setText(String.valueOf(receipt.getPrice()));
            } else {
                // If the receipt is not for the logged-in user, hide the item or set visibility to GONE
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }
    private void filterReceipts() {
        ArrayList<Receipt> filteredList = new ArrayList<>();
        for (Receipt receipt : list) {
            if (receipt.getId_User() == loggedInUserId) {
                filteredList.add(receipt);
            }
        }
        list = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdReceipt, tvIdCar, tvStartDate, tvEndDate,tvPayment, tvPrice;
        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdReceipt = itemView.findViewById(R.id.tvIdReceiptUser);
            tvIdCar = itemView.findViewById(R.id.tvIdCarReceiptUser);
            tvStartDate = itemView.findViewById(R.id.tvRentalStartDateUser);
            tvEndDate = itemView.findViewById(R.id.tvRentalEndDateUser);
            tvPayment = itemView.findViewById(R.id.tvPaymentMethodUser);
            tvPrice = itemView.findViewById(R.id.tvPriceReceiptUser);
        }
    }
}
