package fpoly.vunvph33438.vehiclevista;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;

public class SalesReportActivity extends AppCompatActivity {

    EditText edTuNgay, edDenNgay;
    TextView tvRevenue;
    ReceiptDAO receiptDAO;
    BarChart chartMonthlyRevenue;
    private List<String> xValues = new ArrayList<>();

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

        chartMonthlyRevenue = findViewById(R.id.chartMonthlyRevenue);
        chartMonthlyRevenue.getAxisRight().setDrawLabels(false);

        receiptDAO = new ReceiptDAO(SalesReportActivity.this);
        List<Integer> monthlyRevenues = getMonthlyRevenues();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        xValues = generateXAxisLabels(currentMonth);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < monthlyRevenues.size(); i++) {
            entries.add(new BarEntry(i, monthlyRevenues.get(i)));
        }

        YAxis yAxis = chartMonthlyRevenue.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(1000000000f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);


        BarDataSet dataSet = new BarDataSet(entries, "Monthly revenue");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        chartMonthlyRevenue.setData(barData);

        chartMonthlyRevenue.getDescription().setEnabled(false);
        chartMonthlyRevenue.invalidate();

        chartMonthlyRevenue.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        chartMonthlyRevenue.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartMonthlyRevenue.getXAxis().setGranularity(1f);
        chartMonthlyRevenue.getXAxis().setGranularityEnabled(true);

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
                String formattedPrice = getPriceFormatted(receiptDAO.revenue(tuNgay, denNgay));
                tvRevenue.setText(formattedPrice + " ₫");
            } else {
                Toast.makeText(this, "Please enter the full date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Integer> getMonthlyRevenues() {
        ArrayList<Integer> monthlyRevenues = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        for (int i = 0; i < 5; i++) {
            int startMonth = (currentMonth - i + 12) % 12;
            String startOfMonth = getFirstDayOfMonth(startMonth, calendar.get(Calendar.YEAR));
            String endOfMonth = getLastDayOfMonth(startMonth, calendar.get(Calendar.YEAR));

            int revenue = receiptDAO.revenue(startOfMonth, endOfMonth);

            Log.d("zzzzzz", "getMonthlyRevenues: " + " " + startOfMonth + " " + endOfMonth + " " + revenue);

            monthlyRevenues.add(revenue);
        }

        return monthlyRevenues;
    }

    private String getFirstDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private String getLastDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


    private List<String> generateXAxisLabels(int currentMonth) {
        String[] monthLabels = new DateFormatSymbols().getShortMonths();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int monthIndex = (currentMonth - i + 12) % 12;
            labels.add(monthLabels[monthIndex]);
        }

        return labels;
    }

    public String getPriceFormatted(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        return numberFormat.format(number);
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