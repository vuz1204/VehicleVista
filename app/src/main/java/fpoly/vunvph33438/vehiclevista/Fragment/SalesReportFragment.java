package fpoly.vunvph33438.vehiclevista.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.R;


public class SalesReportFragment extends Fragment {

    EditText edTuNgay, edDenNgay;
    TextView tvRevenue;
    ReceiptDAO receiptDAO;

    public SalesReportFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sales_report_activity, container, false);
        edTuNgay = view.findViewById(R.id.edTuNgay);
        edDenNgay = view.findViewById(R.id.edDenNgay);
        tvRevenue = view.findViewById(R.id.tvRevenue);
        receiptDAO = new ReceiptDAO(getContext());

        view.findViewById(R.id.btnTuNgay).setOnClickListener(v -> {
            showDatePickerDialog(edTuNgay);
        });
        view.findViewById(R.id.btnDenNgay).setOnClickListener(v -> {
            showDatePickerDialog(edDenNgay);
        });
        view.findViewById(R.id.btnFind).setOnClickListener(v -> {
            String tuNgay = edTuNgay.getText().toString();
            String denNgay = edDenNgay.getText().toString();
            if (!tuNgay.isEmpty() && !denNgay.isEmpty()) {
                tvRevenue.setText(receiptDAO.revenue(tuNgay, denNgay) + " VND");
            } else {
                Toast.makeText(getContext(), "Please enter the full date", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, yearSelected, monthOfYear, dayOfMonthSelected) -> {
            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(yearSelected, monthOfYear, dayOfMonthSelected);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = sdf.format(selectedDateCalendar.getTime());
            editText.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}