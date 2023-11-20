package fpoly.vunvph33438.vehiclevista;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class SignUpActivity extends AppCompatActivity {

    EditText edUsernameSU, edFullnameSU, edPhoneNumberSU, edPasswordSU, edRePassword;
    Button btnSignUp;
    UserDAO userDAO;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edUsernameSU = findViewById(R.id.edUsernameSU);
        edFullnameSU = findViewById(R.id.edFullnameSU);
        edPhoneNumberSU = findViewById(R.id.edPhoneNumberSU);
        edPasswordSU = findViewById(R.id.edPasswordSU);
        edRePassword = findViewById(R.id.edRePasswordSU);
        userDAO = new UserDAO(this);

        progressDialog = new ProgressDialog(this);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() > 0) {
                    progressDialog.show();
                    User user = new User();
                    user.setUsername(edUsernameSU.getText().toString());
                    user.setFullname(edFullnameSU.getText().toString());
                    user.setPhoneNumber(Integer.parseInt(edPhoneNumberSU.getText().toString()));
                    user.setPassword(edPasswordSU.getText().toString());
                    if (userDAO.insertData(user)) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        edUsernameSU.setText("");
                        edFullnameSU.setText("");
                        edPhoneNumberSU.setText("");
                        edPasswordSU.setText("");
                        edRePassword.setText("");
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public int validate() {
        int check = 1;
        String username = edUsernameSU.getText().toString();
        String fullname = edFullnameSU.getText().toString();
        String phoneNumberStr = edPhoneNumberSU.getText().toString();
        String password = edPasswordSU.getText().toString();
        String rePassword = edRePassword.getText().toString();

        if (username.isEmpty() || fullname.isEmpty() || phoneNumberStr.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            try {
                int phoneNumber = Integer.parseInt(phoneNumberStr);

                User existingUser = userDAO.selectID(username);
                if (existingUser != null) {
                    Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
                    check = -1;
                } else if (phoneNumber < 1000000 || phoneNumber > 99999999999L) {
                    Toast.makeText(this, "Phone number must be between 7 and 11 digits", Toast.LENGTH_SHORT).show();
                    check = -1;
                } else if (password.length() < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    check = -1;
                } else if (!password.equals(rePassword)) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    check = -1;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid phone number. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }
}