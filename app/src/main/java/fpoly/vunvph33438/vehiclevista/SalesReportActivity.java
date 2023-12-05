package fpoly.vunvph33438.vehiclevista;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;

public class SalesReportActivity extends AppCompatActivity {

    EditText edTuNgay, edDenNgay;
    TextView tvRevenue;
    ReceiptDAO receiptDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sales report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edTuNgay = findViewById(R.id.edTuNgay);
        edDenNgay = findViewById(R.id.edDenNgay);
        tvRevenue = findViewById(R.id.tvRevenue);
        receiptDAO = new ReceiptDAO(SalesReportActivity.this);

        findViewById(R.id.btnTuNgay).setOnClickListener(v -> {
            showDatePickerDialog(edTuNgay);
        });
        findViewById(R.id.btnDenNgay).setOnClickListener(v -> {
            showDatePickerDialog(edDenNgay);
        });
        findViewById(R.id.btnFind).setOnClickListener(v -> {
            String tuNgay = edTuNgay.getText().toString();
            String denNgay = edDenNgay.getText().toString();
            if (!tuNgay.isEmpty() && !denNgay.isEmpty()) {
                tvRevenue.setText(receiptDAO.Revenue(tuNgay, denNgay) + " VND");
            } else {
                Toast.makeText(this, "Please enter the full date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, yearSelected, monthOfYear, dayOfMonthSelected) -> {
            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(yearSelected, monthOfYear, dayOfMonthSelected);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = sdf.format(selectedDateCalendar.getTime());
            editText.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}