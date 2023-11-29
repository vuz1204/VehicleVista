package fpoly.vunvph33438.vehiclevista.ActivityAddReceipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class ViewCar extends AppCompatActivity {
    ImageView imgViewCar;
    TextView tvNameViewCar, tvPriceViewCar, tvDescriptionViewCar;
    Button btnRentalViewCar;
    ArrayList<Car> list;
    CarDAO carDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);
        imgViewCar = findViewById(R.id.imgViewCar);
        tvNameViewCar = findViewById(R.id.tvNameViewCar);
        tvPriceViewCar = findViewById(R.id.tvPriceViewCar);
        tvDescriptionViewCar = findViewById(R.id.tvDescriptionViewCar);
        btnRentalViewCar = findViewById(R.id.btnRentalViewCar);

        int carId = getIntent().getIntExtra("carId", -1);
        if (carId != -1) {
            carDAO = new CarDAO(this);
            Car car = carDAO.getCarById(carId);
            tvNameViewCar.setText("Name: " + car.getModel());
            tvPriceViewCar.setText("Price: " + car.getPrice());
            tvDescriptionViewCar.setText("Description: " + car.getDescription());

            if (car.getImage() != null && car.getImage().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
                imgViewCar.setImageBitmap(bitmap);
            } else {
                imgViewCar.setImageResource(R.drawable.logo);
            }

            btnRentalViewCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (car.isAvailable()==0){
                       Intent intent = new Intent(ViewCar.this, RentalCar.class);
                       intent.putExtra("carId", car.getIdCar());
                       intent.putExtra("carPrice", car.getPrice());
                       startActivity(intent);
                   }else {
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
