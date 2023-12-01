package fpoly.vunvph33438.vehiclevista.ActivityAddReceipt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fpoly.vunvph33438.vehiclevista.Adapter.ReceiptAdapter;
import fpoly.vunvph33438.vehiclevista.Adapter.ReceiptUserAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Fragment.CarBookingHistoryFragment;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class RentalCar extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText edIdReceipt, edIdCar, edIdUser, edStartDate, edEndDate, edPrice;
    private EditText selectedEditText;
    RadioGroup rdoGroup;
    RadioButton rdoDirectPayment, rdoCreditCard;
    Button btnRentalCar;
    private int carPrice;
    private ArrayList<Receipt> list = new ArrayList<>();
    ReceiptAdapter receiptAdapter;
    ReceiptUserAdapter receiptUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_car);
        edIdReceipt = findViewById(R.id.edIdReceipt);
        edIdCar = findViewById(R.id.edIdCarRental);
        edIdUser = findViewById(R.id.edIdUserRentalCar);
        edStartDate = findViewById(R.id.edStartDate);
        edEndDate = findViewById(R.id.edEndDate);
        edPrice = findViewById(R.id.edPriceRenTalCar);
        rdoGroup = findViewById(R.id.rdoGroupRentalCar);
        rdoDirectPayment = findViewById(R.id.rdoDirectPayment);
        rdoCreditCard = findViewById(R.id.rdoCreditCardPayment);
        btnRentalCar = findViewById(R.id.btnRentalCar);
        int carId = getIntent().getIntExtra("carId", -1);
        int userId = getUserIdFromSharedPreferences();
        carPrice = getIntent().getIntExtra("carPrice", 0);
        edIdCar.setText(String.valueOf(carId));
        edIdUser.setText(String.valueOf(userId));
        edStartDate.setOnClickListener(v -> showDatePickerDialog(edStartDate));
        edEndDate.setOnClickListener(v -> showDatePickerDialog(edEndDate));
        btnRentalCar.setOnClickListener(v -> rentCar());
        edIdReceipt.setEnabled(false);
    }
    private void rentCar() {
        int selectedPaymentMethod;
        if (rdoDirectPayment.isChecked()) {
            selectedPaymentMethod = 0;
        } else if (rdoCreditCard.isChecked()) {
            selectedPaymentMethod = 1;
        } else {
            Toast.makeText(RentalCar.this, "Please select payment method", Toast.LENGTH_SHORT).show();
            return;
        }
        Receipt receipt = new Receipt();
        receipt.setPaymentMethod(selectedPaymentMethod);
        receipt.setId_Car(Integer.parseInt(edIdCar.getText().toString()));
        receipt.setId_User(Integer.parseInt(edIdUser.getText().toString()));
        receipt.setRentalStartDate(edStartDate.getText().toString());
        receipt.setRentalEndDate(edEndDate.getText().toString());
        receipt.setPrice(Integer.parseInt(edPrice.getText().toString()));
        ReceiptDAO receiptDAO = new ReceiptDAO(this);
        if (receiptDAO.insert(receipt)) {
            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
            list.add(receipt);
            updateCarAvailability(Integer.parseInt(edIdCar.getText().toString()));
            Intent intent = new Intent(RentalCar.this, CarBookingHistoryFragment.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog(EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
        selectedEditText = editText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (selectedEditText != null) {
            selectedEditText.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth));
            updatePrice();
        }
    }

    private void updatePrice() {
        String startDateStr = edStartDate.getText().toString();
        String endDateStr = edEndDate.getText().toString();
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = dateFormat.parse(endDateStr);
                Log.d("RentalCar", "StartDate: " + startDate);
                Log.d("RentalCar", "EndDate: " + endDate);
                long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                int totalPrice = (int) (diff * carPrice);
                Log.d("RentalCar", "TotalPrice: " + totalPrice);
                edPrice.setText(String.valueOf(totalPrice));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private int getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        return sharedPreferences.getInt("USER_ID", -1);
    }
    private void updateCarAvailability(int carId) {
        CarDAO carDAO = new CarDAO(RentalCar.this);
        int unavailable = 1;
        Car car = carDAO.getCarById(carId);
        if (car != null) {
            car.setAvailable(unavailable);
            carDAO.update(car);
            Toast.makeText(this, "Car rented successfully. Car is now unavailable.", Toast.LENGTH_SHORT).show();
        }
    }


}