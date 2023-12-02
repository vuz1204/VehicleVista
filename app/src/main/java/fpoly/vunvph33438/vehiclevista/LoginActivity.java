package fpoly.vunvph33438.vehiclevista;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class LoginActivity extends AppCompatActivity {

    LinearLayout layoutSignUp;
    EditText edUsername, edPassword;
    TextView tvForgotPassLogin;
    Button btnLogin;
    CheckBox chkRememberPass;
    String strUser, strPass;
    Spinner spinnerRole;
    String valueRole;
    int rolePosition;
    UserDAO userDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layoutSignUp = findViewById(R.id.layoutSignUp);
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        chkRememberPass = findViewById(R.id.chkRememberPass);
        tvForgotPassLogin = findViewById(R.id.tvForgotPassLogin);
        userDAO = new UserDAO(this);

        spinnerRole = findViewById(R.id.spinnerRole);
        ArrayList<String> list = new ArrayList<>();
        list.add("Admin");
        list.add("User");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinnerRole.setAdapter(adapter);
        readFile();
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rolePosition = i;
                valueRole = list.get(i);
                Toast.makeText(LoginActivity.this, valueRole, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tvForgotPassLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        edUsername.setText(sharedPreferences.getString("USERNAME", ""));
        edPassword.setText(sharedPreferences.getString("PASSWORD", ""));
        chkRememberPass.setChecked(sharedPreferences.getBoolean("REMEMBER", false));
        spinnerRole.setSelection(sharedPreferences.getInt("ROLE", 0));
    }

    public void checkLogin() {
        strUser = edUsername.getText().toString();
        strPass = edPassword.getText().toString();
        if (strUser.isEmpty() || strPass.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
        } else {
            if (userDAO.checkLogin(strUser, strPass, String.valueOf(rolePosition))) {
                User user = userDAO.selectID(strUser);
                valueRole = (user.getRole() == 0) ? "admin" : "thuKho";
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                rememberUser(strUser, strPass, chkRememberPass.isChecked(), rolePosition);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                saveUserIdToSharedPreferences(user.getId_user());
                intent.putExtra("role", valueRole);
                startActivity(intent);
                finishAffinity();
            } else {
                Toast.makeText(this, "Login failed. Check your credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void rememberUser(String u, String p, boolean status, int rolePosition) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (!status) {
            edit.clear();
        } else {
            edit.putString("USERNAME", u);
            edit.putString("PASSWORD", p);
            edit.putBoolean("REMEMBER", status);
            edit.putInt("ROLE", rolePosition);
        }
        edit.apply();
    }

    private void saveUserIdToSharedPreferences(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("USER_ID", userId);
        editor.apply();
    }
}
