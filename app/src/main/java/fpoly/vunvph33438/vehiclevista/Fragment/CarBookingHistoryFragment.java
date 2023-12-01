package fpoly.vunvph33438.vehiclevista.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Adapter.ReceiptUserAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class CarBookingHistoryFragment extends Fragment {
    RecyclerView rcvReceipt;
    ReceiptDAO receiptDAO;
    ArrayList<Receipt> list = new ArrayList<>();

    ReceiptUserAdapter receiptUserAdapter;


    public CarBookingHistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_booking_history, container, false);
        rcvReceipt = view.findViewById(R.id.rcvCarBookingHistory);
        receiptDAO = new ReceiptDAO(getContext());
        list = receiptDAO.selectID();
        receiptUserAdapter = new ReceiptUserAdapter(getContext(), list, getLoggedInUserId());
        rcvReceipt.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvReceipt.setAdapter(receiptUserAdapter);
        return view;
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("USER_ID", -1);
    }
}