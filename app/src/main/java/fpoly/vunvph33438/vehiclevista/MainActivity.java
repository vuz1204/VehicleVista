package fpoly.vunvph33438.vehiclevista;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Fragment.AccountFragment;
import fpoly.vunvph33438.vehiclevista.Fragment.BrandFragment;
import fpoly.vunvph33438.vehiclevista.Fragment.CarBookingHistoryFragment;
import fpoly.vunvph33438.vehiclevista.Fragment.CarFragment;
import fpoly.vunvph33438.vehiclevista.Fragment.HistoryFragment;
import fpoly.vunvph33438.vehiclevista.Fragment.HomeFragment;
import fpoly.vunvph33438.vehiclevista.Fragment.ManageFragment;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("VehicleVista");


        bottomNavigationView = findViewById(R.id.nvBottom);
        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        if (role != null && role.equalsIgnoreCase("admin")) {
            bottomNavigationView.inflateMenu(R.menu.drawer_view_admin);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new BrandFragment()).commit();
        } else {
            bottomNavigationView.inflateMenu(R.menu.drawer_view_user);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_loaiXe) {
                getSupportActionBar().setTitle("Brand");
                BrandFragment brandFragment = new BrandFragment();
                replaceFragment(brandFragment);
            } else if (itemId == R.id.nav_xe) {
                getSupportActionBar().setTitle("Car");
                CarFragment carFragment = new CarFragment();
                replaceFragment(carFragment);
            } else if (itemId == R.id.nav_lichSu) {
                getSupportActionBar().setTitle("History");
                HistoryFragment historyFragment = new HistoryFragment();
                replaceFragment(historyFragment);
            } else if (itemId == R.id.sub_quanLy) {
                getSupportActionBar().setTitle("Manage");
                ManageFragment manageFragment = new ManageFragment();
                replaceFragment(manageFragment);
            } else if (itemId == R.id.sub_trangChu) {
                getSupportActionBar().setTitle("Home");
                HomeFragment homeFragment = new HomeFragment();
                replaceFragment(homeFragment);
            } else if (itemId == R.id.nav_lichSuDatXe) {
                getSupportActionBar().setTitle("Car booking history");
                CarBookingHistoryFragment carBookingHistoryFragment = new CarBookingHistoryFragment();
                replaceFragment(carBookingHistoryFragment);
            } else if (itemId == R.id.sub_taiKhoan) {
                getSupportActionBar().setTitle("Account");
                AccountFragment accountFragment = new AccountFragment();
                replaceFragment(accountFragment);
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}