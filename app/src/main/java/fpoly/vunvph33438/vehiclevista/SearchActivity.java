package fpoly.vunvph33438.vehiclevista;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Adapter.BrandSpinner;
import fpoly.vunvph33438.vehiclevista.Adapter.BrandSpinnerUser;
import fpoly.vunvph33438.vehiclevista.Adapter.CarHomeAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.Model.Car;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rcvCarSearch;
    CarHomeAdapter carHomeAdapter;
    CarDAO carDAO;
    ArrayList<Car> listCar = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcvCarSearch = findViewById(R.id.rcvCarSearch);
        carDAO = new CarDAO(this);
        listCar = carDAO.getAllCars();
        carHomeAdapter = new CarHomeAdapter(this, listCar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rcvCarSearch.setLayoutManager(gridLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rcvCarSearch.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rcvCarSearch.setAdapter(carHomeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                carHomeAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                carHomeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.actionFilter) {
            showFilterDialog();
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        Spinner spinnerBrand = view.findViewById(R.id.spinnerBrand);
        BrandDAO brandDAO = new BrandDAO(this);
        ArrayList<Brand> brandList = brandDAO.selectAll();
        BrandSpinnerUser brandAdapter = new BrandSpinnerUser(this, brandList);
        spinnerBrand.setAdapter(brandAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Filter Options")
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Brand selectedBrand = (Brand) spinnerBrand.getSelectedItem();
                        if (selectedBrand != null) {
                            int selectedBrandId = selectedBrand.getIdBrand();

                            if (selectedBrandId != 0) {
                                ArrayList<Car> filteredCars = filterCarsByBrand(selectedBrandId);
                                filteredCars = filterCarsByPrice(filteredCars, view);

                                carHomeAdapter.setCarList(filteredCars);
                                carHomeAdapter.notifyDataSetChanged();
                            } else {
                                carHomeAdapter.setCarList(listCar);
                                carHomeAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Car> filterCarsByPrice(ArrayList<Car> cars, View view) {
        EditText edFromPrice = view.findViewById(R.id.edFromPrice);
        EditText edToPrice = view.findViewById(R.id.edToPrice);

        if (edFromPrice == null || edToPrice == null) {
            return new ArrayList<>();
        }

        String fromPriceString = edFromPrice.getText().toString().trim();
        int fromPrice = fromPriceString.isEmpty() ? 0 : Integer.parseInt(fromPriceString);

        String toPriceString = edToPrice.getText().toString().trim();
        int toPrice = toPriceString.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(toPriceString);

        ArrayList<Car> filteredCars = new ArrayList<>();

        for (Car car : cars) {
            int carPrice = car.getPrice();

            if (carPrice >= fromPrice && carPrice <= toPrice) {
                filteredCars.add(car);
            }
        }

        return filteredCars;
    }

    private ArrayList<Car> filterCarsByBrand(int brandId) {
        ArrayList<Car> filteredCars = new ArrayList<>();
        for (Car car : listCar) {
            if (car.getIdBrand() == brandId) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }
}