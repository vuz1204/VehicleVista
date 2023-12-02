package fpoly.vunvph33438.vehiclevista;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;

public class ForgotPassword extends AppCompatActivity {

    EditText edUsername, edEmail;
    Button btnSaveChangePass, btnCancelChangePass;
    UserDAO userDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edUsername = findViewById(R.id.edUserForgot);
        edEmail = findViewById(R.id.edEmailForgot);
        userDAO = new UserDAO(this);

        btnCancelChangePass = findViewById(R.id.btnBack);
        btnCancelChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSaveChangePass = findViewById(R.id.btnResetPass);
        btnSaveChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int validationStatus = validate();
                if (validationStatus > 0) {
                    if (userDAO.checkInfor(edUsername.getText().toString(), edEmail.getText().toString())) {
                        Intent intent = new Intent(ForgotPassword.this, ChangePassForgot.class);
                        intent.putExtra("username", edUsername.getText().toString());
                        startActivity(intent);

                    } else {
                        Toast.makeText(ForgotPassword.this, "Invalid username or email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private int validate() {
        int check = 1;
        if (edUsername.getText().toString().isEmpty() || edEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            if (!isEmailValid(edEmail.getText().toString())) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}