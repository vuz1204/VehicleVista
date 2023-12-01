package fpoly.vunvph33438.vehiclevista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class AddUserActivity extends AppCompatActivity {

    EditText edAddUsername, edAddFullname, edAddEmail, edAddPhoneNumber, edAddPass, edAddRePass;
    UserDAO userDAO;
    Button btnSaveAdd, btnCancelAdd;
    Spinner spinnerRole;
    ArrayList<String> list = new ArrayList<>();
    String valueRole;
    int rolePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        edAddUsername = findViewById(R.id.edAddUsername);
        edAddFullname = findViewById(R.id.edAddFullname);
        edAddEmail = findViewById(R.id.edAddEmail);
        edAddPhoneNumber = findViewById(R.id.edAddPhoneNumber);
        edAddPass = findViewById(R.id.edAddPass);
        edAddRePass = findViewById(R.id.edAddRePass);
        userDAO = new UserDAO(this);

        spinnerRole = findViewById(R.id.spinnerAddRole);
        list.add("Admin");
        list.add("User");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinnerRole.setAdapter(adapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rolePosition = position;
                valueRole = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSaveAdd = findViewById(R.id.btnSaveAdd);
        btnSaveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() > 0) {
                    User user = new User();
                    user.setUsername(edAddUsername.getText().toString());
                    user.setFullname(edAddFullname.getText().toString());
                    user.setEmail(edAddEmail.getText().toString());
                    user.setPhoneNumber(edAddPhoneNumber.getText().toString());
                    user.setPassword(edAddPass.getText().toString());
                    user.setRole(rolePosition);
                    if (userDAO.insertData(user)) {
                        Toast.makeText(AddUserActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    } else {
                        Toast.makeText(AddUserActivity.this, "Insert failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancelAdd = findViewById(R.id.btnCancelAdd);
        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clearInputFields() {
        edAddUsername.setText("");
        edAddFullname.setText("");
        edAddEmail.setText("");
        edAddPhoneNumber.setText("");
        edAddPass.setText("");
        edAddRePass.setText("");
    }

    public int validate() {
        int check = 1;
        String username = edAddUsername.getText().toString();
        String fullname = edAddFullname.getText().toString();
        String email = edAddEmail.getText().toString();
        String phoneNumberStr = edAddPhoneNumber.getText().toString();
        String password = edAddPass.getText().toString();
        String rePassword = edAddRePass.getText().toString();

        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || phoneNumberStr.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            try {
                int phoneNumber = Integer.parseInt(phoneNumberStr);

                User existingUser = userDAO.selectID(username);
                if (existingUser != null) {
                    Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
                    check = -1;
                } else if (!isValidEmail(email)) {
                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
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

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
}