package fpoly.vunvph33438.vehiclevista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class UpdateProfileActivity extends AppCompatActivity {

    UserDAO userDAO;
    EditText edFullnameUD, edEmailUD, edPhoneNumberUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        edFullnameUD = findViewById(R.id.edFullnameUD);
        edEmailUD = findViewById(R.id.edEmailUD);
        edPhoneNumberUD = findViewById(R.id.edPhoneNumberUD);

        userDAO =  new UserDAO(this);

        findViewById(R.id.btnSaveUD).setOnClickListener(v -> {
            String fullname = edFullnameUD.getText().toString().trim();
            String email = edEmailUD.getText().toString().trim();
            String phoneNumberStr = edPhoneNumberUD.getText().toString().trim();

            if (fullname.isEmpty() || email.isEmpty() || phoneNumberStr.isEmpty()) {
                Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int phoneNumber = Integer.parseInt(phoneNumberStr);

                    if (!isValidEmail(email)) {
                        Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (phoneNumber < 1000000 || phoneNumber > 99999999999L) {
                        Toast.makeText(this, "Phone number must be between 7 and 11 digits", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid phone number. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                String loggedInUsername = sharedPreferences.getString("USERNAME", "");

                User user = userDAO.selectID(loggedInUsername);
                user.setFullname(fullname);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumberStr);
                try {
                    if (userDAO.update(user)) {
                        Toast.makeText(this, "Edited successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Edit failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Edit failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnCancelUD).setOnClickListener(v -> {
            finish();
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
}