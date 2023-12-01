package fpoly.vunvph33438.vehiclevista.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import fpoly.vunvph33438.vehiclevista.AddUserActivity;
import fpoly.vunvph33438.vehiclevista.ChangePasswordActivity;
import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.LoginActivity;
import fpoly.vunvph33438.vehiclevista.Model.User;
import fpoly.vunvph33438.vehiclevista.R;
import fpoly.vunvph33438.vehiclevista.SalesReportActivity;
import fpoly.vunvph33438.vehiclevista.UpdateProfileActivity;
import fpoly.vunvph33438.vehiclevista.UserManagementActivity;

public class ManageFragment extends Fragment {

    View view;
    TextView tvFullname, tvEmail, tvEditProfile, tvAddUser, tvUserManagement, tvSalesReport, tvChangePassword, tvLogout;
    UserDAO userDAO;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manage, container, false);

        tvFullname = view.findViewById(R.id.tvFullname);
        tvEmail = view.findViewById(R.id.tvEmail);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "");

        userDAO = new UserDAO(getActivity());
        User user = userDAO.selectID(username);

        if (user != null) {
            String fullname = user.getFullname();
            String email = user.getEmail();
            tvFullname.setText("Welcome " + fullname + "!");
            tvEmail.setText(email);
        }

        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        tvUserManagement = view.findViewById(R.id.tvUserManagement);
        tvUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserManagementActivity.class);
                startActivity(intent);
            }
        });

        tvAddUser = view.findViewById(R.id.tvAddUser);
        tvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUserActivity.class);
                startActivity(intent);
            }
        });
        tvSalesReport = view.findViewById(R.id.tvSalesReport);
        tvSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SalesReportActivity.class);
                startActivity(intent);
            }
        });

        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        tvLogout = view.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });

        return view;
    }
}