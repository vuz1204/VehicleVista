package fpoly.vunvph33438.vehiclevista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class ChangePassForgot extends AppCompatActivity {
    EditText edNewPass, edCheckPass;
    Button btnSaveChangePass, btnCancelChangePass;
    String username;

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_forgot);
        edNewPass = findViewById(R.id.edNewPasswordForgot);
        edCheckPass = findViewById(R.id.edReNewPasswordForgot);
        userDAO = new UserDAO(this);
        username = getIntent().getStringExtra("username");
        btnCancelChangePass = findViewById(R.id.btnCancelChangePassForgot);
        btnCancelChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputFields();
            }
        });

        btnSaveChangePass = findViewById(R.id.btnSaveChangePassForgot);
        btnSaveChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int validationStatus = validate();

                if (validationStatus > 0) {
                    User user = userDAO.selectID(username);
                    if (user != null) {
                        user.setPassword(edNewPass.getText().toString());
                        if (userDAO.updatePassForgot(user)) {
                            Toast.makeText(ChangePassForgot.this, "Change password successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePassForgot.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Finish the current activity to prevent going back to it when pressing the back button
                        } else {
                            Toast.makeText(ChangePassForgot.this, "Change password failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePassForgot.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void clearInputFields() {
        edNewPass.setText("");
        edCheckPass.setText("");
    }

    private int validate() {
        int check = 1;
        if (edNewPass.getText().toString().isEmpty() || edCheckPass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
            String passOld = sharedPreferences.getString("PASSWORD", "");
            String pass = edNewPass.getText().toString();
            String rePass = edCheckPass.getText().toString();
            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                check = -1;
            } else if (!pass.equals(rePass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }
}