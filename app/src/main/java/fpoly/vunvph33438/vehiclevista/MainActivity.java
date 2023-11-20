package fpoly.vunvph33438.vehiclevista;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpoly.vunvph33438.vehiclevista.Fragment.BrandFragment;

public class MainActivity extends AppCompatActivity {

    private String username = "admin"; // replace with actual username

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nvBottom);

        if ("admin".equals(username)) {
            // Admin user, inflate admin menu
            bottomNavigationView.inflateMenu(R.menu.drawer_view_admin);
        } else {
            // Regular user, inflate regular menu
            bottomNavigationView.inflateMenu(R.menu.drawer_view_user);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Handle navigation item clicks here
            int itemId = item.getItemId();
            if (itemId == R.id.nav_loaiXe) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_loaiXe, new BrandFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.nav_xe) {
                // Handle the click for nav_xe
            } else if (itemId == R.id.nav_lichSu) {
                // Handle the click for nav_lichSu
            } else if (itemId == R.id.sub_quanLy) {
                // Handle the click for sub_quanLy
            } else if (itemId == R.id.sub_trangChu) {
                // Handle the click for sub_trangChu
            } else if (itemId == R.id.nav_lichSuDatXe) {
                // Handle the click for nav_lichSuDatXe
            } else if (itemId == R.id.sub_taiKhoan) {
                // Handle the click for sub_taiKhoan
            }
            return true;
        });
    }
}
