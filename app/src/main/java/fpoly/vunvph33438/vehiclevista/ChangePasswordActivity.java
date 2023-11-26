package fpoly.vunvph33438.vehiclevista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edOldPassWord, edNewPassword, edReNewPassword;
    Button btnSaveChangePass, btnCancelChangePass;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edOldPassWord = findViewById(R.id.edOldPassWord);
        edNewPassword = findViewById(R.id.edNewPassword);
        edReNewPassword = findViewById(R.id.edReNewPassword);
        userDAO = new UserDAO(this);

        btnCancelChangePass = findViewById(R.id.btnCancelChangePass);
        btnCancelChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveChangePass = findViewById(R.id.btnSaveChangePass);
        btnSaveChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("USERNAME", "");

                int validationStatus = validate();

                if (validationStatus > 0) {
                    User user = userDAO.selectID(username);

                    if (user != null) {
                        user.setPassword(edNewPassword.getText().toString());

                        if (userDAO.updatePass(user)) {
                            Toast.makeText(ChangePasswordActivity.this, "Change password successful", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("PASSWORD", edNewPassword.getText().toString());
                            editor.apply();

                            clearInputFields();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Change password failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void clearInputFields() {
        edOldPassWord.setText("");
        edNewPassword.setText("");
        edReNewPassword.setText("");
    }

    public int validate() {
        int check = 1;
        if (edOldPassWord.getText().toString().isEmpty() || edNewPassword.getText().toString().isEmpty() || edReNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
            String passOld = sharedPreferences.getString("PASSWORD", "");
            String pass = edNewPassword.getText().toString();
            String rePass = edReNewPassword.getText().toString();
            if (!passOld.equals(edOldPassWord.getText().toString())) {
                Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                check = -1;
            } else if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                check = -1;
            } else if (pass.equals(passOld)) {
                Toast.makeText(this, "New password must not match the old password", Toast.LENGTH_SHORT).show();
                check = -1;
            } else if (!pass.equals(rePass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }
}