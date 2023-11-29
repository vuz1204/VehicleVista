package fpoly.vunvph33438.vehiclevista.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Adapter.ReceiptAdapter;
import fpoly.vunvph33438.vehiclevista.Adapter.ReceiptUserAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class HistoryFragment extends Fragment {
    RecyclerView rcvReceipt;
    ReceiptDAO receiptDAO;
    ArrayList<Receipt> list = new ArrayList<>();

    ReceiptAdapter receiptAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rcvReceipt = view.findViewById(R.id.rcvReceipt);
        receiptDAO = new ReceiptDAO(getContext());
        list = receiptDAO.selectID();
        receiptAdapter = new ReceiptAdapter(getContext(),list);
        rcvReceipt.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvReceipt.setAdapter(receiptAdapter);

        return view;
    }
}