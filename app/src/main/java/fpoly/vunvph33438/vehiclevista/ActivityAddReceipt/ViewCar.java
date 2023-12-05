package fpoly.vunvph33438.vehiclevista.ActivityAddReceipt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.DAO.ReceiptDAO;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.R;

public class ViewCar extends AppCompatActivity {

    ImageView imgViewCar;
    TextView tvNameViewCar, tvPriceViewCar, tvDescriptionViewCar, tvAvailableViewCar, tvStartDateViewCar, tvEndDateViewCar;
    Button btnRentalViewCar, btnReturnViewCar;
    CarDAO carDAO;
    ReceiptDAO receiptDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);
        imgViewCar = findViewById(R.id.imgViewCar);
        tvNameViewCar = findViewById(R.id.tvNameViewCar);
        tvPriceViewCar = findViewById(R.id.tvPriceViewCar);
        tvDescriptionViewCar = findViewById(R.id.tvDescriptionViewCar);
        tvAvailableViewCar = findViewById(R.id.tvAvailableViewCar);
        tvStartDateViewCar = findViewById(R.id.tvStartDateViewCar);
        tvEndDateViewCar = findViewById(R.id.tvEndDateViewCar);
        btnRentalViewCar = findViewById(R.id.btnRentalViewCar);
        btnReturnViewCar = findViewById(R.id.btnReturnViewCar);

        int carId = getIntent().getIntExtra("carId", -1);
        if (carId != -1) {
            carDAO = new CarDAO(this);
            Car car = carDAO.getCarById(carId);

            receiptDAO = new ReceiptDAO(this);
            Receipt receipt = receiptDAO.selectIdByCar(String.valueOf(carId));

            tvNameViewCar.setText(car.getModel());
            String formattedPrice = car.getPriceFormatted();
            tvPriceViewCar.setText(formattedPrice + " ₫");
            tvDescriptionViewCar.setText("Description: " + car.getDescription());
            if (car.isAvailable() == 0) {
                tvAvailableViewCar.setTextColor(Color.BLUE);
                tvAvailableViewCar.setText("Available");
                tvStartDateViewCar.setVisibility(View.GONE);
                tvEndDateViewCar.setVisibility(View.GONE);
            } else {
                tvAvailableViewCar.setTextColor(Color.RED);
                tvAvailableViewCar.setText("UnAvailable");
                tvStartDateViewCar.setVisibility(View.VISIBLE);
                tvEndDateViewCar.setVisibility(View.VISIBLE);
                tvStartDateViewCar.setText(String.valueOf(receipt.getRentalStartDate()));
                tvEndDateViewCar.setText(" --> " + receipt.getRentalEndDate());
            }

            if (car.getImage() != null && car.getImage().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
                imgViewCar.setImageBitmap(bitmap);
            } else {
                imgViewCar.setImageResource(R.drawable.logo);
            }

            btnReturnViewCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnRentalViewCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (car.isAvailable() == 0) {
                        Intent intent = new Intent(ViewCar.this, RentalCar.class);
                        intent.putExtra("carId", car.getIdCar());
                        intent.putExtra("carPrice", car.getPrice());
                        startActivity(intent);
                    } else {
                        Toast.makeText(ViewCar.this, "The car is being rented", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Invalid car ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
