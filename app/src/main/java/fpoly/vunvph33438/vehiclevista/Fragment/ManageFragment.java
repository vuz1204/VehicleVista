package fpoly.vunvph33438.vehiclevista.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import fpoly.vunvph33438.vehiclevista.ChangePasswordActivity;
import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.LoginActivity;
import fpoly.vunvph33438.vehiclevista.Model.User;
import fpoly.vunvph33438.vehiclevista.R;

public class ManageFragment extends Fragment {

    View view;
    TextView tvFullname, tvPhoneNumber, tvSalesReport, tvChangePassword, tvLogout;
    UserDAO userDAO;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manage, container, false);

        tvFullname = view.findViewById(R.id.tvFullname);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        String username = getActivity().getIntent().getStringExtra("username");
        userDAO = new UserDAO(getActivity());
        User user = userDAO.selectID(username);
        String fullname = user.getFullname();
        int phoneNumber = user.getPhoneNumber();
        tvFullname.setText("Welcome " + fullname + "!");
        tvPhoneNumber.setText(String.valueOf("0" + phoneNumber));

        tvSalesReport = view.findViewById(R.id.tvSalesReport);
        tvSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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